package com.tpe.payload.response.business;

import com.tpe.entity.business.Book;
import lombok.*;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

public class PublisherResponse {

    private Long id;
    private String name;
    private List<Book> books;

}
