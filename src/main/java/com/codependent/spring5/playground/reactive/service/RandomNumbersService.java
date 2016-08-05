package com.codependent.spring5.playground.reactive.service;

import reactor.core.publisher.Flux;

public interface RandomNumbersService {

	/**
	 * Non-blocking randon number generator
	 * @param amount - # of numbers to generate
	 * @param delay - delay between each number generation in milliseconds
	 * @return
	 */
	Flux<Double> generateRandomNumbers(int amount, int delay);
}
