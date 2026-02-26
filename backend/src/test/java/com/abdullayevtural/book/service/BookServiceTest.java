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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.abdullayevtural.book.dto.BookRequestDto;
import com.abdullayevtural.book.dto.BookResponseDto;
import com.abdullayevtural.book.entity.BookEntity;
import com.abdullayevtural.book.entity.UserEntity;
import com.abdullayevtural.book.exception.BookNotFoundException;
import com.abdullayevtural.book.repository.BookRepository;
import com.abdullayevtural.book.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceTest {

	private static final Long CURRENT_USER_ID = 1L;
	private static final String CURRENT_USERNAME = "testuser";

	@Mock
	private BookRepository repository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private BookService bookService;

	private BookEntity bookEntity;
	private BookRequestDto bookRequestDto;
	private UserEntity currentUser;

	@BeforeEach
	void setUp() {
		currentUser = new UserEntity();
		currentUser.setId(CURRENT_USER_ID);
		currentUser.setUsername(CURRENT_USERNAME);

		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.when(auth.getName()).thenReturn(CURRENT_USERNAME);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findByUsername(CURRENT_USERNAME)).thenReturn(Optional.of(currentUser));

		bookEntity = new BookEntity();
		bookEntity.setId(1L);
		bookEntity.setName("Test Book");
		bookEntity.setAuthor("Test Author");
		bookEntity.setPrice(25.50);
		bookEntity.setQuantity(10L);
		bookEntity.setUserId(CURRENT_USER_ID);

		bookRequestDto = new BookRequestDto();
		bookRequestDto.setName("New Book");
		bookRequestDto.setAuthor("New Author");
		bookRequestDto.setPrice(30.0);
		bookRequestDto.setQuantity(5L);
	}

	@Test
	@DisplayName("Should return all books when getAllBooks is called")
	void getAllBooks_shouldReturnAllBooks_whenBooksExist() {
		List<BookEntity> books = new ArrayList<>();
		books.add(bookEntity);
		BookEntity book2 = new BookEntity();
		book2.setId(2L);
		book2.setName("Book 2");
		book2.setAuthor("Author 2");
		book2.setPrice(15.0);
		book2.setQuantity(3L);
		book2.setUserId(CURRENT_USER_ID);
		books.add(book2);

		when(repository.findByUserIdOrderByIdAsc(CURRENT_USER_ID)).thenReturn(books);

		List<BookResponseDto> result = bookService.getAllBooks();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("Test Book", result.get(0).getName());
		assertEquals("Book 2", result.get(1).getName());
		verify(repository, times(1)).findByUserIdOrderByIdAsc(CURRENT_USER_ID);
	}

	@Test
	@DisplayName("Should return empty list when no books exist")
	void getAllBooks_shouldReturnEmptyList_whenNoBooksExist() {
		when(repository.findByUserIdOrderByIdAsc(CURRENT_USER_ID)).thenReturn(new ArrayList<>());

		List<BookResponseDto> result = bookService.getAllBooks();

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(repository, times(1)).findByUserIdOrderByIdAsc(CURRENT_USER_ID);
	}

	@Test
	@DisplayName("Should create and return book when createBook is called")
	void createBook_shouldCreateAndReturnBook_whenValidRequest() {
		BookEntity savedBook = new BookEntity();
		savedBook.setId(1L);
		savedBook.setName(bookRequestDto.getName());
		savedBook.setAuthor(bookRequestDto.getAuthor());
		savedBook.setPrice(bookRequestDto.getPrice());
		savedBook.setQuantity(bookRequestDto.getQuantity());
		savedBook.setUserId(CURRENT_USER_ID);

		when(repository.save(any(BookEntity.class))).thenReturn(savedBook);

		BookResponseDto result = bookService.createBook(bookRequestDto);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("New Book", result.getName());
		verify(repository, times(1)).save(any(BookEntity.class));
	}

	@Test
	@DisplayName("Should return book when getBookById is called with existing id")
	void getBookById_shouldReturnBook_whenBookExists() {
		when(repository.findByIdAndUserId(1L, CURRENT_USER_ID)).thenReturn(Optional.of(bookEntity));

		BookResponseDto result = bookService.getBookById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Test Book", result.getName());
		verify(repository, times(1)).findByIdAndUserId(1L, CURRENT_USER_ID);
	}

	@Test
	@DisplayName("Should throw BookNotFoundException when getBookById is called with non-existing id")
	void getBookById_shouldThrowException_whenBookDoesNotExist() {
		when(repository.findByIdAndUserId(999L, CURRENT_USER_ID)).thenReturn(Optional.empty());

		BookNotFoundException exception = assertThrows(BookNotFoundException.class,
				() -> bookService.getBookById(999L));
		assertTrue(exception.getMessage().contains("Bu ID-li kitab tapilmadi"));
		verify(repository, times(1)).findByIdAndUserId(999L, CURRENT_USER_ID);
	}

	@Test
	@DisplayName("Should update and return book when updateBook is called with existing id")
	void updateBook_shouldUpdateAndReturnBook_whenBookExists() {
		BookEntity updatedBook = new BookEntity();
		updatedBook.setId(1L);
		updatedBook.setName(bookRequestDto.getName());
		updatedBook.setAuthor(bookRequestDto.getAuthor());
		updatedBook.setPrice(bookRequestDto.getPrice());
		updatedBook.setQuantity(bookRequestDto.getQuantity());
		updatedBook.setUserId(CURRENT_USER_ID);

		when(repository.findByIdAndUserId(1L, CURRENT_USER_ID)).thenReturn(Optional.of(bookEntity));
		when(repository.save(any(BookEntity.class))).thenReturn(updatedBook);

		BookResponseDto result = bookService.updateBook(1L, bookRequestDto);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("New Book", result.getName());
		verify(repository, times(1)).findByIdAndUserId(1L, CURRENT_USER_ID);
		verify(repository, times(1)).save(any(BookEntity.class));
	}

	@Test
	@DisplayName("Should throw BookNotFoundException when updateBook is called with non-existing id")
	void updateBook_shouldThrowException_whenBookDoesNotExist() {
		when(repository.findByIdAndUserId(999L, CURRENT_USER_ID)).thenReturn(Optional.empty());

		BookNotFoundException exception = assertThrows(BookNotFoundException.class,
				() -> bookService.updateBook(999L, bookRequestDto));
		assertTrue(exception.getMessage().contains("Bu ID-li kitab tapilmadi"));
		verify(repository, never()).save(any(BookEntity.class));
	}

	@Test
	@DisplayName("Should delete book when deleteBookById is called with existing id")
	void deleteBookById_shouldDeleteBook_whenBookExists() {
		when(repository.findByIdAndUserId(1L, CURRENT_USER_ID)).thenReturn(Optional.of(bookEntity));
		Mockito.doNothing().when(repository).delete(bookEntity);

		bookService.deleteBookById(1L);

		verify(repository, times(1)).findByIdAndUserId(1L, CURRENT_USER_ID);
		verify(repository, times(1)).delete(bookEntity);
	}

	@Test
	@DisplayName("Should throw BookNotFoundException when deleteBookById is called with non-existing id")
	void deleteBookById_shouldThrowException_whenBookDoesNotExist() {
		when(repository.findByIdAndUserId(999L, CURRENT_USER_ID)).thenReturn(Optional.empty());

		BookNotFoundException exception = assertThrows(BookNotFoundException.class,
				() -> bookService.deleteBookById(999L));
		assertTrue(exception.getMessage().contains("Bu ID-li kitab tapilmadi"));
		verify(repository, never()).delete(any(BookEntity.class));
	}
}