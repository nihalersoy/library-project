package com.tpe.controller.user;

import com.tpe.payload.request.user.SaveUserRequest;
import com.tpe.payload.request.user.UserRequest;
import com.tpe.payload.request.user.UserSignIn;
import com.tpe.payload.response.business.ResponseMessage;
import com.tpe.payload.response.user.UserResponse;
import com.tpe.payload.response.user.UserSignInResponse;
import com.tpe.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //NOT: signIn()
    @PostMapping("/signin") //TODO WhiteListe ekle
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

    //NOT: saveUser()
    @PreAuthorize("hasAnyAuthority('MEMBER','ADMIN','EMPLOYEE')")
    @PostMapping("/saveUser")
    public ResponseMessage<UserResponse> saveUser(
            @RequestBody @Valid SaveUserRequest saveUserRequest,
            HttpServletRequest servletRequest){

        return userService.saveUser(saveUserRequest,servletRequest);
    }





}
