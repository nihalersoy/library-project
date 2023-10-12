package com.tpe.payload.request.business;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder(toBuilder = true)
public class BookRequestForUpdate extends BookRequest{

    @NotNull(message = "Please state if book is active")
    private Boolean active;


}
