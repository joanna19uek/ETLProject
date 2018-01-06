package com.uek.etl.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uek.etl.dao.AdvantageDao;
import com.uek.etl.dao.DisadvantageDao;
import com.uek.etl.dao.ProductDao;
import com.uek.etl.dao.ReviewDao;
import com.uek.etl.dao.entities.Product;
import com.uek.etl.dao.entities.Review;
import com.uek.etl.responses.ETLResponse;
import com.uek.etl.responses.ExtractResponse;
import com.uek.etl.responses.LoadResponse;
import com.uek.etl.responses.TransformResponse;
import com.uek.etl.schema.ProductSchema;
import com.uek.etl.schema.ReviewSchema;

/**
 * Serwis do realizacji procesu ETL
 *
 */
@Service
public class EtlService {
	private static final Integer COMMENTS_ON_PAGE = 10;
	
	private List<Document> htmlDocuments;
	private ProductSchema product;
	private String productCode;
	private List<ReviewSchema> reviewsList;
	
	@Autowired
	ProductDao productDao;
	
	@Autowired
	ReviewDao reviewDao;
	
	@Autowired
	AdvantageDao advantageDao;
	
	@Autowired
	DisadvantageDao disadvantageDao;
	
	/**
	 * Pobiera pliki z opiniami dla wskazanego produktu
	 * @param productCode kod produktu
	 * @return nazwa produktu, liczba stron z opiniami oraz liczba samych opinii
	 * @throws IOException
	 */
	public ExtractResponse extract(String productCode) throws IOException {
		this.productCode = productCode;
		ExtractResponse extractResponse = new ExtractResponse();
		
		htmlDocuments = new ArrayList<Document>();
		
		List<String> urls = new ArrayList<String>();
		urls.add("https://www.ceneo.pl/" + productCode + "#tab=reviews");
		
		Document doc = Jsoup.connect(urls.get(0)).get();
		
		String product = doc.select("nav > dl > dd > strong").first().text();
		extractResponse.setProduct(product);
		
		String reviewsTab = doc.select("ul.wrapper > li > a").get(2).text();
		Integer bracketIndex = reviewsTab.indexOf("(");
		if (bracketIndex == -1) {		//brak opinii
			extractResponse.setReviewsPagesNumber(0);
			extractResponse.setReviewsNumber(0);
		} else {
			Integer reviewsNumber = Integer.valueOf(reviewsTab.substring(bracketIndex+1, reviewsTab.indexOf(")")));
			extractResponse.setReviewsNumber(reviewsNumber);
			
			Integer pagesNumber;
			if (reviewsNumber % COMMENTS_ON_PAGE != 0) {
				pagesNumber = reviewsNumber / COMMENTS_ON_PAGE + 1;
			} else {
				pagesNumber = reviewsNumber / COMMENTS_ON_PAGE;
			}
			extractResponse.setReviewsPagesNumber(pagesNumber);
			
			for (int i = 1; i < pagesNumber; i++) {
				urls.add("https://www.ceneo.pl/" + productCode + "/opinie-" + (i + 1));
			}
		}
		
		for (String url : urls) {
			doc = Jsoup.connect(url).get();
			htmlDocuments.add(doc);
		}
		
		return extractResponse;
	}
	
	/**
	 * Dokonuje transformacji pobranych danych do odpowiedniego formatu
	 * @return liczba produktów i opinii gotowych do załadowania do bazy danych
	 */
	public TransformResponse transform() {
		TransformResponse transformResponse = new TransformResponse();
		
		product = getProduct(htmlDocuments.get(0));
		transformResponse.setProductRecords(1);
		
		if (htmlDocuments.size() > 0) {
			reviewsList = new ArrayList<ReviewSchema>();
			for (Document doc : htmlDocuments) {
				Elements reviews = doc.select("ol.product-reviews").first().children();
				
				for (Element rev : reviews) {
					ReviewSchema review = getReview(rev);
					reviewsList.add(review);
				}
			}
		}
		transformResponse.setReviewRecords(reviewsList.size());
		
		return transformResponse;
	}
	
