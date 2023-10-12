package com.tpe.payload.mappers;

import com.tpe.entity.business.Book;
import com.tpe.payload.response.business.BookResponse;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookResponse mapBookToBookResponse (Book book){

        return BookResponse.builder()
                .name(book.getName())
                .isbn(book.getIsbn())
                .pageCount(book.getPageCount())
                .publishDate(book.getPublishDate())
                .image(book.getImage())
                .loanable(book.isLoanable())
                .shelfCode(book.getShelfCode())
                .active(book.getActive())
                .featured(book.isFeatured())
                .authors(book.getAuthors())
                .publisher(book.getPublisher())
                .category(book.getCategory())
                .build();
    }

}
