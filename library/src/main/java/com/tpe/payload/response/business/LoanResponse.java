package com.tpe.payload.response.business;

import com.tpe.entity.business.Book;
import lombok.*;
import lombok.experimental.SuperBuilder;


import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class LoanResponse {

    private Long id;
    private Long userId;
    private LocalDateTime loanDate;
    private LocalDateTime expireDate;
    private LocalDateTime returnDate;
    private Set<Book> bookList;

}
