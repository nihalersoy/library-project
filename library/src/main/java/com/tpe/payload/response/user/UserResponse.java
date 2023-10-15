package com.tpe.payload.response.user;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder(toBuilder = true)
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private int score;

    private String address;

    private String phone;

    private LocalDate birthDate;

    private String email;

    private String role;

}
