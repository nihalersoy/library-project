package com.tpe.payload.request.business;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@SuperBuilder
public class BookRequestForUpdate extends BookRequest{

    @NotNull(message = "Please state if book is active")
    private Boolean active;


}
