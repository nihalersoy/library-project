package com.tpe.payload.mappers;

import com.tpe.entity.business.Book;
import com.tpe.entity.business.Publisher;
import com.tpe.payload.request.business.PublisherRequest;
import com.tpe.payload.response.business.PublisherResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublisherMapper {

    public PublisherResponse mapPublisherToPublisherResponse (Publisher publisher){

        return PublisherResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .books(publisher.getBooks())
                .build();
    }

    public Publisher mapPublisherRequestToPublisher (PublisherRequest publisherRequest, List<Book> bookList){

        return Publisher.builder()
                .name(publisherRequest.getName())
                .books(bookList)
                .build();
    }


}
