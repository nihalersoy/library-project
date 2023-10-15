package com.tpe.controller.business;

import com.tpe.payload.request.business.BookRequest;
import com.tpe.payload.request.business.BookRequestForUpdate;
import com.tpe.payload.response.business.BookResponse;
import com.tpe.payload.response.business.ResponseMessage;
import com.tpe.service.business.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

//    Not: getBooksWithPage()
    @GetMapping //FRD herkes girebilsin demiş //TODO WhiteList ekle
    public Page<BookResponse> getBooksWithPage(
            @RequestParam(value = "q",required = false,defaultValue = "null") String q,
            @RequestParam(value = "cat",required = false,defaultValue = "null") String category,
            @RequestParam(value = "author",required = false,defaultValue = "null") String author,
            @RequestParam(value = "publisher",required = false, defaultValue = "null") String publisher,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam(value = "sort",defaultValue = "name") String sort,
            @RequestParam(value = "type",defaultValue = "asc") String type,
            HttpServletRequest servletRequest
            ){

        return bookService.getBooksWithPage(servletRequest,q,category,author,publisher,page,size,sort,type);

    }

    //Not: getBookById()
    @GetMapping("/{id}") //TODO WhiteList ekle
    public ResponseMessage<BookResponse> getBookById(@PathVariable Long id){

        return bookService.getBookById(id);
    }

    //Not: saveBook()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/save")
    public ResponseMessage<BookResponse> saveBook(@RequestBody @Valid BookRequest bookRequest){

        return bookService.saveBook(bookRequest);
    }

    //Not: updateBookById()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/updateBook/{id}")
    public ResponseMessage<BookResponse> updateBook(
            @RequestBody @Valid BookRequestForUpdate bookRequestForUpdate,
            @PathVariable Long id){

        return bookService.updateBook(bookRequestForUpdate,id);
    }

    //NOT: deleteBookById()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage<String> deleteBook (@PathVariable Long id){

        return bookService.deleteBookById(id);
    }








}