	private ProductSchema getProduct(Document document) {
		ProductSchema product = new ProductSchema();
		product.setCode(productCode);
		
		Element ddElement = document.select("nav > dl > dd").first();
		String type = ddElement.select("span > a > span").get(3).text();
		product.setType(type);
		
		String productLine = document.select("nav > dl > dd > strong").first().text();
		String[] productLineElements = productLine.split(" ");
		product.setBrand(productLineElements[0]);
		StringBuilder model = new StringBuilder();
		int i = 1;
		while (!productLineElements[i].toLowerCase().contains("gb") &&
				//!productLineElements[i].contains("-") &&
				!productLineElements[i].toLowerCase().contains("dual") &&
				!productLineElements[i].toLowerCase().contains(",") &&
				!productLineElements[i].toLowerCase().contains("\"")) {
			model.append(productLineElements[i] + " ");
			if (i < productLineElements.length-1) {
				i++;
			} else {
				break;
			}
		}
		model.deleteCharAt(model.length()-1);
		
		StringBuilder additionalNotes = new StringBuilder();
		Element versions = document.select("div.product-content > dl").last();
		Map<String, List<String>> variants = new HashMap<String, List<String>>();
		for (Element elem : versions.select("dl.product-param")) {
			List<String> valuesList = new ArrayList<String>();
			String name = elem.select("span.param-name").text();
			for (Element variant : elem.select("dd > ul > li")) {
				if (variant.text().contains(" ")) {
					valuesList.add(new StringBuilder(variant.text()).deleteCharAt(variant.text().indexOf(" ")).toString());
				} else {
					valuesList.add(variant.text());
				}
			}
			variants.put(name, valuesList);
		}
		for (Map.Entry<String, List<String>> entry : variants.entrySet()) {
			if (entry.getKey().equals("Wersja kolorystyczna")) {
				for (String paramValue : entry.getValue()) {
					if (productLine.toLowerCase().contains(paramValue.toLowerCase()) || productLine.toLowerCase().contains(paramValue.toLowerCase().substring(0, 4))) {
						if (additionalNotes.length() != 0) {
							additionalNotes.append(", ");
						}
						additionalNotes.append(paramValue);
						if (model.toString().contains(paramValue)) {
							model.delete(model.indexOf(paramValue)-1, model.length());
						}
					}
				}
			} else {
				for (String paramValue : entry.getValue()) {
					if (productLine.toLowerCase().contains(paramValue.toLowerCase())) {
						if (additionalNotes.length() != 0) {
							additionalNotes.append(", ");
						}
						additionalNotes.append(paramValue);
					}
				}
			}
		}
		if (additionalNotes.length() != 0) {
			product.setAdditionalNotes(additionalNotes.toString());
		}
		
		product.setModel(model.toString());
		
		return product;
	}
	
