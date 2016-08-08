package com.codependent.spring5.playground.reactive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class RandomNumbersServiceImpl implements RandomNumbersService{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Non-blocking randon number generator
	 * @param amount - # of numbers to generate
	 * @param delay - delay between each number generation in milliseconds
	 * @return
	 */
	public Flux<Double> generateRandomNumbers(int amount, int delay){
		return Flux.range(1, amount)
				   .delayMillis(delay)
				   .map(i -> {
					   double random = Math.random();
					   logger.info("******* Generated [{}]", random);
					   return random;
				   }).log();
	}
	
}
