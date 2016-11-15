package com.codependent.spring5.playground.reactive.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.reactive.ClientRequest;
import org.springframework.web.client.reactive.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Component
public class RandomNumbersServiceClient {

	@Autowired
	private WebClient webClient;
	
	@Autowired
	private ObjectMapper jacksonObjectMapper;
	
	public Flux<Double> getRandomNumbers(String serviceBaseUrl){
		ClientRequest<Void> request = ClientRequest.GET(serviceBaseUrl+"/randomNumbers")
				.accept(MediaType.APPLICATION_JSON).build();
		Flux<Double> flux = webClient.retrieveFlux(request, Double.class);
		return flux;
	}
	
	public Flux<Double> getRandomNumbersStreaming(String serviceBaseUrl){
		ClientRequest<Void> request = ClientRequest.GET(serviceBaseUrl+"/randomNumbersStreaming")
				.accept(MediaType.TEXT_EVENT_STREAM).build();
		Flux<Double> response = webClient.retrieveFlux(request, Double.class);
		/*
				.perform(get(serviceBaseUrl+"/randomNumbersStreaming").header("Accept", "text/event-stream"))
				.extract(bodyStream(String.class))
				.map((e -> {
					try {
						e = e.substring(e.indexOf(":")+1);
						Double a = jacksonObjectMapper.readValue(e, Double.class);
						return a;
					} catch (Exception e1) {
						e1.printStackTrace();
						return null;
					}
					
				}));
				*/
		return response;
	}
	
}
