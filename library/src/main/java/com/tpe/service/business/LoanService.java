package com.tpe.service.business;

import com.tpe.entity.business.Book;
import com.tpe.entity.business.Loan;
import com.tpe.entity.user.User;
import com.tpe.exception.BadRequestException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.mappers.LoanMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.response.business.LoanResponse;
import com.tpe.payload.response.business.LoanResponseForManager;
import com.tpe.payload.response.business.ResponseMessage;
import com.tpe.repository.business.LoanRepository;
import com.tpe.service.helper.PageableHelper;
import com.tpe.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final PageableHelper pageableHelper;
    private final UserHelper userHelper;
    private final LoanMapper loanMapper;
    private final BookService bookService;

    public ResponseMessage<Page<LoanResponse>> getLoans
            (int page, int size, String sort, String type, HttpServletRequest servletRequest) {

        Pageable pageable = pageableHelper.getPageable(page,size,sort,type);

        User user = userHelper.getUserByEmail(servletRequest);
        Page<Loan> loanList = loanRepository.findAllByUser_Id(user.getId(),pageable);
        Page<LoanResponse> loanResponses = loanList.map(loanMapper::mapLoanToLoanResponse);

        return ResponseMessage.<Page<LoanResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .object(loanResponses)
                .build();
    }


    public ResponseMessage<List<LoanResponse>> getLoansByUserId(Long id) {

        //bu id ile user var mı?
        userHelper.isUserExistsById(id);

        List<Loan> loanList = loanRepository.findAllByUser_IdEquals(id);

        return ResponseMessage.<List<LoanResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .object(loanList.stream().map(loanMapper::mapLoanToLoanResponse).collect(Collectors.toList()))
                .build();
    }


    public ResponseMessage<Page<LoanResponseForManager>> getLoansForManagement
            (Long id, int page, int size, String sort, String type) {

        Pageable pageable = pageableHelper.getPageable(page,size,sort,type);

        //Bu id ile user var mı?
        userHelper.isUserExistsById(id);

        Page<Loan> loans = loanRepository.findAllByUser_Id(id,pageable);
        Page<LoanResponseForManager> loanResponseForManagers = loans.map(loanMapper::mapLoanToLoanResponseForManager);

        return ResponseMessage.<Page<LoanResponseForManager>>builder()
                .httpStatus(HttpStatus.OK)
                .object(loanResponseForManagers)
                .build();
    }


    public ResponseMessage<Page<LoanResponseForManager>> getLoansByBookId
            (Long bookId, int page, int size, String sort, String type) {

        //bu id ile book var mı?
        bookService.isBookExistById(bookId);

        Pageable pageable = pageableHelper.getPageable(page,size,sort,type);
        Page<Loan> loans = loanRepository.findAllByBookList_Id(pageable,bookId);

        return ResponseMessage.<Page<LoanResponseForManager>>builder()
                .httpStatus(HttpStatus.OK)
                .object(loans.map(loanMapper::mapLoanToLoanResponseForManager))
                .build();
    }

    public ResponseMessage<LoanResponseForManager> getLoansByLoanId(Long id) {

        Loan loan = isLoanExistsById(id);

        return ResponseMessage.<LoanResponseForManager>builder()
                .httpStatus(HttpStatus.OK)
                .object(loanMapper.mapLoanToLoanResponseForManager(loan))
                .build();
    }

    public Loan isLoanExistsById(Long id){

        return loanRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.LOAN_NOT_FOUND,id)));
    }

    public ResponseMessage<LoanResponseForManager> saveLoan
            (Long userId, List<Long> bookIdList, String notes) {

        //bu id ile user ve book var mı bakıyoruz
        User user = userHelper.isUserExistsById(userId);
        List<Book> bookList = bookIdList.stream().map(bookService::isBookExistById).collect(Collectors.toList());


        //gönderdiği kitaplar içinde loanable olmayan var mı?
        isBookListLoanable(bookList);

        //user loanList i boş değilse alıp geri döndürmediği kitap var mı bakacağız
        List<Loan> usersLoanList = user.getLoanList();
        Set<Book> usersCurrentBooks = new HashSet<>();
        if (!usersLoanList.isEmpty()){

            for (Loan loan : usersLoanList){
                //tarihi geçen kitaplar
                if (loan.getReturnDate().isBefore(LocalDateTime.now())){
                    return ResponseMessage.<LoanResponseForManager>builder()
                            .message(String.format(ErrorMessages.USER_CAN_NOT_LOAN_BOOK,userId))
                            .build();
                } else if (loan.getReturnDate().isAfter(LocalDateTime.now())) { //tarihi geçmeyen kitaplar

                    usersCurrentBooks = loan.getBookList();
                }
            }
        }

        int userScore = user.getScore();
        int usersCurrentBookCount = usersCurrentBooks.size();
        LocalDateTime loanDate = LocalDateTime.now();
        Loan loan = loanMapper.createLoan(loanDate,notes,user,bookList);
        //loan oluşturuldu
        Loan savedLoan = checkAndSetLoan(userScore,usersCurrentBookCount,bookList,loanDate,loan);
        loanRepository.save(savedLoan);

        //bookListteki kitapların fieldlarını setliyoruz
        setBookLoanableFalse(bookList);

        return ResponseMessage.<LoanResponseForManager>builder()
                .httpStatus(HttpStatus.CREATED)
                .object(loanMapper.mapLoanToLoanResponseForManager(loan))
                .message(SuccessMessages.LOAN_CREATED)
                .build();
    }

    private List<Book> setBookLoanableFalse (List<Book> books){

        for (Book book : books){

            book.setLoanable(false);
        }
        return books;
    }
    private Set<Book> setBookLoanableTrue (Set<Book> books){
        for (Book book : books){
            book.setLoanable(true);
        }
        return books;
    }

    private Loan checkAndSetLoan (int userScore,int usersCurrentBookCount,List<Book> bookList,LocalDateTime loanDate,Loan loan){

        switch (userScore){
            case 2:
                //20 gün için 5 kitap alabilir
                if (bookList.size()>5 || (bookList.size()+usersCurrentBookCount)>5 ){
                    throw new BadRequestException(String.format(ErrorMessages.OVER_MAX_BOOK_COUNT,"5"));
                }
                loan.setExpireDate(loanDate.plusDays(20));
                loan.setReturnDate(loanDate.plusDays(19));
                break;
            case 1:
                //15 gün 4 kitap
                if (bookList.size()>4 ||(bookList.size()+usersCurrentBookCount)>4 ){
                    throw new BadRequestException(String.format(ErrorMessages.OVER_MAX_BOOK_COUNT,"4"));
                }
                loan.setExpireDate(loanDate.plusDays(15));
                loan.setExpireDate(loanDate.plusDays(14));
                break;
            case 0:
                //10 gün 3 kitap
                if (bookList.size()>3 ||(bookList.size()+usersCurrentBookCount)>3 ){
                    throw new BadRequestException(String.format(ErrorMessages.OVER_MAX_BOOK_COUNT,"3"));
                }
                loan.setExpireDate(loanDate.plusDays(10));
                loan.setReturnDate(loanDate.plusDays(9));
                break;
            case -1:
                //6 gün 2 kitap
                if (bookList.size()>2 ||(bookList.size()+usersCurrentBookCount)>2 ){
                    throw new BadRequestException(String.format(ErrorMessages.OVER_MAX_BOOK_COUNT,"3"));
                }
                loan.setExpireDate(loanDate.plusDays(6));
                loan.setReturnDate(loanDate.plusDays(5));
                break;
            case -2:
                //3 gün 1 kitap
                if (bookList.size()>1 ||(bookList.size()+usersCurrentBookCount)>1 ){
                    throw new BadRequestException(String.format(ErrorMessages.OVER_MAX_BOOK_COUNT,"3"));
                }
                loan.setExpireDate(loanDate.plusDays(3));
                loan.setReturnDate(loanDate.plusDays(2));
                break;
        }

        return loan;

    }

    private void isBookListLoanable (List<Book> bookList){
        if (bookList.stream().anyMatch(book ->!book.isLoanable())){

            throw new  BadRequestException(ErrorMessages.LOAN_NOT_AVAILABLE);
        }
    }


    public ResponseMessage<LoanResponseForManager> updateLoanById
            (Long loanId, String notes, LocalDateTime expireDate, LocalDateTime returnDate) {

        //bu id ile loan var mı?
        Loan loan =isLoanExistsById(loanId);

        //kitapları geri getirdiyse
        if (returnDate!=null){
            Set<Book> bookList = loan.getBookList();
            setBookLoanableTrue(bookList);

            User user = loan.getUser();
            if (returnDate.isBefore(loan.getReturnDate()) || returnDate.equals(loan.getReturnDate()) ){
                user.setScore(user.getScore()+1);
            } else if (returnDate.isAfter(loan.getReturnDate())) {
                user.setScore(user.getScore()-1);
            }
        }

        loan.setExpireDate(expireDate);
        loan.setNotes(notes);

        loanRepository.save(loan);

        return ResponseMessage.<LoanResponseForManager>builder()
                .httpStatus(HttpStatus.OK)
                .object(loanMapper.mapLoanToLoanResponseForManager(loan))
                .message(SuccessMessages.LOAN_UPDATED)
                .build();
    }
}
