package com.propertyproject.propertyservice;

import org.springframework.boot.SpringApplication;

public class TestPropertyserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(PropertyserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
