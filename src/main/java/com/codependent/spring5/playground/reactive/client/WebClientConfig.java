package com.codependent.spring5.playground.reactive.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebClientConfig {
	
	@Bean
	public ObjectMapper jacksonObjectMapper(){
		return new ObjectMapper();
	}
	
	@Bean
	public WebClient webClient(){
		WebClient.create("http://example.com");
		WebClient webClient = WebClient.create();
		return webClient;
	}
	
}
