package com.tpe.payload.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class UserRequest extends BaseUserRequest {

    @NotNull(message = "This field can not be empty")
    @Size(min = 2,max = 30,message = "First name should be between 2 - 30 chars")
    private String firstName;

    @NotNull(message = "This field can not be empty")
    @Size(min = 2,max = 30,message = "Last name should be between 2 - 30 chars")
    private String lastName;

    @NotNull(message = "This field can not be empty")
    @Size(min = 10,max = 100,message ="Address should be between 10 - 100 chars" )
    private String address;

    @NotNull(message = "This field can not be empty")
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}")
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate birthDate;


}
