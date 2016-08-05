package com.codependent.spring5.playground.reactive.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.reactive.WebClient;

@Configuration
public class WebClientConfig {
	
	@Bean
	public WebClient webClient(){
		WebClient webClient = new WebClient(new ReactorClientHttpConnector());
		return webClient;
	}
	
}
