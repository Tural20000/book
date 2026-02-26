package com.abdullayevtural.book.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.abdullayevtural.book.entity.BookEntity;

@DataJpaTest
@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.datasource.url=jdbc:h2:mem:testdb" })
@DisplayName("BookRepository Tests")
class BookRepositoryTest {

	@Autowired
	private BookRepository bookRepository;

	private BookEntity bookEntity;

	@BeforeEach
	void setUp() {
		bookEntity = new BookEntity();
		bookEntity.setName("Test Book");
		bookEntity.setAuthor("Test Author");
		bookEntity.setPrice(25.50);
		bookEntity.setQuantity(10L);
		bookEntity.setUserId(1L);
	}

	@Test
	@DisplayName("Should save book successfully")
	void save_shouldSaveBook_successfully() {
		// Act
		BookEntity savedBook = bookRepository.save(bookEntity);

		// Assert
		assertNotNull(savedBook);
		assertNotNull(savedBook.getId());
		assertEquals("Test Book", savedBook.getName());
		assertEquals("Test Author", savedBook.getAuthor());
		assertEquals(25.50, savedBook.getPrice());
		assertEquals(10L, savedBook.getQuantity());
	}

	@Test
	@DisplayName("Should find book by id")
	void findById_shouldFindBook_whenBookExists() {
		// Arrange
		BookEntity savedBook = bookRepository.save(bookEntity);

		// Act
		Optional<BookEntity> foundBook = bookRepository.findById(savedBook.getId());

		// Assert
		assertTrue(foundBook.isPresent());
		assertEquals(savedBook.getId(), foundBook.get().getId());
		assertEquals("Test Book", foundBook.get().getName());
	}

	@Test
	@DisplayName("Should return empty optional when book not found")
	void findById_shouldReturnEmpty_whenBookDoesNotExist() {
		// Act
		Optional<BookEntity> foundBook = bookRepository.findById(999L);

		// Assert
		assertFalse(foundBook.isPresent());
	}

	@Test
	@DisplayName("Should find all books by user id")
	void findByUserId_shouldFindUserBooks() {
		// Arrange
		Long userId = 1L;
		BookEntity book1 = new BookEntity(null, "Book 1", "Author 1", 10.0, 5L, userId);
		BookEntity book2 = new BookEntity(null, "Book 2", "Author 2", 20.0, 3L, userId);
		bookRepository.save(book1);
		bookRepository.save(book2);

		// Act
		List<BookEntity> userBooks = bookRepository.findByUserIdOrderByIdAsc(userId);

		// Assert
		assertNotNull(userBooks);
		assertEquals(2, userBooks.size());
		assertTrue(userBooks.stream().anyMatch(b -> "Book 1".equals(b.getName())));
		assertTrue(userBooks.stream().anyMatch(b -> "Book 2".equals(b.getName())));
	}

	@Test
	@DisplayName("Should find by id and user id")
	void findByIdAndUserId_shouldFindBook_whenOwnedByUser() {
		BookEntity saved = bookRepository.save(bookEntity);
		Optional<BookEntity> found = bookRepository.findByIdAndUserId(saved.getId(), 1L);
		assertTrue(found.isPresent());
		assertEquals("Test Book", found.get().getName());
	}

	@Test
	@DisplayName("Should return empty when book belongs to other user")
	void findByIdAndUserId_shouldReturnEmpty_whenOtherUser() {
		BookEntity saved = bookRepository.save(bookEntity);
		Optional<BookEntity> found = bookRepository.findByIdAndUserId(saved.getId(), 999L);
		assertFalse(found.isPresent());
	}

	@Test
	@DisplayName("Should update book successfully")
	void save_shouldUpdateBook_successfully() {
		// Arrange
		BookEntity savedBook = bookRepository.save(bookEntity);
		savedBook.setName("Updated Book");
		savedBook.setPrice(35.0);

		// Act
		BookEntity updatedBook = bookRepository.save(savedBook);

		// Assert
		assertNotNull(updatedBook);
		assertEquals(savedBook.getId(), updatedBook.getId());
		assertEquals("Updated Book", updatedBook.getName());
		assertEquals(35.0, updatedBook.getPrice());
	}

	@Test
	@DisplayName("Should delete book by id")
	void deleteById_shouldDeleteBook_successfully() {
		// Arrange
		BookEntity savedBook = bookRepository.save(bookEntity);
		Long bookId = savedBook.getId();

		// Act
		bookRepository.deleteById(bookId);

		// Assert
		Optional<BookEntity> deletedBook = bookRepository.findById(bookId);
		assertFalse(deletedBook.isPresent());
	}

	@Test
	@DisplayName("Should check if book exists by id")
	void existsById_shouldReturnTrue_whenBookExists() {
		// Arrange
		BookEntity savedBook = bookRepository.save(bookEntity);

		// Act
		boolean exists = bookRepository.existsById(savedBook.getId());

		// Assert
		assertTrue(exists);
	}

	@Test
	@DisplayName("Should return false when book does not exist")
	void existsById_shouldReturnFalse_whenBookDoesNotExist() {
		// Act
		boolean exists = bookRepository.existsById(999L);

		// Assert
		assertFalse(exists);
	}

	@Test
	@DisplayName("Should count all books")
	void count_shouldReturnCorrectCount() {
		bookRepository.save(new BookEntity(null, "Book 1", "Author 1", 10.0, 5L, 1L));
		bookRepository.save(new BookEntity(null, "Book 2", "Author 2", 20.0, 3L, 1L));
		long count = bookRepository.count();
		assertTrue(count >= 2);
	}

	@Test
	@DisplayName("Should delete all books")
	void deleteAll_shouldDeleteAllBooks() {
		bookRepository.save(new BookEntity(null, "Book 1", "Author 1", 10.0, 5L, 1L));
		bookRepository.save(new BookEntity(null, "Book 2", "Author 2", 20.0, 3L, 1L));
		bookRepository.deleteAll();
		assertEquals(0, bookRepository.count());
	}
}
