package com.tpe.service.business;

import com.tpe.entity.business.Author;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.repository.business.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;


    public List<Author> getAuthorById(Set<Long> authorIdList) {

        List<Author> authorList =authorRepository.findAllById(authorIdList);
        if (authorList.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.AUTHOR_NOT_FOUND);
        }
        return authorList;
    }
}
