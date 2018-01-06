package com.uek.etl.dao.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

/**
 * Klasa reprezentująca tabelę opinii zawierająca pola:
 * <ul>
 * <li>id - unikalny identyfikator opinii,</li>
 * <li>productId - kod produktu,</li>
 * <li>reviewSummary - treść opinii,</li>
 * <li>starsNumber - liczba gwiazdek,</li>
 * <li>author - autor opinii,</li>
 * <li>date - data dodania opinii,</li>
 * <li>recommendation - rekomendacja (poleca/nie poleca),</li>
 * <li>votesForReviewUseful - liczba osób uważających opinię za przydatną,</li>
 * <li>votesForReviewUseless - liczba osób uważających opinię za nieprzydatną</li>
 * </ul>
 *
 */
@Entity
@Table(name="review")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * unikalny identyfikator opinii
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
	private Integer id;
	
	/**
	 * kod produktu
	 */
	@Column(name="product_id", insertable=false, updatable=false)
	@Getter
	private String productId;
	
	/**
	 * treść opinii
	 */
	@Column(name="review_summary")
	@Getter @Setter
	private String reviewSummary;
	
	/**
	 * liczba gwiazdek
	 */
	@Column(name="stars_number")
	@Getter @Setter
	private Double starsNumber;
	
	/**
	 * autor opinii
	 */
	@Getter @Setter
	private String author;
	
	/**
	 * data dodania opinii
	 */
	@Getter @Setter
	private Timestamp date;
	
	/**
	 * rekomendacja
	 */
	@Getter @Setter
	private String recommendation;
	
	/**
	 * liczba osób uważających opinię za przydatną
	 */
	@Column(name="votes_for_review_useful")
	@Getter @Setter
	private Integer votesForReviewUseful;
	
	/**
	 * liczba osób uważających opinię za nieprzydatną
	 */
	@Column(name="votes_for_review_useless")
	@Getter @Setter
	private Integer votesForReviewUseless;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	@Getter @Setter
	private Product product;
	
	/**
	 * zalety
	 */
	@OneToMany(mappedBy="review")
	@Getter
	@JsonManagedReference
	private List<Advantage> advantages;
	
	/**
	 * wady
	 */
	@OneToMany(mappedBy="review")
	@Getter
	@JsonManagedReference
	private List<Disadvantage> disadvantages;
}
