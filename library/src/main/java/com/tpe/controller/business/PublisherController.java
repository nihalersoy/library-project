package com.tpe.controller.business;

import com.tpe.payload.request.business.PublisherRequest;
import com.tpe.payload.response.business.PublisherResponse;
import com.tpe.payload.response.business.ResponseMessage;
import com.tpe.service.business.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping("/getPublishers")//TODO white liste ekle
    public ResponseMessage<Page<PublisherResponse>> getPublisher (
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam(value = "sort",defaultValue = "name") String sort,
            @RequestParam(value = "type",defaultValue = "asc") String type){

        return publisherService.getAllPublishers(page,size,sort,type);
    }

    @GetMapping("/getById/{id}")//TODO White liste ekle
    public ResponseMessage<PublisherResponse> getPublisherById(@PathVariable Long id){

        return publisherService.findPublisherById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/save")
    public ResponseMessage<PublisherResponse> savePublisher (@RequestBody @Valid PublisherRequest publisherRequest){

        return publisherService.savePublisher(publisherRequest);
    }

}
