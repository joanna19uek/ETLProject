package com.uek.etl.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.uek.etl.dao.entities.Disadvantage;
import com.uek.etl.dao.entities.Review;

/**
 * Repozytorium odpowiedzialne za operacje na bazie danych
 * dotyczące zapisu i odczytu danych z tabeli wad
 *
 */
@Repository
@Transactional
public class DisadvantageDao {

	@PersistenceContext
	EntityManager entityManager;
	
	/**
	 * Zapisuje wadę do bazy danych
	 * @param review opinia, której dana wada dotyczy
	 * @param value wada
	 */
	public void saveDisadvantage(Review review, String value) {
		Disadvantage disadvantage = new Disadvantage();
		
		disadvantage.setReview(review);
		disadvantage.setValue(value);
		
		entityManager.persist(disadvantage);
	}
	
	/**
	 * Usuwa wszystkie rekordy z tabeli wad
	 */
	public void deleteAllDisadvantages() {
		Query query = entityManager.createQuery("DELETE FROM Disadvantage");
		query.executeUpdate();
	}
}
