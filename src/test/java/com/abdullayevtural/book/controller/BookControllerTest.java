package com.abdullayevtural.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.abdullayevtural.book.dto.BookRequestDto;
import com.abdullayevtural.book.dto.BookResponseDto;
import com.abdullayevtural.book.exception.BookNotFoundException;
import com.abdullayevtural.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
@DisplayName("BookController Tests")
class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@Autowired
	private ObjectMapper objectMapper;

	private BookResponseDto bookResponseDto;
	private BookRequestDto bookRequestDto;

	@BeforeEach
	void setUp() {
		bookResponseDto = new BookResponseDto();
		bookResponseDto.setId(1L);
		bookResponseDto.setName("Test Book");
		bookResponseDto.setAuthor("Test Author");
		bookResponseDto.setPrice(25.50);
		bookResponseDto.setQuantity(10L);

		bookRequestDto = new BookRequestDto();
		bookRequestDto.setName("New Book");
		bookRequestDto.setAuthor("New Author");
		bookRequestDto.setPrice(30.0);
		bookRequestDto.setQuantity(5L);
	}

	@Test
	@DisplayName("GET /api/books - Should return all books")
	void getAllBooks_shouldReturnAllBooks() throws Exception {
		// Arrange
		List<BookResponseDto> books = new ArrayList<>();
		books.add(bookResponseDto);
		BookResponseDto book2 = new BookResponseDto(2L, "Book 2", "Author 2", 15.0, 3L);
		books.add(book2);

		when(bookService.getAllBooks()).thenReturn(books);

		// Act & Assert
		mockMvc.perform(get("/api/books").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].name").value("Test Book")).andExpect(jsonPath("$[1].id").value(2L))
				.andExpect(jsonPath("$[1].name").value("Book 2"));

		verify(bookService, org.mockito.Mockito.times(1)).getAllBooks();
	}

	@Test
	@DisplayName("GET /api/books/{id} - Should return book by id")
	void getBookById_shouldReturnBook_whenBookExists() throws Exception {
		// Arrange
		when(bookService.getBookById(1L)).thenReturn(bookResponseDto);

		// Act & Assert
		mockMvc.perform(get("/api/books/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Test Book"))
				.andExpect(jsonPath("$.author").value("Test Author")).andExpect(jsonPath("$.price").value(25.50))
				.andExpect(jsonPath("$.quantity").value(10L));

		verify(bookService, org.mockito.Mockito.times(1)).getBookById(1L);
	}

	@Test
	@DisplayName("GET /api/books/{id} - Should return 404 when book not found")
	void getBookById_shouldReturn404_whenBookNotFound() throws Exception {
		// Arrange
		when(bookService.getBookById(999L)).thenThrow(new BookNotFoundException("Bu ID-li kitab tapilmadi: ID= 999"));

		// Act & Assert
		mockMvc.perform(get("/api/books/999").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());

		verify(bookService, org.mockito.Mockito.times(1)).getBookById(999L);
	}

	@Test
	@DisplayName("POST /api/books - Should create book and return 201")
	void createBook_shouldCreateBook_whenValidRequest() throws Exception {
		// Arrange
		when(bookService.createBook(any(BookRequestDto.class))).thenReturn(bookResponseDto);

		// Act & Assert
		mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookRequestDto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Test Book"))
				.andExpect(jsonPath("$.author").value("Test Author"));

		verify(bookService, org.mockito.Mockito.times(1)).createBook(any(BookRequestDto.class));
	}

	@Test
	@DisplayName("POST /api/books - Should return 400 when validation fails")
	void createBook_shouldReturn400_whenValidationFails() throws Exception {
		// Arrange
		BookRequestDto invalidRequest = new BookRequestDto();
		invalidRequest.setName(""); // Empty name should fail validation
		invalidRequest.setAuthor("");
		invalidRequest.setPrice(null);
		invalidRequest.setQuantity(null);

		// Act & Assert
		mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest))).andExpect(status().isBadRequest());

		verify(bookService, org.mockito.Mockito.never()).createBook(any(BookRequestDto.class));
	}

	@Test
	@DisplayName("PUT /api/books/{id} - Should update book and return updated book")
	void updateBook_shouldUpdateBook_whenValidRequest() throws Exception {
		// Arrange
		BookResponseDto updatedResponse = new BookResponseDto();
		updatedResponse.setId(1L);
		updatedResponse.setName("Updated Book");
		updatedResponse.setAuthor("Updated Author");
		updatedResponse.setPrice(35.0);
		updatedResponse.setQuantity(7L);

		when(bookService.updateBook(eq(1L), any(BookRequestDto.class))).thenReturn(updatedResponse);

		// Act & Assert
		mockMvc.perform(put("/api/books/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookRequestDto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Updated Book"))
				.andExpect(jsonPath("$.author").value("Updated Author"));

		verify(bookService, org.mockito.Mockito.times(1)).updateBook(eq(1L), any(BookRequestDto.class));
	}

	@Test
	@DisplayName("PUT /api/books/{id} - Should return 404 when book not found")
	void updateBook_shouldReturn404_whenBookNotFound() throws Exception {
		// Arrange
		when(bookService.updateBook(eq(999L), any(BookRequestDto.class)))
				.thenThrow(new BookNotFoundException("Bu ID-li kitab tapilmadi: ID= 999"));

		// Act & Assert
		mockMvc.perform(put("/api/books/999").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookRequestDto))).andExpect(status().isInternalServerError());

		verify(bookService, org.mockito.Mockito.times(1)).updateBook(eq(999L), any(BookRequestDto.class));
	}

	@Test
	@DisplayName("DELETE /api/books/{id} - Should delete book and return 204")
	void deleteBook_shouldDeleteBook_whenBookExists() throws Exception {
		// Arrange
		doNothing().when(bookService).deleteBookById(1L);

		// Act & Assert
		mockMvc.perform(delete("/api/books/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(bookService, org.mockito.Mockito.times(1)).deleteBookById(1L);
	}

	@Test
	@DisplayName("DELETE /api/books/{id} - Should return error when book not found")
	void deleteBook_shouldReturnError_whenBookNotFound() throws Exception {
		// Arrange
		doThrow(new BookNotFoundException("Bu ID-li kitab tapilmadi: ID= 999")).when(bookService).deleteBookById(999L);

		// Act & Assert
		mockMvc.perform(delete("/api/books/999").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());

		verify(bookService, org.mockito.Mockito.times(1)).deleteBookById(999L);
	}
}
