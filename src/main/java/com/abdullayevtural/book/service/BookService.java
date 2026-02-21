package com.abdullayevtural.book.service;

import java.awt.desktop.OpenFilesEvent;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.abdullayevtural.book.dto.BookRequestDto;
import com.abdullayevtural.book.dto.BookResponseDto;
import com.abdullayevtural.book.entity.BookEntity;
import com.abdullayevtural.book.exception.BookNotFoundException;
import com.abdullayevtural.book.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookService {
	@Autowired
	private BookRepository repository;

	public List<BookResponseDto> getAllBooks() {
		List<BookEntity> books = repository.findAll();
		List<BookResponseDto> dtos = new ArrayList<BookResponseDto>();

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
		BookEntity book = new BookEntity();
		book.setName(request.getName());
		book.setAuthor(request.getAuthor());
		book.setPrice(request.getPrice());
		book.setQuantity(request.getQuantity());
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
		if (!repository.existsById(id)) {
			throw new BookNotFoundException("Bu ID-li kitab tapilmadi: ID= " + id);
		}
		repository.deleteById(id);
	}

	@Cacheable(value = "books", key = "#id")
	public BookResponseDto getBookById(Long id) {
		log.info("DB-den kitab getirilir: ID{}", id);
		BookEntity book = repository.findById(id)
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
		BookEntity book = repository.findById(id)
				.orElseThrow(() -> new BookNotFoundException("Bu ID-li kitab tapilmadi: ID + " + id));
		book.setName(request.getName());
		book.setAuthor(request.getAuthor());
		book.setPrice(request.getPrice());
		book.setQuantity(request.getQuantity());

		BookEntity updateBook = repository.save(book);
		BookResponseDto dto = new BookResponseDto();

		dto.setId(updateBook.getId());
		dto.setAuthor(updateBook.getAuthor());
		dto.setName(updateBook.getName());
		dto.setPrice(updateBook.getPrice());
		dto.setQuantity(updateBook.getQuantity());

		return dto;
	}
}
