package com.tpe.entity.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) //TODO min:2 max:80
    private String name;

    @Column(nullable = false,length = 17) //TODO format:999-99-99999-99-9
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