	private ReviewSchema getReview(Element rev) {
		ReviewSchema review = new ReviewSchema();
		
		List<String> disadvantages = new ArrayList<String>();
		Element cons = rev.select("div.cons-cell > ul").first();
		if (cons != null) {
			Elements c = cons.select("li");
			for (Element p : c) {
				disadvantages.add(p.text());
			}
		}
		review.setDisadvantages(disadvantages);
		
		List<String> advantages = new ArrayList<String>();
		Element pros = rev.select("div.pros-cell > ul").first();
		if (pros != null) {
			Elements e = pros.select("li");
			for (Element p : e) {
				advantages.add(p.text());
			}
		}
		review.setAdvantages(advantages);
		
		String reviewSummary = rev.select("p.product-review-body").first().text();
		reviewSummary = reviewSummary.replaceAll(";", ":");
		reviewSummary = reviewSummary.replaceAll("\"", "''");
		review.setReviewSummary(reviewSummary);
		
		String stars = rev.select("span.review-score-count").first().text();
		Double starsNumber = Double.valueOf((stars.substring(0, stars.indexOf("/"))).replace(",", "."));
		review.setStarsNumber(starsNumber);
		
		String author = rev.select("div.reviewer-name-line").first().text();
		review.setAuthor(author);
		
		String date = rev.select("span.review-time > time").first().attr("datetime");
		review.setDate(Timestamp.valueOf(date));
		
		Element recommendation = rev.select("div.product-review-summary > em").first();
		if (recommendation != null) {
			review.setRecommendation(recommendation.text());
		}
		
		Integer voteYes = Integer.valueOf(rev.select("button.vote-yes > span").first().text());
		review.setVoteReviewUseful(voteYes);
		
		Integer voteNo = Integer.valueOf(rev.select("button.vote-no > span").first().text());
		review.setVoteReviewUseless(voteNo);
		
		return review;
	}
	
	/**
	 * Zapisuje produkt i opinie, o ile nie ma ich jeszcze w bazie danych
	 * @return liczba nowych rekordów dodanych do tabeli produktów, opinii, zalet i wad
	 */
	public LoadResponse load() {
		Integer newProductRecordsNumber = 0;
		Integer newReviewRecordsNumber = 0;
		Integer newAdvantagesRecordsNumber = 0;
		Integer newDisadvantagesRecordsNumber = 0;
		
		Product productFromDB = productDao.getProduct(productCode);
		Product savedProduct = new Product();
		if (productFromDB == null) {
			savedProduct = productDao.saveProduct(productCode, product);
			newProductRecordsNumber++;
		}
		
		for (ReviewSchema review : reviewsList) {
			Review reviewFromDB = reviewDao.getReview(productFromDB == null ? savedProduct.getId() : productFromDB.getId(), review.getAuthor(), review.getDate());
			if (reviewFromDB == null) {
				Review savedReview = reviewDao.saveReview(productFromDB == null ? savedProduct : productFromDB, review);
				newReviewRecordsNumber++;
				
				if (!review.getAdvantages().isEmpty()) {
					for (String advantage : review.getAdvantages()) {
						advantageDao.saveAdvantage(savedReview, advantage);
						newAdvantagesRecordsNumber++;
					}
				}
				
				if (!review.getDisadvantages().isEmpty()) {
					for (String disadvantage : review.getDisadvantages()) {
						disadvantageDao.saveDisadvantage(savedReview, disadvantage);
						newDisadvantagesRecordsNumber++;
					}
				}
			}
		}
		
		return new LoadResponse(newProductRecordsNumber, newReviewRecordsNumber, newAdvantagesRecordsNumber, newDisadvantagesRecordsNumber);
	}
	
	/**
	 * Realizuje cały proces ETL
	 * @param productCode kod produktu
	 * @return nazwa produktu, liczba stron z opiniami, liczba opinii,<br>
	 * 		   liczba produktów i opinii gotowych do załadowania do bazy danych,<br>
	 * 		   liczba nowych rekordów dodanych do tabeli produktów, opinii, wad i zalet
	 * @throws IOException
	 */
	public ETLResponse etl(String productCode) throws IOException {
		ExtractResponse extractResponse = extract(productCode);
		TransformResponse transformResponse = transform();
		LoadResponse loadResponse = load();
		return new ETLResponse(
				extractResponse.getProduct(),
				extractResponse.getReviewsPagesNumber(),
				extractResponse.getReviewsNumber(),
				transformResponse.getProductRecords(),
				transformResponse.getReviewRecords(),
				loadResponse.getNewProductRecordsNumber(),
				loadResponse.getNewReviewRecordsNumber(),
				loadResponse.getNewAdvantagesRecordsNumber(),
				loadResponse.getNewDisadvantagesRecordsNumber());
	}
}
