package com.uek.etl.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uek.etl.responses.FileResponse;
import com.uek.etl.services.FileService;

/**
 * 
 * Kontroler obsługujący żadania związane z eksportem danych do plików CSV i TXT
 * 
 */
@Controller
@CrossOrigin
public class FileController {

	@Autowired
	private FileService fileService;
	
	/**
	 * Obsługuje żądanie wygenerowania pliku csv z opiniami dla wszystkich produktów
	 * @return nazwa pliku i jego zawartość
	 */
	@RequestMapping(value="generateCSV", method=RequestMethod.GET)
	@ResponseBody
	public FileResponse generateCSV() {
		return fileService.generateCSV();
	}
	
	/**
	 * Obsługuje żądanie wygenerowania pliku csv z opiniami dla wskazanego produktu
	 * @param productCode kod produktu
	 * @return nazwa pliku i jego zawartość
	 * @throws IOException
	 */
	@RequestMapping(value="generateCSV/{productCode}", method=RequestMethod.GET)
	@ResponseBody
	public FileResponse generateCSV(@PathVariable String productCode) throws IOException {
		return fileService.generateCSV(productCode);
	}
	
	/**
	 * Obsługuje żądanie wygenerowania pliku txt z opiniami dla wskazanego produktu
	 * @param productCode kod produktu
	 * @return nazwa pliku i jego zawartość
	 * @throws IOException
	 */
	@RequestMapping(value="generateTXT/{productCode}", method=RequestMethod.GET)
	@ResponseBody
	public FileResponse generateTXT(@PathVariable String productCode) throws IOException {
		return fileService.generateTXT(productCode);
	}
	
	/**
	 * Obsługuje żądanie wygenerowania pliku txt z opiniami dla wszystkich produktów
	 * @return nazwa pliku i jego zawartość
	 */
	@RequestMapping(value="generateTXT", method=RequestMethod.GET)
	@ResponseBody
	public FileResponse generateTXT() {
		return fileService.generateTXT();
	}
	
}
