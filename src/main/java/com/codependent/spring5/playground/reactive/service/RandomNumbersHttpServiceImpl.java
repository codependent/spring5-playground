package com.codependent.spring5.playground.reactive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codependent.spring5.playground.reactive.client.RandomNumbersServiceClient;

import reactor.core.publisher.Flux;

@Service
public class RandomNumbersHttpServiceImpl implements RandomNumbersService{

	@Autowired
	private RandomNumbersServiceClient rns;
	
	public Flux<Double> generateRandomNumbers(int amount, int delay){
		return rns.getRandomNumbers("http://localhost:8080");
	}
	
	public Flux<Object> generateRandomNumbersStreaming(int amount, int delay){
		return rns.getRandomNumbersStreaming("http://localhost:8080");
	}
	
}
