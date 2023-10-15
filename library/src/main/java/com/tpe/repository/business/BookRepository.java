package com.tpe.repository.business;


import com.tpe.entity.business.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("SELECT b FROM Book b WHERE (:q IS NULL OR b.name = :q OR b.isbn = :q " +
            "OR b.publisher.name = :q OR EXISTS (SELECT a FROM b.authors a WHERE a.name = :q)) " +
            "AND (:category = 'null' OR b.category = :category) " +
            "AND (:author = 'null' OR EXISTS (SELECT a FROM b.authors a WHERE a.name = :author)) " +
            "AND (:publisher = 'null' OR b.publisher.name = :publisher) " +
            "AND (b.active = TRUE OR :isAdmin = TRUE)")
    Page<Book> findBooksByFilters(
            @Param("q") String q, @Param("category") String category, @Param("author") String author,
            @Param("publisher") String publisher, @Param("isAdmin") boolean isAdmin, Pageable pageable
    );

    boolean existsByIsbn(String isbn);


}
