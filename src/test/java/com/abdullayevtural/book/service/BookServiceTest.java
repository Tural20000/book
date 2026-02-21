package com.abdullayevtural.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abdullayevtural.book.entity.BookEntity;
import com.abdullayevtural.book.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

	@Mock
	private BookRepository repository;

	@InjectMocks
	private BookService bookService;

	@Test
	void getBookById_shouldReturnBook_whenBookExists() {

		BookEntity book = new BookEntity();
		book.setId(1L);
		book.setName("Test Book");
		book.setAuthor("Test Author");
		book.setPrice(10.0);
		book.setQuantity((long) 5);

		when(repository.findById(1L)).thenReturn(Optional.of(book));

		var result = bookService.getBookById(1L);

		assertEquals("Test Book", result.getName());
	}

}