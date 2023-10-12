package com.tpe.service.business;

import com.tpe.entity.business.Author;
import com.tpe.entity.business.Book;
import com.tpe.entity.business.Category;
import com.tpe.entity.business.Publisher;
import com.tpe.entity.user.User;
import com.tpe.exception.BadRequestException;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.mappers.BookMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.request.business.BookRequest;
import com.tpe.payload.request.business.BookRequestForUpdate;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;

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

         //POJO donusum icin author-publisher-category getirilecek
         List<Author> authors = authorService.getAuthorById(bookRequest.getAuthorIdList());
         Publisher publisher =publisherService.getPublisherById(bookRequest.getPublisherId());
         Category category = categoryService.getCategoryById(bookRequest.getCategoryId());

        //DTO-->POJO
        Book book = bookMapper.mapBookRequestToBook(bookRequest);
        //TODO pdf sayfa 15 zeynep hocaya sor

        //eksik fieldları setliyoruz
        book.setLoanable(true);
        book.setActive(true);
        book.setCreateDate(LocalDateTime.now());//TODO zone ekle
        book.setBuiltIn(false);
        book.setAuthors(authors);
        book.setPublisher(publisher);
        book.setCategory(category);

        return ResponseMessage.<BookResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .object(bookMapper.mapBookToBookResponse(book))
                .message(SuccessMessages.BOOK_CREATED)
                .build();
    }


    public ResponseMessage<BookResponse> updateBook(BookRequestForUpdate bookRequestForUpdate, Long id) {

        //update etmek istediği kitap var mı?
        Book existingBook = bookRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.BOOK_NOT_FOUND,id)));

        //unique field isbn kontrol et
        if ( bookRepository.existsByIsbn(bookRequestForUpdate.getIsbn())){
            throw new ConflictException(String.format(ErrorMessages.BOOK_ALREADY_EXISTS,bookRequestForUpdate.getIsbn()));
        }

        //POJO donusum icin author-publisher-category getirilecek
        List<Author> authors = authorService.getAuthorById(bookRequestForUpdate.getAuthorIdList());
        Publisher publisher =publisherService.getPublisherById(bookRequestForUpdate.getPublisherId());
        Category category = categoryService.getCategoryById(bookRequestForUpdate.getCategoryId());

        //DTO-->POJO donusum
        Book updatedBook = bookMapper.mapBookRequestToBook(bookRequestForUpdate);

        //eksik filedları setliyoruz
        updatedBook.setLoanable(true);
        updatedBook.setActive(bookRequestForUpdate.getActive());
        updatedBook.setCreateDate(LocalDateTime.now());//TODO zone ekle
        updatedBook.setBuiltIn(false);
        updatedBook.setAuthors(authors);
        updatedBook.setPublisher(publisher);
        updatedBook.setCategory(category);

        //id field setliyoruz
        updatedBook.setId(existingBook.getId());

        return ResponseMessage.<BookResponse>builder()
                .message(SuccessMessages.BOOK_UPDATED)
                .object(bookMapper.mapBookToBookResponse(updatedBook))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<String> deleteBookById(Long id) {

        //book db de var mı?
        doesExistsById(id);

        bookRepository.deleteById(id);

        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .message(SuccessMessages.BOOK_DELETED)
                .build();
    }


    private boolean doesExistsById(Long id){

        if(!bookRepository.existsById(id)){
            throw new ResourceNotFoundException(String.format(ErrorMessages.BOOK_NOT_FOUND,id));
        }

        return true;
    }

}
