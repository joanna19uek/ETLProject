package com.uek.etl.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uek.etl.dao.AdvantageDao;
import com.uek.etl.dao.DisadvantageDao;
import com.uek.etl.dao.ReviewDao;
import com.uek.etl.dao.entities.Review;
import com.uek.etl.responses.ReviewResponse;

/**
 * Serwis do zarządzania opiniami
 *
 */
@Service
public class ReviewService {

	@Autowired
	ReviewDao reviewDao;
	
	@Autowired
	AdvantageDao advantageDao;
	
	@Autowired
	DisadvantageDao disadvantageDao;
	
	/**
	 * Pobiera listę opinii dla produktu
	 * @param productCode kod produktu
	 * @return dane opinii wraz z wadami i zaletami
	 */
	public List<ReviewResponse> getReviewsForProduct(String productCode) {
		List<ReviewResponse> response = new ArrayList<ReviewResponse>();
		
		List<Review> reviews = reviewDao.getReviewsForProduct(productCode);
		for (Review rev : reviews) {
			ReviewResponse reviewResponse = new ReviewResponse();
			reviewResponse.setProductId(rev.getProductId());
			reviewResponse.setReviewSummary(rev.getReviewSummary());
			reviewResponse.setStarsNumber(rev.getStarsNumber());
			reviewResponse.setAuthor(rev.getAuthor());
			Date date = new Date(rev.getDate().getTime());
			reviewResponse.setDate(new SimpleDateFormat("dd-MM-yyyy").format(date));
			reviewResponse.setRecommendation(rev.getRecommendation() != null ? rev.getRecommendation() : "-");
			reviewResponse.setVotesForReviewUseful(rev.getVotesForReviewUseful());
			reviewResponse.setVotesForReviewUseless(rev.getVotesForReviewUseless());
			reviewResponse.setAdvantages(rev.getAdvantages().stream().map(adv -> adv.getValue()).collect(Collectors.joining(", ")));
			reviewResponse.setDisadvantages(rev.getDisadvantages().stream().map(disadv -> disadv.getValue()).collect(Collectors.joining(", ")));
			response.add(reviewResponse);
		}
		
		return response;
	}
	
	/**
	 * Usuwa wszystkie opinie wraz z wadami i zaletami
	 * @return liczba usuniętych opinii
	 */
	public Integer deleteAllReviews() {
		advantageDao.deleteAllAdvantages();
		disadvantageDao.deleteAllDisadvantages();
		return reviewDao.deleteAllReviews();
	}
}
