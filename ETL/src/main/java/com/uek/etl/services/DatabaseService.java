package com.uek.etl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uek.etl.responses.DeleteAllResponse;
import com.uek.etl.responses.DeleteReviewsResponse;

/**
 * Serwis do zarządzania bazą danych
 *
 */
@Service
public class DatabaseService {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ReviewService reviewService;
	
	/**
	 * Usuwa wszystkie rekordy z bazy danych
	 * @return liczba usuniętych produktów oraz opinii
	 */
	public DeleteAllResponse deleteAll() {
		DeleteAllResponse deleteAllResponse = new DeleteAllResponse();
		
		deleteAllResponse.setDeletedReviewsNumber(reviewService.deleteAllReviews());
		deleteAllResponse.setDeletedProductsNumber(productService.deleteAllProducts());
		
		return deleteAllResponse;
	}
	
	/**
	 * Usuwa wszystkie opinie z bazy danych
	 * @return liczba usuniętych opinii
	 */
	public DeleteReviewsResponse deleteReviews() {
		DeleteReviewsResponse deleteReviewsResponse = new DeleteReviewsResponse();
		deleteReviewsResponse.setDeletedReviewsNumber(reviewService.deleteAllReviews());
		return deleteReviewsResponse;
	}
}
