package com.joey.memememelandia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MemeMemelandiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemeMemelandiaApplication.class, args);
	}

}
