package com.tpe.service.helper;

import com.tpe.entity.user.User;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.repository.user.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class UserHelper {

    private final UserRepository userRepository;

    //servletRequest ile User getirme
    public User getUserByEmail(HttpServletRequest servletRequest){

        String email = (String) servletRequest.getAttribute("email");

        return userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND,email)));

    }



}
