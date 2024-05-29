package com.joey.memelandiaapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MemelandiaApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemelandiaApiGatewayApplication.class, args);
	}

}
