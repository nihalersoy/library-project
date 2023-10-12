package com.tpe.payload.request.business;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.File;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookRequest {


    @NotNull(message = "Please provide a name for the book")
    @Size(min = 2,max = 80,message = "Book name must be between 2 - 80 chars")
    private String name;

    @NotNull(message = "Please provide an ISBN number for the book")
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{5}-\\d{2}-\\d")
    private String isbn;

    @Nullable
    private Integer pageCount;

    @NotNull(message = "This field can not be empty")
    private Set<Long> authorIdList;

    @NotNull(message = "This field can not be empty")
    private Long publisherId;

    @Digits(integer = 4,fraction = 0,message = "Please only provide the publish year")
    private int publishDate;

    @NotNull(message = "This field can not be empty")
    private Long categoryId;

    @Nullable
    private File image;

    @NotNull(message = "Please provide a shelf code for the book")
    @Size(min = 6,max = 6, message = "Please provide a 6 char shelf code")
    @Pattern(regexp = "^[A-Z]{2}-\\\\d{3}$",message = "Please provide the correct format ex:AA-999")
    private String shelfCode;

    @NotNull(message = "Please state if this book is featured")//TODO default false
    private boolean featured;

}
