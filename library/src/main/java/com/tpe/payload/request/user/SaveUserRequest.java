package com.tpe.payload.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SaveUserRequest extends UserRequest{

    @NotNull(message = "This field can not be empty")
    private String role;
}
