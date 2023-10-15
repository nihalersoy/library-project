package com.tpe.service.user;

import com.tpe.LibraryApplication;
import com.tpe.entity.enums.Role;
import com.tpe.entity.user.User;
import com.tpe.entity.user.UserRole;
import com.tpe.payload.request.user.SaveUserRequest;
import com.tpe.payload.response.user.UserResponse;
import com.tpe.exception.BadRequestException;
import com.tpe.payload.mappers.UserMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.request.user.UserRequest;
import com.tpe.payload.request.user.UserSignIn;
import com.tpe.payload.response.business.ResponseMessage;
import com.tpe.payload.response.user.UserSignInResponse;
import com.tpe.repository.user.UserRepository;
import com.tpe.security.jwt.JwtUtils;
import com.tpe.security.service.UserDetailsImpl;
import com.tpe.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserHelper userHelper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final UserRepository userRepository;

    public ResponseMessage<UserSignInResponse> signIn(UserSignIn userSignIn) {

        //1. adım email ve password gelicek
        String email = userSignIn.getEmail();
        String password = userSignIn.getPassword();

        //2. adım auth nesnesi olusturulacak
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password));

        //3. validasyonu yapılan kullanıcıyı SecurityContextHolder atılacak
        //varolan context getirildi, içine auth nesnesi setlendi
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //token oluşturuldu
        String token = "Bearer "+ jwtUtils.generateToken(authentication);

        //Sign in olan User getirildi
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        List<String> roles = userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        //response nesnesindeki diğer filedları setliyoruz
        UserSignInResponse.UserSignInResponseBuilder userSignInResponse = UserSignInResponse.builder();
        userSignInResponse.phone(userDetails.getPhone());
        userSignInResponse.email(userDetails.getEmail());
        userSignInResponse.token(token.substring(7));
        if (!roles.isEmpty()){
            userSignInResponse.roles(roles);
        }

        return ResponseMessage.<UserSignInResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(userSignInResponse.build())
                .message(SuccessMessages.SIGN_IN)
                .build();

    }

    public ResponseMessage<UserResponse> register(UserRequest userRequest) {

        //email zaten sistemde var mı kontrolü
        userHelper.doesUserExist(userRequest.getEmail());

        //DTO-->POJO
        User user = userMapper.mapUserRequestToUser(userRequest);

        //password encode edilecek
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        //eksik fieldlar tamamlanacak
        user.setScore(0);
        user.setCreateDate(LocalDateTime.now());
        user.setBuiltIn(Boolean.FALSE);

        //rol bilgisi ayarlanacak
        user.getUserRoles().add(userRoleService.getUserRole(Role.MEMBER));

        //user kaydedilecek
        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .object(userMapper.mapUserToUserResponse(savedUser))
                .message(SuccessMessages.REGISTER)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<UserResponse> getAuthenticatedUser(HttpServletRequest servletRequest) {

        User user = userHelper.getUserByEmail(servletRequest);
        return ResponseMessage.<UserResponse>builder()
                .object(userMapper.mapUserToUserResponse(user))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public int countAllAdmins() {
        return userRepository.countAllAdmins(Role.ADMIN);
    }

    public ResponseMessage<UserResponse> saveUser
            (SaveUserRequest saveUserRequest, HttpServletRequest servletRequest) {

        //requestle gelen email zaten var mı?
        userHelper.doesUserExist(saveUserRequest.getEmail());

        //DTO-->POJO
        User user = userMapper.mapUserRequestToUser(saveUserRequest);

        //methodu kim tetikledi bakıyoruz
        User savingUser = userHelper.getUserByEmail(servletRequest);

        //Requestle gelen strign role-->UserRole çeviriyoruz
        Role roleRequest = userRoleService.getRoleFromString(saveUserRequest.getRole());
        UserRole userRoleRequest = userRoleService.getUserRole(roleRequest);

        //rol setlemesinden önce kontrol yapılıyor
        if (savingUser.getUserRoles().stream()
                .noneMatch(userRole -> userRole.getName().equalsIgnoreCase(Role.ADMIN.getName()))
                && !(roleRequest.getName().equalsIgnoreCase(Role.MEMBER.getName()))){ //admin değilse ve member harici bir user save etmek isterse

            throw new BadRequestException(String.format(ErrorMessages.SAVE_USER_ERROR,saveUserRequest.getRole()));
        }
         //rol setlemesi yapılacak
        user.setUserRoles(new HashSet<>());
        user.getUserRoles().add(userRoleRequest);

        //password encode
        user.setPassword( passwordEncoder.encode(saveUserRequest.getPassword())  );

        //eksik field setlemesi
        user.setScore(0);
        user.setCreateDate(LocalDateTime.now());
        user.setBuiltIn(Boolean.FALSE);
        user.setLoanList(new ArrayList<>());

        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .object(userMapper.mapUserToUserResponse(user))
                .message(SuccessMessages.USER_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .build();

    }

    //RUNNER ICIN
    public ResponseMessage<UserResponse> saveAdmin
            (SaveUserRequest saveUserRequest) {

        //requestle gelen email zaten var mı?
        userHelper.doesUserExist(saveUserRequest.getEmail());

        //DTO-->POJO
        User user = userMapper.mapUserRequestToUser(saveUserRequest);


        //Requestle gelen strign role-->UserRole çeviriyoruz
        Role roleRequest = userRoleService.getRoleFromString(saveUserRequest.getRole());
        UserRole userRoleRequest = userRoleService.getUserRole(roleRequest);

        //rol setlemesi yapılacak
        user.setUserRoles(new HashSet<>());
        user.getUserRoles().add(userRoleRequest);

        //password encode
        user.setPassword( passwordEncoder.encode(saveUserRequest.getPassword())  );

        //eksik field setlemesi
        user.setScore(0);
        user.setCreateDate(LocalDateTime.now());
        user.setBuiltIn(Boolean.TRUE);

        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .object(userMapper.mapUserToUserResponse(user))
                .message(SuccessMessages.USER_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .build();

    }




}










