package com.tpe.payload.response.business;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class LoanResponseForManager extends LoanResponse {

    private String notes;

}
