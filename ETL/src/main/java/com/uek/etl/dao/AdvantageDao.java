package com.uek.etl.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.uek.etl.dao.entities.Advantage;
import com.uek.etl.dao.entities.Review;

/**
 * Repozytorium odpowiedzialne za operacje na bazie danych
 * dotyczące zapisu i odczytu danych z tabeli zalet
 *
 */
@Repository
@Transactional
public class AdvantageDao {

	@PersistenceContext
	EntityManager entityManager;
	
	/**
	 * Zapisuje zaletę do bazy danych
	 * @param review opinia, której dana zaleta dotyczy
	 * @param value zaleta
	 */
	public void saveAdvantage(Review review, String value) {
		Advantage advantage = new Advantage();
		
		advantage.setReview(review);
		advantage.setValue(value);
		
		entityManager.persist(advantage);
	}
	
	/**
	 * Usuwa wszystkie rekordy z tabeli zalet
	 */
	public void deleteAllAdvantages() {
		Query query = entityManager.createQuery("DELETE FROM Advantage");
		query.executeUpdate();
	}
}
