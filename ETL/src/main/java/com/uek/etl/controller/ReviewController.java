package com.uek.etl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uek.etl.responses.ReviewResponse;
import com.uek.etl.services.ReviewService;

/**
 * Kontroler obsługujący żądania związane z opiniami
 *
 */
@Controller
@CrossOrigin
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	/**
	 * Obsługuje żądanie pobrania opinii dla wskazanego produktu
	 * @param productCode kod produktu
	 * @return lista opinii
	 */
	@RequestMapping(value="reviews/{productCode}", method=RequestMethod.GET)
	@ResponseBody
	public List<ReviewResponse> getReviews(@PathVariable String productCode) {
		return reviewService.getReviewsForProduct(productCode);
	}
}
