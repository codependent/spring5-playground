package com.codependent.spring5.playground.reactive.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.codec.SseEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codependent.spring5.playground.reactive.service.RandomNumbersService;

import reactor.core.publisher.Flux;

@RestController
public class RandomNumbersController {
	
	@Autowired
	@Qualifier("randomNumbersServiceImpl")
	private RandomNumbersService rsn;
	
	@Autowired
	@Qualifier("randomNumbersHttpServiceImpl")
	private RandomNumbersService rsn2;
	
	@GetMapping("/randomNumbers")
	public Flux<Double> getReactiveRandomNumbers() {
		return rsn.generateRandomNumbers(10, 500);
	}
	
	@GetMapping(value="/randomNumbersStreaming", headers="accept=text/event-stream")
	public Flux<Double> getReactiveRandomNumbersSse() {
		return rsn.generateRandomNumbers(10, 500);
	}
	
	@GetMapping("/randomNumbersClient")
	public Flux<Double> getReactiveRandomNumbersWithClient() {
		Flux<Double> randomNumbersFlux = rsn2.generateRandomNumbers(10, 500);
		return randomNumbersFlux.log();
	}
	
}
