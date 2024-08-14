package com.example.jangkau;

import io.github.cdimascio.dotenv.Dotenv;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class JangkauApplication {

	public static void main(String[] args) {
//		Dotenv dotenv = Dotenv.load();
//
//		System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
//		System.setProperty("POSTGRESQL_USER", dotenv.get("POSTGRESQL_USER"));
//		System.setProperty("POSTGRESQL_PASS", dotenv.get("POSTGRESQL_PASS"));

		SpringApplication.run(JangkauApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
