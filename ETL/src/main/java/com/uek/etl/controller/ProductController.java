package com.uek.etl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uek.etl.dao.entities.Product;
import com.uek.etl.services.ProductService;

/**
 * Kontroler obsługujący żadania związane z produktami
 *
 */
@Controller
@CrossOrigin
public class ProductController {

	@Autowired
	private ProductService productService;
	
	/**
	 * Obsługuje żądanie pobrania wszystkich produktów
	 * @return lista produktów
	 */
	@RequestMapping(value="products", method=RequestMethod.GET)
	@ResponseBody
	public List<Product> getProducts() {
		return productService.getAllProducts();
	}
}
