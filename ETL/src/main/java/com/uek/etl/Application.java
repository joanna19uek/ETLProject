package com.uek.etl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główna klasa aplikacji
 * 
 */
@SpringBootApplication
public class Application {

	/**
	 * Główna metoda uruchamiająca aplikację
	 * @param args opcjonalne parametry uruchomieniowe
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
