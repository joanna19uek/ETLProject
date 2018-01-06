package com.uek.etl.responses;

import lombok.Data;

/**
 * Klasa reprezentująca zwracaną odpowiedź na żądanie usunięcia wszystkich opinii z bazy danych.<br>
 * Zawiera liczbę usuniętych opinii
 *
 */
@Data
public class DeleteReviewsResponse {
	private Integer deletedReviewsNumber;
}
