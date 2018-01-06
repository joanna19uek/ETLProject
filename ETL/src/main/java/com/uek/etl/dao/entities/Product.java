package com.uek.etl.dao.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Klasa reprezentująca tabelę produktów zawierająca pola:
 * <ul>
 * <li>id - unikalny kod produktu,</li>
 * <li>type - kategoria,</li>
 * <li>brand - marka,</li>
 * <li>model - model,</li>
 * <li>additionalNotes - dodatkowe uwagi
 * </ul>
 *
 */
@Entity
@Table(name="product")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * unikalny kod produktu
	 */
	@Id
	@Getter @Setter
	private String id;
	
	/**
	 * kategoria
	 */
	@Getter @Setter
	private String type;
	
	/**
	 * marka
	 */
	@Getter @Setter
	private String brand;
	
	/**
	 * model
	 */
	@Getter @Setter
	private String model;
	
	/**
	 * dodatkowe uwagi
	 */
	@Column(name="additional_notes")
	@Getter @Setter
	private String additionalNotes;
	
	@OneToMany(mappedBy="product")
	private List<Review> reviews;
}
