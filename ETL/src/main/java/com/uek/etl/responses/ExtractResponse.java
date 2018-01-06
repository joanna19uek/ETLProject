package com.uek.etl.responses;

import lombok.Data;

/**
 * Klasa reprezentująca zwracaną odpowiedź na żadanie pobrania danych z serwisu ceneo.pl (etap extract).<br>
 * Zawiera nazwę produktu, liczbę stron z opiniami i liczbę samych opinii
 *
 */
@Data
public class ExtractResponse {
	private String product;
	private Integer reviewsPagesNumber;
	private Integer reviewsNumber;
}
