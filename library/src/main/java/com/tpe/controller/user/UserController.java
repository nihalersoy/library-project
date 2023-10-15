package com.tpe.controller.user;

import com.tpe.payload.request.user.SaveUserRequest;
import com.tpe.payload.request.user.UserRequest;
import com.tpe.payload.request.user.UserSignIn;
import com.tpe.payload.response.business.ResponseMessage;
import com.tpe.payload.response.user.UserResponse;
import com.tpe.payload.response.user.UserSignInResponse;
import com.tpe.service.helper.UserHelper;
import com.tpe.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserHelper userHelper;

    //NOT: signIn()
    @PostMapping("/signin")
    public ResponseMessage<UserSignInResponse> signInForAnonymous(
            @RequestBody @Valid UserSignIn userSignIn){

        return userService.signIn(userSignIn);
    }

    //NOT: register()
    @PostMapping("/register") //TODO Whiteliste ekle
    public ResponseMessage<UserResponse> register (
            @RequestBody @Valid UserRequest userRequest){

        return userService.register(userRequest);
    }

    //NOT: member-employee-admin için giren kişinin bilgilerini getirecek
    @PreAuthorize("hasAnyAuthority('MEMBER','ADMIN','EMPLOYEE')")
    @PostMapping("/authUser")
    public ResponseMessage<UserResponse> getAuthenticatedUser(HttpServletRequest servletRequest){

        return userService.getAuthenticatedUser(servletRequest);
    }

    //NOT: getUserLoans() It will return authenticated user loans
//    @PreAuthorize("hasAnyAuthority('MEMBER','ADMIN','EMPLOYEE')")
//    @PostMapping("/user/loans")
//    public ResponseMessage<UserWithLoans> getUserLoans(
//            @RequestParam (value = "page", defaultValue = "0") int page,
//            @RequestParam (value = "size", defaultValue = "20") int size,
//            @RequestParam (value = "sort", defaultValue = "createDate") String sort,
//            @RequestParam (value = "type", defaultValue = "desc") String type){
//
//
//    }


    //NOT: saveUser()
    @PreAuthorize("hasAnyAuthority('MEMBER','ADMIN','EMPLOYEE')")
    @PostMapping("/saveUser")
    public ResponseMessage<UserResponse> saveUser(
            @RequestBody @Valid SaveUserRequest saveUserRequest,
            HttpServletRequest servletRequest){

        return userService.saveUser(saveUserRequest,servletRequest);
    }







}
