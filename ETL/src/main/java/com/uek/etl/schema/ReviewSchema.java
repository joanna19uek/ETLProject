package com.uek.etl.schema;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klasa reprezentująca model danych opinii.<br>
 * Zawiera:
 * <ul>
 * <li>treść opinii,</li>
 * <li>liczbę gwiazdek,</li>
 * <li>autora opinii,</li>
 * <li>datę dodania opinii,</li>
 * <li>rekomendację (poleca/nie poleca),</li>
 * <li>liczbę osób uważających opinię za przydatną i nieprzydatną,</li>
 * <li>wady oraz zalety produktu</li>
 * </ul>
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSchema {
	private String reviewSummary;
	private Double starsNumber;
	private String author;
	private Timestamp date;
	private String recommendation;
	private Integer voteReviewUseful;
	private Integer voteReviewUseless;
	private List<String> disadvantages;
	private List<String> advantages;
}
