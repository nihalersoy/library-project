package com.tpe.service.helper;

import com.tpe.entity.user.User;
import com.tpe.exception.ConflictException;
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

    public boolean doesUserExist(String email){

        if (userRepository.existsByEmail(email)){
            throw new ConflictException(String.format(ErrorMessages.USER_ALREADY_EXISTS,email));
        }

        return false;
    }

    public User isUserExistsById (Long id){

        return userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND_BY_ID,id)));

    }



}
