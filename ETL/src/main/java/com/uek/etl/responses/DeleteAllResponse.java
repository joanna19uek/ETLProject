package com.uek.etl.responses;

import lombok.Data;

/**
 * Klasa reprezentująca zwracaną odpowiedź na żądanie usunięcia wszystkich rekordów z bazy danych.<br>
 * Zawiera liczbę usuniętych produktów oraz opinii
 *
 */
@Data
public class DeleteAllResponse {
	private Integer deletedProductsNumber;
	private Integer deletedReviewsNumber;
}
