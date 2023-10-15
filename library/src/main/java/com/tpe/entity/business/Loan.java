package com.tpe.entity.business;

import com.tpe.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

@Table(name = "t_loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime loanDate;//TODO yyyy-MM-dd HH:mm timezone:US

    @Column(nullable = false)
    private LocalDateTime expireDate;//TODO yyyy-MM-dd HH:mm timezone:US

    @Column(nullable = false)
    private LocalDateTime returnDate;//TODO yyyy-MM-dd HH:mm timezone:US

    private String notes;//TODO max:300

    @ManyToOne
    private User user;

    @ManyToMany()
    @JoinTable(name = "loans_books",joinColumns = @JoinColumn(name = "loan_id"),inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> bookList;

    @PreRemove
    private void removeBooksFromLoanList (){
        bookList.forEach(book -> book.getLoanList().remove(this));
        bookList.clear();
    }



}
