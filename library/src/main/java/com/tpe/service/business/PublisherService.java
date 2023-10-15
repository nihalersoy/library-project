package com.tpe.service.business;


import com.tpe.entity.business.Book;
import com.tpe.entity.business.Publisher;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.mappers.PublisherMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.request.business.PublisherRequest;
import com.tpe.payload.response.business.PublisherResponse;
import com.tpe.payload.response.business.ResponseMessage;
import com.tpe.repository.business.PublisherRepository;
import com.tpe.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.logging.ErrorManager;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final PageableHelper pageableHelper;
    private final PublisherMapper publisherMapper;
    private final BookService bookService;

    public Publisher getPublisherById(Long publisherId) {
        return publisherRepository.findById(publisherId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.PUBLISHER_NOT_FOUND,publisherId)));

    }


    public ResponseMessage<Page<PublisherResponse>> getAllPublishers
            (int page, int size, String sort, String type) {

        Pageable pageable = pageableHelper.getPageable(page,size,sort,type);

        Page<PublisherResponse> publishers = publisherRepository.findAll(pageable).map(publisherMapper::mapPublisherToPublisherResponse);
        return ResponseMessage.<Page<PublisherResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .object(publishers)
                .build();

    }


    public ResponseMessage<PublisherResponse> findPublisherById(Long id) {

        Publisher publisher = getPublisherById(id);
        return ResponseMessage.<PublisherResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(publisherMapper.mapPublisherToPublisherResponse(publisher))
                .build();
    }


    public ResponseMessage<PublisherResponse> savePublisher(PublisherRequest publisherRequest) {

        //bu publisher sistemde zaten var mı kontrol edicez
        String publisherName = publisherRequest.getName();
        existByName(publisherName);

        //requestte gelen id listi booklara çeviriyoruz
        List<Book> bookList = publisherRequest.getBookIdList().stream().map(bookService::isBookExistById).collect(Collectors.toList());

        //DTO-->POJO
        Publisher publisher = publisherMapper.mapPublisherRequestToPublisher(publisherRequest,bookList);
        publisher.setBuiltIn(Boolean.FALSE);

        Publisher savedPublisher = publisherRepository.save(publisher);

        return ResponseMessage.<PublisherResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .object(publisherMapper.mapPublisherToPublisherResponse(savedPublisher))
                .build();
    }

    private void existByName(String name){

        publisherRepository.findByName(name).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.PUBLISHER_NOT_FOUND_WITH_NAME,name)));
    }


}
