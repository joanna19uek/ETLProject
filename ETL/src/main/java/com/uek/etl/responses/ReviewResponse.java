package com.uek.etl.responses;

import lombok.Data;

/**
 * Klasa reprezentująca zwracaną odpowiedź na żądanie pobrania opinii dla wybranego produktu.<br>
 * Zawiera:
 * <ul>
 * <li>kod produktu,</li>
 * <li>treść opinii,</li>
 * <li>liczbę gwiazdek,</li>
 * <li>autora opinii,</li>
 * <li>datę dodania opinii,</li>
 * <li>rekomendację (poleca/nie poleca),</li>
 * <li>lizbę osób uznających opinię za przydatną i nieprzydatną,</li>
 * <li>zalety oraz wady produktu</li>
 * </ul>
 *
 */
@Data
public class ReviewResponse {
	private String productId;
	private String reviewSummary;
	private Double starsNumber;
	private String author;
	private String date;
	private String recommendation;
	private Integer votesForReviewUseful;
	private Integer votesForReviewUseless;
	private String advantages;
	private String disadvantages;
}
