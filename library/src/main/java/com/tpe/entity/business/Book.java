package com.tpe.entity.business;

import lombok.*;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder(toBuilder = true)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//

    @Column(nullable = false)
    private String name;//

    @Column(nullable = false,length = 17,unique = true)
    private String isbn;//

    private Integer pageCount;//

    @Column(length = 4)
    private int publishDate; //

    private File image;//

    @Column(nullable = false) //TODO default deger true
    private boolean loanable;

    @Column(nullable = false,length = 6)
    private String shelfCode;//

    @Column(nullable = false)//TODO default true
    private Boolean active;

    @Column(nullable = false) //TODO default false
    private boolean featured;//

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
