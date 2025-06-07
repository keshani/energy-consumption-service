package com.enefit.energyconsumption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class EnergyconsumptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergyconsumptionApplication.class, args);
	}

}
