package com.uek.etl.responses;

import lombok.Data;

/**
 * Klasa reprezentująca zwracaną odpowiedź na żądanie transformacji pobranych danych do odpowiedniego formatu.<br>
 * Zawiera liczbę produktów i opinii gotowych do załadowania do bazy danych
 */
@Data
public class TransformResponse {
	private Integer productRecords;
	private Integer reviewRecords;
}
