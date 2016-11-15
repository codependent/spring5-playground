package com.codependent.spring5.playground.reactive.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.reactive.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebClientConfig {
	
	@Bean
	public ObjectMapper jacksonObjectMapper(){
		return new ObjectMapper();
	}
	
	@Bean
	public WebClient webClient(){
		WebClient webClient = WebClient.create(new ReactorClientHttpConnector());
		return webClient;
	}
	
}
