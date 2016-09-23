package com.codependent.spring5.playground;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.codependent.spring5.playground.reactive.client.RandomNumbersServiceClient;
import com.codependent.spring5.playground.reactive.client.WebClientConfig;

import reactor.core.publisher.Flux;

@Test
@ContextConfiguration(classes={WebClientConfig.class, RandomNumbersServiceClient.class})
public class RandomNumbersServiceClientTest extends AbstractTestNGSpringContextTests{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RandomNumbersServiceClient client;
	
	public void testNumbersServiceClientTest() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux<Double> randomNumbers = client.getRandomNumbers("http://localhost:8080");
		randomNumbers.doOnComplete( () -> {
			latch.countDown();
		})
		.doOnError( (e) ->{
			e.printStackTrace();
		})
		.subscribe( (n) -> {
			logger.info("------------> GOT NUMBER {}", n);
		});
		
		latch.await();
	}
	
	public void testNumbersServiceClientStreamingTest() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux<Double> randomNumbers = client.getRandomNumbersStreaming("http://localhost:8080");
		randomNumbers.doOnComplete( () -> {
			latch.countDown();
		}).doOnNext( c -> {
			System.out.println("HERE");
		})
		.subscribe( (n) -> {
			logger.info("------------> GOT NUMBER {}", n);
		});
		
		latch.await();
	}
	
}
