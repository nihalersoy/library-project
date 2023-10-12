package com.tpe.entity.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "t_author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) //TODO min:4 max:70
    private String name;

    @Column(nullable = false) //TODO default false
    private Boolean builtIn;

    @ManyToMany(mappedBy = "authors")
    @JoinTable(name = "authors_books",joinColumns = @JoinColumn(name = "author_id"),inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books;

    @PreRemove
    private void removeBooksFromAuthor(){
        books.forEach(book -> book.getAuthors().remove(this));
        books.clear();
    }



}
