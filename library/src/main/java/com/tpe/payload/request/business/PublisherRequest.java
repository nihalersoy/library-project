package com.tpe.payload.request.business;

import com.tpe.entity.business.Book;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PublisherRequest {


    @Size(min = 2,max = 50, message = "Publisher name must be between 2 - 50 chars")
    @NotNull(message = "This field can not be empty")
    private String name;

    @NotNull(message = "This field can not be empty")
    private Boolean builtIn;

    private List<Long> bookIdList;
}
