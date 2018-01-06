package com.uek.etl.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uek.etl.responses.ETLResponse;
import com.uek.etl.responses.ExtractResponse;
import com.uek.etl.responses.LoadResponse;
import com.uek.etl.responses.TransformResponse;
import com.uek.etl.services.EtlService;

/**
 * 
 * Kontroler obsługujący żadania związane z pobraniem, transformacją
 * i załadowaniem danych do bazy danych (proces ETL)
 */
@Controller
@CrossOrigin
public class ETLController {

	@Autowired
	private EtlService etlService;
	
	/**
	 * Obsługuje żądanie pobrania danych
	 * @param productCode kod produktu
	 * @return nazwa produktu, liczba stron z opiniami oraz liczba opinii
	 * @throws IOException
	 */
	@RequestMapping(value="extract/{productCode}", method=RequestMethod.GET)
	@ResponseBody
	public ExtractResponse extract(@PathVariable String productCode) throws IOException {
		return etlService.extract(productCode);
	}
	
	/**
	 * Obsługuje żądanie transformacji pobranych danych do odpowiedniego formatu
	 * @return liczba produktów i opinii gotowych do załadowania do bazy
	 */
	@RequestMapping(value="transform", method=RequestMethod.GET)
	@ResponseBody
	public TransformResponse transform() {
		return etlService.transform();
	}
	
	/**
	 * Obsługuje żądanie załodowania przetworzonych danych do bazy danych
	 * @return liczba dodanych rekordów do tabeli produktów, opinii, zalet i wad
	 */
	@RequestMapping(value="load", method=RequestMethod.GET)
	@ResponseBody
	public LoadResponse load() {
		return etlService.load();
	}
	
	/**
	 * Obsługuje żądanie pobrania danych, ich transformacji i załadowania do bazy danych
	 * @param productCode kod produktu
	 * @return nazwa produktu, liczba pobranych stron z opiniami, liczba pobranych opinii,<br>
	 * 		   liczba produktów i opinii gotowych do załadowania do bazy,<br>
	 * 		   liczba dodanych rekordów do tabeli produktów, opinii, zalet i wad
	 * @throws IOException
	 */
	@RequestMapping(value="etl/{productCode}", method=RequestMethod.GET)
	@ResponseBody
	public ETLResponse etl(@PathVariable String productCode) throws IOException {
		return etlService.etl(productCode);
	}
}
