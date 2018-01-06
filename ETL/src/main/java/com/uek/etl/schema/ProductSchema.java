package com.uek.etl.schema;

import lombok.Data;

/**
 * Klasa reprezentująca model danych produktu.<br>
 * Zawiera:
 * <ul>
 * <li>kod produktu,</li>
 * <li>kategorię,</li>
 * <li>markę,</li>
 * <li>model</li>
 * <li>dodatkowe uwagi</li>
 * </ul>
 *
 */
@Data
public class ProductSchema {
	private String code;
	private String type;
	private String brand;
	private String model;
	private String additionalNotes;
}
