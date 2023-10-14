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

@Table(name = "t_publisher")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) //TODO min:2 max:50
    private String name;

    @Column(nullable = false) //TODO default false
    private Boolean builtIn;

    @OneToMany(mappedBy = "publisher",cascade = CascadeType.REMOVE)//TODO bu tamamen benim fikrimdi, değiştirilebilir
    private List<Book> books;


}
