package com.tpe.payload.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSignInResponse {

    private String token;

    private String role;

    private String phone;

    private String email;


}
