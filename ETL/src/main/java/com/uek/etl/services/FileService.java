package com.uek.etl.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uek.etl.dao.entities.Product;
import com.uek.etl.responses.FileResponse;
import com.uek.etl.responses.ReviewResponse;

/**
 * Serwis do generowania plików csv i txt z opiniami
 *
 */
@Service
public class FileService {
	/**
	 * Separator pól w plikach txt i csv
	 */
	public final String SEPARATOR = ";";
	
	/**
	 * Znak końca linii w plikach txt i csv
	 */
	public final String END_LINE_CHAR = "\n";
	
	/**
	 * Znak zabezpieczający
	 */
	public final String SECURE_STRING = "\"";
	
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ReviewService reviewService;
	
	/**
	 * Generuje plik txt z opiniami dla wybranego produktu
	 * @param productCode kod produktu
	 * @return nazwa pliku i jego zawartość
	 */
	public FileResponse generateTXT(String productCode) {
		FileResponse response = new FileResponse();		
					
		String[] txtContent = prepareProductToExport(productCode);
		
		response.setFileContent(txtContent[0]);
		response.setFileName(txtContent[1] + ".txt");
		
		return response;
	}
	
	/**
	 * Generuje plik txt z opiniami dla wszystkich produktów
	 * @return nazwa pliku i jego zawartość
	 */
	public FileResponse generateTXT() {
		FileResponse response = new FileResponse();
					
		String txtContent = prepareProductsToExport();	
		
		response.setFileContent(txtContent);
		response.setFileName("All" + createName() + ".txt");
		
		return response;
	}
	
	/**
	 * Generuje plik csv z opiniami dla wybranego produktu
	 * @param productCode kod produktu
	 * @return nazwa pliku i jego zawartość
	 */
	public FileResponse generateCSV(String productCode) {
		FileResponse response = new FileResponse();		
					
		String[] csvContent = prepareProductToExport(productCode);
		
		response.setFileContent(csvContent[0]);
		response.setFileName(csvContent[1] + ".csv");
		
		return response;
	}
	
	/**
	 * Generuje plik csv z opiniami dla wszystkich produktów
	 * @return nazwa pliku i jego zawartość
	 */
	public FileResponse generateCSV() {
		FileResponse response = new FileResponse();		
					
		String csvContent = prepareProductsToExport();
		
		response.setFileContent(csvContent);
		response.setFileName("AllProducts" + createName() + ".csv");
		
		return response;
	}
	
	private String[] prepareProductToExport(String productCode) {
		String FIRST_LINE = "Review Summary" + SEPARATOR +
							"Stars Number" + SEPARATOR +
							"Author" + SEPARATOR +
							"Date" + SEPARATOR +
							"Recommendation" + SEPARATOR +
							"Useful Vote" + SEPARATOR +
							"Useless Vote" + SEPARATOR +
							"Disadvantages" + SEPARATOR +
							"Advantages" + END_LINE_CHAR;
		
		Product product = productService.getProduct(productCode);
		
		String[] content = new String[2];
		content[0] = FIRST_LINE;
		
		content[1] = product.getId() + "_" + product.getType() + "_" + product.getBrand() + "_" + product.getModel();
		content[1] = content[1].replaceAll(" ", "-");
		
		List<ReviewResponse> reviewsList = reviewService.getReviewsForProduct(product.getId());
		String aLine = "";
		for(ReviewResponse r : reviewsList){
			aLine += SECURE_STRING + r.getReviewSummary() + SECURE_STRING + SEPARATOR;
			aLine += SECURE_STRING + r.getStarsNumber() + SECURE_STRING + SEPARATOR;
			aLine += SECURE_STRING + r.getAuthor() + SECURE_STRING + SEPARATOR;
			aLine += SECURE_STRING + r.getDate() + SECURE_STRING + SEPARATOR;
			aLine += SECURE_STRING + r.getRecommendation() + SECURE_STRING + SEPARATOR;
			aLine += SECURE_STRING + r.getVotesForReviewUseful() + SECURE_STRING + SEPARATOR;
			aLine += SECURE_STRING + r.getVotesForReviewUseless() + SECURE_STRING + SEPARATOR;
			aLine += SECURE_STRING + r.getDisadvantages() + SECURE_STRING + SEPARATOR;
			aLine += SECURE_STRING + r.getAdvantages() + SECURE_STRING + SEPARATOR;
			aLine += END_LINE_CHAR;
		}
		content[0] += aLine;
		
		return content;
	}
	
	private String prepareProductsToExport() {
		String FIRST_LINE = "Code" + SEPARATOR +
							"Type" + SEPARATOR +
							"Brand" + SEPARATOR +
							"Model" + SEPARATOR +
							"Additional Notes" + SEPARATOR +
							"Review Summary" + SEPARATOR +
							"Stars Number" + SEPARATOR +
							"Author" + SEPARATOR +
							"Date" + SEPARATOR +
							"Recommendation" + SEPARATOR +
							"Useful Vote" + SEPARATOR +
							"Useless Vote" + SEPARATOR +
							"Disadvantages" + SEPARATOR +
							"Advantages" + END_LINE_CHAR;
		
		String content = FIRST_LINE;
		List<Product> productsList = productService.getAllProducts();
		for(Product p : productsList){
			String pLine = "";
			pLine += SECURE_STRING + p.getId() + SECURE_STRING + SEPARATOR;
			pLine += SECURE_STRING + p.getType() + SECURE_STRING + SEPARATOR;
			pLine += SECURE_STRING + p.getBrand() + SECURE_STRING + SEPARATOR;
			pLine += SECURE_STRING + p.getModel() + SECURE_STRING + SEPARATOR;
			pLine += SECURE_STRING + (p.getAdditionalNotes() != null ? p.getAdditionalNotes() : "") + SECURE_STRING + SEPARATOR;
			
			String aLine = "";
			List<ReviewResponse> reviewsList = reviewService.getReviewsForProduct(p.getId());
			for(ReviewResponse r : reviewsList){
				aLine += pLine;
				aLine += SECURE_STRING + r.getReviewSummary() + SECURE_STRING + SEPARATOR;
				aLine += SECURE_STRING + r.getStarsNumber() + SECURE_STRING + SEPARATOR;
				aLine += SECURE_STRING + r.getAuthor() + SECURE_STRING + SEPARATOR;
				aLine += SECURE_STRING + r.getDate() + SECURE_STRING + SEPARATOR;
				aLine += SECURE_STRING + r.getRecommendation() + SECURE_STRING + SEPARATOR;
				aLine += SECURE_STRING + r.getVotesForReviewUseful() + SECURE_STRING + SEPARATOR;
				aLine += SECURE_STRING + r.getVotesForReviewUseless() + SECURE_STRING + SEPARATOR;
				aLine += SECURE_STRING + r.getDisadvantages() + SECURE_STRING + SEPARATOR;
				aLine += SECURE_STRING + r.getAdvantages() + SECURE_STRING + SEPARATOR;
				aLine += END_LINE_CHAR;
			}
			
			content += aLine;
		}
		
		return content;
	}
	
	private String createName() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String name = dateFormat.format(new Date());
		return name;
	}
}
