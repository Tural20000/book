package com.abdullayevtural.book.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BookRequestDto {
	@NotBlank(message = "Ad bos ola bilmez!")
	private String name;

	@NotBlank(message = "Muellif bos ola bilmez!")
	private String author;

	@Min(value = 1, message = "Qiymet 1 manatdan az ola bilmez!")
	@Max(value = 1000, message = "Qiymet 1000 manatdan cox ola bilmez!")
	@NotNull(message = "Qiymet bos ola bilmez!")
	private Double price;

	@Min(value = 1, message = "Say 1 den az ola bilmez!")
	private Long quantity;

}
