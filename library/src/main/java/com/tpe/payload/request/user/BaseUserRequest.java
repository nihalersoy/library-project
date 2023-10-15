package com.tpe.payload.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public abstract class BaseUserRequest {

    @NotNull(message = "This field can not be empty")
    @Email(message = "This is not a valid email address")
    private String email;

    @NotNull(message = "This field can not be empty")
    private String password;


}
