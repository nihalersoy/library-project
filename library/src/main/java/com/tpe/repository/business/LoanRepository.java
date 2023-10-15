package com.tpe.repository.business;

import com.tpe.entity.business.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {


    Page<Loan> findAllByUser_Id(Long id, Pageable pageable);

    List<Loan> findAllByUser_IdEquals(Long id);

    @Query("SELECT l FROM Loan l WHERE :bookId MEMBER OF l.bookList.id ")
    Page<Loan> findAllByBookList_Id(Pageable pageable, Long bookId);


}
