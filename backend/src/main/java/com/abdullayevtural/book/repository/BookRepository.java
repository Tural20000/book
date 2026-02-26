package com.abdullayevtural.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdullayevtural.book.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

	List<BookEntity> findByUserIdOrderByIdAsc(Long userId);

	Optional<BookEntity> findByIdAndUserId(Long id, Long userId);
}
