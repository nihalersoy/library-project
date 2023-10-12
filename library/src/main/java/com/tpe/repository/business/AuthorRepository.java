package com.tpe.repository.business;

import com.tpe.entity.business.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author,Long> {

}
