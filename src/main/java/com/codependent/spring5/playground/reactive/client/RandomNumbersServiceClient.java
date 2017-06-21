package com.codependent.spring5.playground.reactive.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Component
public class RandomNumbersServiceClient {

	@Autowired
	private WebClient webClient;
	
	public Flux<Double> getRandomNumbers(String serviceBaseUrl){
		Flux<Double> flux = webClient
				.get()
				.uri(serviceBaseUrl+"/randomNumbers")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.flatMapMany( response -> response.bodyToFlux(Double.class))
			 	.log();
		return flux;
	}
	
	public Flux<Double> getRandomNumbersStreaming(String serviceBaseUrl){
		Flux<Double> flux = webClient
				.get()
				.uri(serviceBaseUrl+"/randomNumbersStreaming")
				.accept(MediaType.TEXT_EVENT_STREAM)
				.exchange()
				.flatMapMany( response -> response.bodyToFlux(Double.class))
			 	.log();
		return flux;
	}
	
}
