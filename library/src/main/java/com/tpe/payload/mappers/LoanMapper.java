package com.tpe.payload.mappers;

import com.tpe.entity.business.Book;
import com.tpe.entity.business.Loan;
import com.tpe.entity.user.User;
import com.tpe.payload.response.business.LoanResponse;
import com.tpe.payload.response.business.LoanResponseForManager;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class LoanMapper {

    public LoanResponse mapLoanToLoanResponse (Loan loan) {

        return LoanResponse.builder()
                .id(loan.getId())
                .userId(loan.getUser().getId())
                .loanDate(loan.getLoanDate())
                .expireDate(loan.getExpireDate())
                .returnDate(loan.getReturnDate())
                .bookList(loan.getBookList())
                .build();
    }

    public LoanResponseForManager mapLoanToLoanResponseForManager (Loan loan){

        return LoanResponseForManager.builder()
                .id(loan.getId())
                .userId(loan.getUser().getId())
                .loanDate(loan.getLoanDate())
                .expireDate(loan.getExpireDate())
                .returnDate(loan.getReturnDate())
                .bookList(loan.getBookList())
                .notes(loan.getNotes())
                .build();
    }

    public Loan createLoan (LocalDateTime loanDate, String notes, User user, Collection<Book> bookList){
        return Loan.builder()
                .loanDate(loanDate)
                .notes(notes)
                .user(user)
                .bookList((Set<Book>) bookList)
                .build();

    }
}
