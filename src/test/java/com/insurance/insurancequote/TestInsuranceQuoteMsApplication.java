package com.insurance.insurancequote;

import org.springframework.boot.SpringApplication;

public class TestInsuranceQuoteMsApplication {

	public static void main(String[] args) {
		SpringApplication.from(InsuranceQuoteMsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
