package com.covidTestManagement.covid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan(basePackages = "com.covidTestManagement.covid")
public class CovidApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidApplication.class, args);
	}

}
  