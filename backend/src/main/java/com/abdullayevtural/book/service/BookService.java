package com.abdullayevtural.book.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.abdullayevtural.book.dto.BookRequestDto;
import com.abdullayevtural.book.dto.BookResponseDto;
import com.abdullayevtural.book.entity.BookEntity;
import com.abdullayevtural.book.entity.UserEntity;
import com.abdullayevtural.book.exception.BookNotFoundException;
import com.abdullayevtural.book.repository.BookRepository;
import com.abdullayevtural.book.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookService {
	@Autowired
	private BookRepository repository;
	@Autowired
	private UserRepository userRepository;

	private Long getCurrentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getName() == null) {
			throw new IllegalStateException("Daxil olmamış istifadəçi");
		}
		UserEntity user = userRepository.findByUsername(auth.getName())
				.orElseThrow(() -> new BookNotFoundException("Istifadəçi tapılmadı"));
		return user.getId();
	}

	public List<BookResponseDto> getAllBooks() {
		Long userId = getCurrentUserId();
		List<BookEntity> books = repository.findByUserIdOrderByIdAsc(userId);
		List<BookResponseDto> dtos = new ArrayList<>();

		for (BookEntity s : books) {
			BookResponseDto dto = new BookResponseDto();
			dto.setId(s.getId());
			dto.setName(s.getName());
			dto.setAuthor(s.getAuthor());
			dto.setPrice(s.getPrice());
			dto.setQuantity(s.getQuantity());
			dtos.add(dto);
		}
		return dtos;
	}

	public BookResponseDto createBook(BookRequestDto request) {
		Long userId = getCurrentUserId();
		BookEntity book = new BookEntity();
		book.setName(request.getName());
		book.setAuthor(request.getAuthor());
		book.setPrice(request.getPrice());
		book.setQuantity(request.getQuantity());
		book.setUserId(userId);
		BookEntity savedBook = repository.save(book);

		BookResponseDto response = new BookResponseDto();
		response.setId(savedBook.getId());
		response.setName(savedBook.getName());
		response.setAuthor(savedBook.getAuthor());
		response.setPrice(savedBook.getPrice());
		response.setQuantity(savedBook.getQuantity());
		return response;
	}

	public void deleteBookById(Long id) {
		Long userId = getCurrentUserId();
		BookEntity book = repository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new BookNotFoundException("Bu ID-li kitab tapilmadi: ID= " + id));
		repository.delete(book);
	}

	public BookResponseDto getBookById(Long id) {
		Long userId = getCurrentUserId();
		BookEntity book = repository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new BookNotFoundException("Bu ID-li kitab tapilmadi: ID= " + id));
		BookResponseDto dto = new BookResponseDto();
		dto.setId(book.getId());
		dto.setName(book.getName());
		dto.setAuthor(book.getAuthor());
		dto.setPrice(book.getPrice());
		dto.setQuantity(book.getQuantity());
		return dto;
	}

	public BookResponseDto updateBook(Long id, BookRequestDto request) {
		Long userId = getCurrentUserId();
		BookEntity book = repository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new BookNotFoundException("Bu ID-li kitab tapilmadi: ID= " + id));
		book.setName(request.getName());
		book.setAuthor(request.getAuthor());
		book.setPrice(request.getPrice());
		book.setQuantity(request.getQuantity());
		BookEntity updated = repository.save(book);
		BookResponseDto dto = new BookResponseDto();
		dto.setId(updated.getId());
		dto.setName(updated.getName());
		dto.setAuthor(updated.getAuthor());
		dto.setPrice(updated.getPrice());
		dto.setQuantity(updated.getQuantity());
		return dto;
	}
}
