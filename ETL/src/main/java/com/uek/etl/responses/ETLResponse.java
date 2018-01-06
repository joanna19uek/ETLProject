package com.uek.etl.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Klasa reprezentująca zwracaną odpowiedź na żądanie przeprowadzenia procesu ETL.<br>
 * Zawiera:
 * <ul>
 * <li>nazwę produktu, liczbę stron z opiniami, liczbę opinii,</li>
 * <li>liczbę produktów i opinii gotowych do załadowania do bazy danych,</li>
 * <li>liczbę nowych rekordów dodanych do tabeli produktów, opinii, wad i zalet</li>
 * </ul>
 *
 */
@Data
@AllArgsConstructor
public class ETLResponse {
	private String product;
	private Integer reviewsPagesNumber;
	private Integer reviewsNumber;
	
	private Integer productRecords;
	private Integer reviewRecords;
	
	private Integer newProductRecordsNumber;
	private Integer newReviewRecordsNumber;
	private Integer newAdvantagesRecordsNumber;
	private Integer newDisadvantagesRecordsNumber;
}
