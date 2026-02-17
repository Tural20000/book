package com.abdullayevtural.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.book.dto.BookRequestDto;
import com.abdullayevtural.book.dto.BookResponseDto;
import com.abdullayevtural.book.exception.MyValidationException;
import com.abdullayevtural.book.service.BookService;

import jakarta.validation.Valid;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/books")
public class BookController {
	@Autowired
	private BookService service;
	
	@GetMapping
	public List<BookResponseDto> getAllBooks(){
		log.info("Info: XML konfiqurasiyasi isleyir!");
		return service.getAllBooks();
		
	}
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public BookResponseDto createBook(@Valid @RequestBody BookRequestDto request,BindingResult br ) {
		if(br.hasErrors()) {
			throw new MyValidationException(br);
		}
		return service.createBook(request);
	}
	@DeleteMapping
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteBook(@PathVariable Long id) {
		service.deleteBookById(id);
	}
	@GetMapping("/{id}")
	public BookResponseDto getBookById(@PathVariable Long id) {
		log.info("Info: XML konfiqurasiyasi isleyir!");
		return service.getBookById(id);
	}
	@PutMapping("/{id}")
	public BookResponseDto updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDto request,BindingResult br) {
		if(br.hasErrors()) {
			throw new MyValidationException(br);
		}
		return service.updateBook(id, request);
		
	}

}
