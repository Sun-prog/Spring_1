package com.spring.techserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TechservApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechservApplication.class, args);
	}

}
