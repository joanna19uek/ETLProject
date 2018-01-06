package com.uek.etl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.uek.etl.dao.entities.Product;
import com.uek.etl.schema.ProductSchema;

/**
 * Repozytorium odpowiedzialne za operacje na bazie danych
 * dotyczące odczytu i zapisu danych do tabeli produktów
 *
 */
@Repository
@Transactional
public class ProductDao {
	
	@PersistenceContext
	EntityManager entityManager;
	
	/**
	 * Pobiera z bazy informacje o wskazanym produkcie
	 * @param productCode kod produktu
	 * @return dane produktu, jeśli taki produkt istnieje; w przeciwnym wypadku - null
	 */
	public Product getProduct(String productCode) {
		TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p " +
															  "WHERE p.id = :code", Product.class);
		query.setParameter("code", productCode);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Zapisuje produkt do bazy danych
	 * @param productCode kod produktu
	 * @param productParams dane produktu
	 * @return zapisany produkt
	 */
	public Product saveProduct(String productCode, ProductSchema productParams) {
		Product product = new Product();
		product.setId(productCode);
		product.setType(productParams.getType());
		product.setBrand(productParams.getBrand());
		product.setModel(productParams.getModel());
		product.setAdditionalNotes(productParams.getAdditionalNotes());
		
		entityManager.persist(product);
		
		return product;
	}
	
	/**
	 * Pobiera listę wszystkich produktów
	 * @return lista produktów
	 */
	public List<Product> getAllProducts() {
		TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p", Product.class);
		List<Product> products = query.getResultList();
		
		if (products.size() > 0) {
			return products;
		} else {
			return null;
		}
	}
	
	/**
	 * Usuwa wszystkie rekordy z tabeli produktów
	 * @return liczba usuniętych rekordów
	 */
	public Integer deleteAllProducts() {
		Query query = entityManager.createQuery("DELETE FROM Product");
		return query.executeUpdate();
	}
}
