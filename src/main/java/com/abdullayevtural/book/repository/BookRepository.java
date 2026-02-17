package com.abdullayevtural.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdullayevtural.book.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

}
