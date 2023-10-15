package com.tpe.payload.request.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class UserSignIn extends BaseUserRequest{

    @NotNull(message = "This field can not be empty")//TODO sor bakalım millet nasıl yapmis
    private String role;

}
