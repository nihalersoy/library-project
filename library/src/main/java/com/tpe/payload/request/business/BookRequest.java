package com.tpe.payload.request.business;

import com.tpe.entity.business.Author;
import com.tpe.entity.business.Category;
import com.tpe.entity.business.Loan;
import com.tpe.entity.business.Publisher;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class BookRequest {

    //TODO min:2 max:80
    @NotNull(message = "Please provide a name for the book")
    @Size(min = 2,max = 80,message = "Book name must be between 2 - 80 chars")
    private String name;

    @Column(nullable = false,length = 17) //TODO format:999-99-99999-99-9
    @NotNull(message = "Please provide an ISBN number for the book")

    private String isbn;

    @Column(nullable = false)
    private int pageCount;

    @Column(length = 4)
    private int publishDate; //TODO only year info(yani sadece 4 karakter)

    private File image;

    @Column(nullable = false) //TODO default deger true
    private boolean loanable;

    @Column(nullable = false,length = 6)//TODO format AA-999
    private String shelfCode;

    @Column(nullable = false)//TODO default true
    private Boolean active;

    @Column(nullable = false) //TODO default false
    private boolean featured;

    @Column(nullable = false)//TODO yyyy-MM-dd HH:mm timezone:US
    private LocalDateTime createDate;

    @Column(nullable = false) //TODO default false
    private boolean builtIn;

    @ManyToMany
    private List<Loan> loanList;

    @ManyToMany
    private List<Author> authors;

    @ManyToOne
    private Publisher publisher;

    @ManyToOne
    private Category category;
}
