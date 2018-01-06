package com.uek.etl.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uek.etl.dao.ProductDao;
import com.uek.etl.dao.entities.Product;

/**
 * Serwis do zarządzania produktami
 *
 */
@Service
public class ProductService {
	
	@Autowired
	ProductDao productDao;
	
	/**
	 * Pobiera wszystkie produkty
	 * @return lista produktów
	 */
	public List<Product> getAllProducts() {
		return productDao.getAllProducts();
	}
	
	/**
	 * Pobiera informacje o wskazanym produkcie
	 * @param productCode kod produktu
	 * @return obiekt zawierający wszystkie informacje o produkcie
	 */
	public Product getProduct(String productCode){
		return productDao.getProduct(productCode);
	}
	
	/**
	 * Usuwa wszystkie produkty
	 * @return liczba usuniętych produktów
	 */
	public Integer deleteAllProducts() {
		return productDao.deleteAllProducts();
	}
}
