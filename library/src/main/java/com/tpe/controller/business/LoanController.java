package com.tpe.controller.business;

import com.tpe.entity.business.Loan;
import com.tpe.payload.response.business.LoanResponse;
import com.tpe.payload.response.business.LoanResponseForManager;
import com.tpe.payload.response.business.ResponseMessage;
import com.tpe.service.business.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/getLoans")
    public ResponseMessage<Page<LoanResponse>> getLoans(
            @RequestParam (value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam(value = "sort",defaultValue = "loanDate") String sort,
            @RequestParam(value = "type",defaultValue = "desc") String type,
            HttpServletRequest servletRequest){

        return loanService.getLoans(page,size,sort,type,servletRequest);
    }

    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @GetMapping("/{id}")
    public ResponseMessage<List<LoanResponse>> getLoansByUserId(@PathVariable Long id){

        return loanService.getLoansByUserId(id);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseMessage<Page<LoanResponseForManager>> getLoansForManagement (
            @PathVariable Long id,
            @RequestParam (value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam(value = "sort",defaultValue = "loanDate") String sort,
            @RequestParam(value = "type",defaultValue = "desc") String type){

        return loanService.getLoansForManagement(id,page,size,sort,type);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @GetMapping("/book/{bookId}")
    public ResponseMessage<Page<LoanResponseForManager>> getLoansByBookId(
            @PathVariable Long bookId,
            @RequestParam (value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam(value = "sort",defaultValue = "loanDate") String sort,
            @RequestParam(value = "type",defaultValue = "desc") String type){

        return loanService.getLoansByBookId(bookId,page,size,sort,type);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @GetMapping("/auth/{id}")
    public ResponseMessage<LoanResponseForManager> getLoansById(@PathVariable Long id){

        return loanService.getLoansByLoanId(id);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @PostMapping("/createLoan")
    public ResponseMessage<LoanResponseForManager> saveLoan (
            @RequestParam (value = "userId") Long userId,
            @RequestParam (value = "bookIdList") List<Long> bookIdList,
            @RequestParam (value = "notes") String notes){

        return loanService.saveLoan(userId,bookIdList,notes);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    @PostMapping("/updateLoan/{id}")
    public ResponseMessage<LoanResponseForManager> updateLoanById (
            @PathVariable (value = "loanId") Long loanId,
            @RequestParam (value = "notes") String notes,
            @RequestParam (value = "expireDate") LocalDateTime expireDate,
            @RequestParam (value = "returnDate") LocalDateTime returnDate){

        return loanService.updateLoanById(loanId,notes,expireDate,returnDate);
    }



}
