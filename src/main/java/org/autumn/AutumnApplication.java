package org.autumn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for the Autumn Framework.
 * <p>
 * This class serves as the entry point for running the framework as a standalone Spring
 * Boot application, though typically it's used as a testing library.
 */
@SpringBootApplication
public class AutumnApplication {

	/**
	 * Main method to start the Spring Boot application.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(AutumnApplication.class, args);
	}

}
