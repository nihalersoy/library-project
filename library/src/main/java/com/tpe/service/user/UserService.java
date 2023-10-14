package com.tpe.service.user;

import com.tpe.entity.enums.Role;
import com.tpe.entity.user.User;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

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
        String token = "Bearer " + jwtUtils.generateToken(authentication);

        //Sign in olan User getirildi
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        //user girdiği rol bilgisi doğru mu kontrolü
        checkIfRoleCorrect(userSignIn.getRole());

        //user hangi rolle giriş yapmış kontrolü
        String role = getUserRole(userSignIn.getRole());

        //response nesnesindeki diğer filedları setliyoruz
        UserSignInResponse.UserSignInResponseBuilder userSignInResponse = UserSignInResponse.builder();
        userSignInResponse.phone(userDetails.getPhone());
        userSignInResponse.email(userDetails.getEmail());
        userSignInResponse.token(token);
        if (role!=null){
            userSignInResponse.role(role);
        }

        return ResponseMessage.<UserSignInResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(userSignInResponse.build())
                .message(SuccessMessages.SIGN_IN)
                .build();

    }

    private boolean checkIfRoleCorrect(String role){

        if ( !(role.equalsIgnoreCase(Role.ADMIN.getName()) ||
                role.equalsIgnoreCase(Role.MEMBER.getName()) ||
                role.equalsIgnoreCase(Role.EMPLOYEE.getName()) ||
                role.equalsIgnoreCase("ANONYMOUS")) ){

            throw new BadRequestException(String.format(ErrorMessages.ROLE_DOES_NOT_EXIST,role));
        }

        return true;
    }

    private String getUserRole(String role){

        if (role.equalsIgnoreCase(Role.ADMIN.getName())){

            return Role.ADMIN.getName();
        }
        if (role.equalsIgnoreCase(Role.MEMBER.getName())){

            return Role.MEMBER.getName();
        }
        if (role.equalsIgnoreCase(Role.EMPLOYEE.getName())){

            return Role.EMPLOYEE.getName();
        }

        return null;
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

        //rol setlemesi yapılıyor
        if (savingUser.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getName().equalsIgnoreCase(Role.ADMIN.getName()))){

        }

        return null;
    }

    /*
    //[!]Role bilgisi setlenecek
        if (userRole.equalsIgnoreCase(RoleType.ADMIN.name())){

            if (Objects.equals(userRequest.getUsername(),"Admin")){
                user.setBuilt_in(true);
            }
            user.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));
        }else if (userRole.equalsIgnoreCase("Dean")){
            user.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        } else if (userRole.equalsIgnoreCase("ViceDean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANT_MANAGER));
        }else {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_USERROLE,userRole));
        }

        //[!] Password encode edilecek
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //[!] isAdvisor --> False
        user.setIsAdvisor(Boolean.FALSE);
        //[!] DB ye kaydediliyor
        User savedUser = userRepository.save(user);
        //[] Response nesnesi oluşturuluyor
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATED)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();
     */
}










