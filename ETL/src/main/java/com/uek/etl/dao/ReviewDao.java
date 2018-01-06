package com.uek.etl.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.uek.etl.dao.entities.Product;
import com.uek.etl.dao.entities.Review;
import com.uek.etl.schema.ReviewSchema;

/**
 * Repozytorium odpowiedzialne za operacje na bazie danych
 * dotyczące odczytu i zapisu danych do tabeli opinii
 *
 */
@Repository
@Transactional
public class ReviewDao {

	@PersistenceContext
	EntityManager entityManager;
	
	/**
	 * Pobiera z bazy danych informacje o opinii
	 * @param productId kod produktu
	 * @param author autor opinii
	 * @param date data dodania opinii
	 * @return dane opinii, jeśli taka opinia istnieje; w przeciwnym wypadku - null
	 */
	public Review getReview(String productId, String author, Timestamp date) {
		TypedQuery<Review> query = entityManager.createQuery("SELECT r FROM Review r WHERE " +
															 "r.productId = :productId AND " +
															 "r.author = :author AND " +
															 "r.date = :date", Review.class);
		query.setParameter("productId", productId);
		query.setParameter("author", author);
		query.setParameter("date", date);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Zapisuje opinię do bazy
	 * @param product produkt, którego opinia dotyczy
	 * @param reviewParams dane opinii
	 * @return zapisana opinia
	 */
	public Review saveReview(Product product, ReviewSchema reviewParams) {
		Review review = new Review();
		
		review.setProduct(product);
		review.setReviewSummary(reviewParams.getReviewSummary());
		review.setStarsNumber(reviewParams.getStarsNumber());
		review.setAuthor(reviewParams.getAuthor());
		review.setDate(reviewParams.getDate());
		review.setRecommendation(reviewParams.getRecommendation());
		review.setVotesForReviewUseful(reviewParams.getVoteReviewUseful());
		review.setVotesForReviewUseless(reviewParams.getVoteReviewUseless());
		
		entityManager.persist(review);
		
		return review;
	}
	
	/**
	 * Pobiera listę opinii dla wskazanego produktu
	 * @param productCode kod produktu
	 * @return lista opinii
	 */
	public List<Review> getReviewsForProduct(String productCode) {
		TypedQuery<Review> query = entityManager.createQuery("SELECT r FROM Review r WHERE r.productId = :productId", Review.class);
		query.setParameter("productId", productCode);
		return query.getResultList();
	}
	
	/**
	 * Usuwa wszystkie rekordy z tabeli opinii
	 * @return liczba usuniętych rekordów
	 */
	public Integer deleteAllReviews() {
		Query query = entityManager.createQuery("DELETE FROM Review");
		return query.executeUpdate();
	}
}
