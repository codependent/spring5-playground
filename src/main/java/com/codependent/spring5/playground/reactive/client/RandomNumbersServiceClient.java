package com.codependent.spring5.playground.reactive.client;

import static org.springframework.web.client.reactive.ClientWebRequestBuilders.get;
import static org.springframework.web.client.reactive.ResponseExtractors.bodyStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.reactive.WebClient;

import reactor.core.publisher.Flux;

@Component
public class RandomNumbersServiceClient {

	@Autowired
	private WebClient webClient;
	
	public Flux<Double> getRandomNumbers(String serviceUrl){
		Flux<Double> response = webClient
				.perform(get("http://localhost:8080/randomNumbers").accept("application/json"))
				.extract(bodyStream(Double.class));
		return response;
	}
	
}
