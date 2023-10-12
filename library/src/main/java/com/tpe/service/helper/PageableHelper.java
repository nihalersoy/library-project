package com.tpe.service.helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter

public class PageableHelper {

    public Pageable getPageable(int page, int size, String sort,String type){

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());
        if (type.equalsIgnoreCase("desc")){
            pageable =PageRequest.of(page,size,Sort.by(sort).descending());
        }

        return pageable;
    }

}
