package com.abdullayevtural.book.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BookResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String author;
	private Double price;
	private Long quantity;

}
