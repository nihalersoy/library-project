package com.tpe.payload.response.business;

import com.tpe.entity.business.Author;
import com.tpe.entity.business.Category;
import com.tpe.entity.business.Publisher;
import lombok.*;


import java.io.File;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

public class BookResponse {

    private String name;//
    private String isbn;//
    private int pageCount;//
    private int publishDate;//
    private File image;//
    private boolean loanable;//TODO
    private String shelfCode; //
    private boolean active;//TODO
    private boolean featured;//TODO
    private List<Author> authors; //
    private Publisher publisher;//
    private Category category;//

}
