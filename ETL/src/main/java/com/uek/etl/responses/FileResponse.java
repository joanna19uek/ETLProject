package com.uek.etl.responses;

import lombok.Data;

/**
 * Klasa reprezentująca zwracaną odpowiedź na żądanie wygenerowania pliku csv lub txt
 * z opiniami dla wszystkich lub wybranego produktu.<br>
 * Zawiera nazwę pliku oraz jego zawartość
 *
 */
@Data
public class FileResponse {
	private String fileContent;
	private String fileName;
}
