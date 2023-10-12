package com.tpe.service;

import com.tpe.entity.business.Book;
import com.tpe.entity.user.User;
import com.tpe.exception.BadRequestException;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.mappers.BookMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.request.business.BookRequest;
import com.tpe.payload.response.business.BookResponse;
import com.tpe.payload.response.business.ResponseMessage;
import com.tpe.repository.business.BookRepository;
import com.tpe.service.helper.PageableHelper;
import com.tpe.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserHelper userHelper;
    private final PageableHelper pageableHelper;
    private final BookMapper bookMapper;

    public Page<BookResponse> getBooksWithPage
            (HttpServletRequest servletRequest,String query, String category, String author, String publisher, int page, int size, String sort, String type) {

        //parametrelerin tamamı boş mu kontrolü
        if (query==null && category==null && author==null && publisher==null) {

            throw new BadRequestException(ErrorMessages.FIELD_EMPTY);
        }

        //Pageabale nesnesi oluşturuyoruz
        Pageable pageable = pageableHelper.getPageable(page,size,sort,type);

        //Methodu tetikleyen User getirildi
        User user = userHelper.getUserByEmail(servletRequest);

        return bookRepository.findBooksByFilters(query,category,author,publisher,isUserAdmin(user),pageable)
                .map(bookMapper::mapBookToBookResponse);

    }

    private boolean isUserAdmin (User user){

        if (user.getUserRole().getRole().getName().equals("ADMIN")){
            return true;
        }
        return false;

    }


    public ResponseMessage<BookResponse> getBookById(Long id) {

        //Bu id ile book var mı?
        Book book = bookRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.BOOK_NOT_FOUND,id)));

        return ResponseMessage.<BookResponse>builder()
                .object(bookMapper.mapBookToBookResponse(book))
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public ResponseMessage<BookResponse> saveBook(BookRequest bookRequest) {

        //gelen bookRequest database de var mı?
         if ( bookRepository.existsByIsbn(bookRequest.getIsbn())){
             throw new ConflictException(String.format(ErrorMessages.BOOK_ALREADY_EXISTS,bookRequest.getIsbn()));
         }

         //

    }
}
