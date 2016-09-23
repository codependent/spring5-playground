package com.codependent.spring5.playground.reactive.client;

import static org.springframework.web.client.reactive.ClientWebRequestBuilders.get;
import static org.springframework.web.client.reactive.ResponseExtractors.bodyStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
		Flux<Double> response = webClient
				.perform(get(serviceBaseUrl+"/randomNumbers"))
				.extract(bodyStream(Double.class));
		return response;
	}
	
	public Flux<Double> getRandomNumbersStreaming(String serviceBaseUrl){
		Flux<Double> response = webClient
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
		return response;
	}
	
}
