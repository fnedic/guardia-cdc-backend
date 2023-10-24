package com.CDC.GuardiaBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.CDC.GuardiaBackend.mappers")
@SpringBootApplication
public class GuardiaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuardiaBackendApplication.class, args);
	}

}
