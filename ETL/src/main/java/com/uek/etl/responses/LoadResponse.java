package com.uek.etl.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Klasa reprezentująca zwracaną odpowiedź na żądanie załadowania przetworzonych danych do bazy.<br>
 * Zawiera liczbę nowych rekordów dodanych do tabeli produktów, opinii, wad i zalet
 *
 */
@Data
@AllArgsConstructor
public class LoadResponse {
	private Integer newProductRecordsNumber;
	private Integer newReviewRecordsNumber;
	private Integer newAdvantagesRecordsNumber;
	private Integer newDisadvantagesRecordsNumber;
}
