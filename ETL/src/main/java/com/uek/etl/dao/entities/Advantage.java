package com.uek.etl.dao.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

/**
 * Klasa reprezentująca tabelę zalet zawierająca pola:
 * <ul>
 * <li>id - unikalny identyfikator zalety,</li>
 * <li>reviewId - identyfikator opinii,</li>
 * <li>value - zaleta</li>
 * </ul>
 */
@Entity
@Table(name="advantages")
public class Advantage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * unikalny identyfikator zalety
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
	private Integer id;
	
	/**
	 * identyfikator opinii
	 */
	@Column(name="review_id", insertable=false, updatable=false)
	@Getter
	private Integer reviewId;
	
	/**
	 * zaleta
	 */
	@Getter @Setter
	private String value;
	
	@ManyToOne
	@JoinColumn(name="review_id")
	@Getter @Setter
	@JsonBackReference
	private Review review;
}
