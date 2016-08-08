package com.codependent.spring5.playground;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.codependent.spring5.playground.reactive.client.AccountsServiceClient;
import com.codependent.spring5.playground.reactive.client.WebClientConfig;
import com.codependent.spring5.playground.reactive.dto.Alert;

import reactor.core.publisher.Flux;

@Test
@ContextConfiguration(classes={WebClientConfig.class, AccountsServiceClient.class})
public class AccountsServiceClientTest extends AbstractTestNGSpringContextTests{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AccountsServiceClient client;
	
	public void testAccountAlertsClientTest() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux<Alert> alerts = client.getAccountAlerts("http://localhost:8080");
		alerts.doOnComplete( () -> {
			latch.countDown();
		}).subscribe( (n) -> {
			logger.info("------------> GOT ALERT {}", n);
		});
		
		latch.await();
	}
	
	public void testNumbersServiceClientStreamingTest() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux<Alert> alerts = client.getAccountAlertsStreaming("http://localhost:8080");
		alerts.doOnComplete( () -> {
			latch.countDown();
		}).subscribe( (n) -> {
			logger.info("------------> GOT ALERT {}", n);
		});
		
		latch.await();
	}
	
}
