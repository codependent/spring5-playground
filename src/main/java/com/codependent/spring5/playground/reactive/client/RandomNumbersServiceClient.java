package com.codependent.spring5.playground.reactive.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Component
public class RandomNumbersServiceClient {

	@Autowired
	private WebClient webClient;
	
	public Flux<Double> getRandomNumbers(String serviceBaseUrl){
		final ClientRequest<Void> request = ClientRequest.GET(serviceBaseUrl+"/randomNumbers")
				.accept(MediaType.TEXT_EVENT_STREAM).build();
		Flux<Double> flux = webClient
				.exchange(request)
				.flatMap( response -> response.bodyToFlux(Double.class))
			 	.log();
		return flux;
	}
	
	public Flux<Double> getRandomNumbersStreaming(String serviceBaseUrl){
		final ClientRequest<Void> request = ClientRequest.GET(serviceBaseUrl+"/randomNumbersStreaming")
				.accept(MediaType.TEXT_EVENT_STREAM).build();
		Flux<Double> flux = webClient
				.exchange(request)
				.flatMap( response -> response.bodyToFlux(Double.class))
			 	.log();
		return flux;
	}
	
}
