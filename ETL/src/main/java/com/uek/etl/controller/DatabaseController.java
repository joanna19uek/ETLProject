package com.uek.etl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uek.etl.responses.DeleteAllResponse;
import com.uek.etl.responses.DeleteReviewsResponse;
import com.uek.etl.services.DatabaseService;

/**
 * 
 * Kontroler obsługujący żadania związane z czyszczeniem bazy danych
 *
 */
@Controller
@CrossOrigin
public class DatabaseController {
	
	@Autowired
	private DatabaseService databaseService;

	/**
	 * Obsługuje żadanie usunięcia z bazy danych wszystkich rekordów (produktów i opinii)
	 * @return liczba usuniętych produktów i opinii
	 */
	@RequestMapping(value="delete/all", method=RequestMethod.GET)
	@ResponseBody
	public DeleteAllResponse deleteAll() {
		return databaseService.deleteAll();
	}
	
	/**
	 * Obsługuje żądanie usunięcia z bazy danych wszystkich opinii
	 * @return liczba usuniętych opinii
	 */
	@RequestMapping(value="delete/reviews", method=RequestMethod.GET)
	@ResponseBody
	public DeleteReviewsResponse deleteReviews() {
		return databaseService.deleteReviews();
	}
}
