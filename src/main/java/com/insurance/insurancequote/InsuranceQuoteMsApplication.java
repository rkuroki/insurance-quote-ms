package com.insurance.insurancequote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InsuranceQuoteMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceQuoteMsApplication.class, args);
	}

}
