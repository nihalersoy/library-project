package com.tpe.payload.mappers;

import com.tpe.entity.enums.Role;
import com.tpe.entity.user.User;
import com.tpe.payload.request.user.UserRequest;
import com.tpe.payload.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapUserRequestToUser (UserRequest userRequest){

        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .address(userRequest.getAddress())
                .phone(userRequest.getPhone())
                .birthDate(userRequest.getBirthDate())
                .email(userRequest.getEmail())
                .build();
    }

    public UserResponse mapUserToUserResponse (User user){

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .score(user.getScore())
                .address(user.getAddress())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .role(Role.MEMBER.getName())
                .build();
    }




}
