package com.abdullayevtural.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abdullayevtural.book.dto.BookRequestDto;
import com.abdullayevtural.book.dto.BookResponseDto;
import com.abdullayevtural.book.entity.BookEntity;
import com.abdullayevtural.book.exception.BookNotFoundException;
import com.abdullayevtural.book.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceTest {

	@Mock
	private BookRepository repository;

	@InjectMocks
	private BookService bookService;

	private BookEntity bookEntity;
	private BookRequestDto bookRequestDto;

	@BeforeEach
	void setUp() {
		bookEntity = new BookEntity();
		bookEntity.setId(1L);
		bookEntity.setName("Test Book");
		bookEntity.setAuthor("Test Author");
		bookEntity.setPrice(25.50);
		bookEntity.setQuantity(10L);

		bookRequestDto = new BookRequestDto();
		bookRequestDto.setName("New Book");
		bookRequestDto.setAuthor("New Author");
		bookRequestDto.setPrice(30.0);
		bookRequestDto.setQuantity(5L);
	}

	@Test
	@DisplayName("Should return all books when getAllBooks is called")
	void getAllBooks_shouldReturnAllBooks_whenBooksExist() {
		// Arrange
		List<BookEntity> books = new ArrayList<>();
		books.add(bookEntity);
		BookEntity book2 = new BookEntity(2L, "Book 2", "Author 2", 15.0, 3L);
		books.add(book2);

		when(repository.findAll()).thenReturn(books);

		// Act
		List<BookResponseDto> result = bookService.getAllBooks();

		// Assert
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("Test Book", result.get(0).getName());
		assertEquals("Book 2", result.get(1).getName());
		verify(repository, times(1)).findAll();
	}

	@Test
	@DisplayName("Should return empty list when no books exist")
	void getAllBooks_shouldReturnEmptyList_whenNoBooksExist() {
		// Arrange
		when(repository.findAll()).thenReturn(new ArrayList<>());

		// Act
		List<BookResponseDto> result = bookService.getAllBooks();

		// Assert
		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(repository, times(1)).findAll();
	}

	@Test
	@DisplayName("Should create and return book when createBook is called")
	void createBook_shouldCreateAndReturnBook_whenValidRequest() {
		// Arrange
		BookEntity savedBook = new BookEntity();
		savedBook.setId(1L);
		savedBook.setName(bookRequestDto.getName());
		savedBook.setAuthor(bookRequestDto.getAuthor());
		savedBook.setPrice(bookRequestDto.getPrice());
		savedBook.setQuantity(bookRequestDto.getQuantity());

		when(repository.save(any(BookEntity.class))).thenReturn(savedBook);

		// Act
		BookResponseDto result = bookService.createBook(bookRequestDto);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("New Book", result.getName());
		assertEquals("New Author", result.getAuthor());
		assertEquals(30.0, result.getPrice());
		assertEquals(5L, result.getQuantity());
		verify(repository, times(1)).save(any(BookEntity.class));
	}

	@Test
	@DisplayName("Should return book when getBookById is called with existing id")
	void getBookById_shouldReturnBook_whenBookExists() {
		// Arrange
		when(repository.findById(1L)).thenReturn(Optional.of(bookEntity));

		// Act
		BookResponseDto result = bookService.getBookById(1L);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Test Book", result.getName());
		assertEquals("Test Author", result.getAuthor());
		assertEquals(25.50, result.getPrice());
		assertEquals(10L, result.getQuantity());
		verify(repository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("Should throw BookNotFoundException when getBookById is called with non-existing id")
	void getBookById_shouldThrowException_whenBookDoesNotExist() {
		// Arrange
		when(repository.findById(999L)).thenReturn(Optional.empty());

		// Act & Assert
		BookNotFoundException exception = assertThrows(BookNotFoundException.class,
				() -> bookService.getBookById(999L));
		assertTrue(exception.getMessage().contains("Bu ID-li kitab tapilmadi"));
		verify(repository, times(1)).findById(999L);
	}

	@Test
	@DisplayName("Should update and return book when updateBook is called with existing id")
	void updateBook_shouldUpdateAndReturnBook_whenBookExists() {
		// Arrange
		BookEntity updatedBook = new BookEntity();
		updatedBook.setId(1L);
		updatedBook.setName(bookRequestDto.getName());
		updatedBook.setAuthor(bookRequestDto.getAuthor());
		updatedBook.setPrice(bookRequestDto.getPrice());
		updatedBook.setQuantity(bookRequestDto.getQuantity());

		when(repository.findById(1L)).thenReturn(Optional.of(bookEntity));
		when(repository.save(any(BookEntity.class))).thenReturn(updatedBook);

		// Act
		BookResponseDto result = bookService.updateBook(1L, bookRequestDto);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("New Book", result.getName());
		assertEquals("New Author", result.getAuthor());
		assertEquals(30.0, result.getPrice());
		assertEquals(5L, result.getQuantity());
		verify(repository, times(1)).findById(1L);
		verify(repository, times(1)).save(any(BookEntity.class));
	}

	@Test
	@DisplayName("Should throw BookNotFoundException when updateBook is called with non-existing id")
	void updateBook_shouldThrowException_whenBookDoesNotExist() {
		// Arrange
		when(repository.findById(999L)).thenReturn(Optional.empty());

		// Act & Assert
		BookNotFoundException exception = assertThrows(BookNotFoundException.class,
				() -> bookService.updateBook(999L, bookRequestDto));
		assertTrue(exception.getMessage().contains("Bu ID-li kitab tapilmadi"));
		verify(repository, times(1)).findById(999L);
		verify(repository, never()).save(any(BookEntity.class));
	}

	@Test
	@DisplayName("Should delete book when deleteBookById is called with existing id")
	void deleteBookById_shouldDeleteBook_whenBookExists() {
		// Arrange
		when(repository.existsById(1L)).thenReturn(true);

		// Act
		bookService.deleteBookById(1L);

		// Assert
		verify(repository, times(1)).existsById(1L);
		verify(repository, times(1)).deleteById(1L);
	}

	@Test
	@DisplayName("Should throw BookNotFoundException when deleteBookById is called with non-existing id")
	void deleteBookById_shouldThrowException_whenBookDoesNotExist() {
		// Arrange
		when(repository.existsById(999L)).thenReturn(false);

		// Act & Assert
		BookNotFoundException exception = assertThrows(BookNotFoundException.class,
				() -> bookService.deleteBookById(999L));
		assertTrue(exception.getMessage().contains("Bu ID-li kitab tapilmadi"));
		verify(repository, times(1)).existsById(999L);
		verify(repository, never()).deleteById(any());
	}
}