package com.codependent.spring5.playground.reactive.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codependent.spring5.playground.reactive.dto.Alert;
import com.codependent.spring5.playground.reactive.service.AccountService;

import reactor.core.publisher.Flux;

@RestController
public class AccountsRestController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/accounts/{id}/alerts")
	public Flux<Alert> getAccountAlertsInHistory(@PathVariable Integer id, @DateTimeFormat(iso=ISO.DATE) @RequestParam Date from, 
			 @DateTimeFormat(iso=ISO.DATE) @RequestParam Date until) {
		return accountService.getAccountAlerts(id, from, until);
	}
	
	@GetMapping(value="/accounts/{id}/alerts/live", produces="text/event-stream")
	public Flux<Alert> getAccountAlertsStreaming(@PathVariable Integer id) {
		return accountService.getAccountAlertsStreaming(id);
	}
	/*
	@GetMapping(value="/accounts/alerts2", produces="text/event-stream")
	public Publisher<Alert> getAsyncAlerts(){

		return new Publisher<Alert>() {

			private int loops = 5;
			
			@Override
			public void subscribe(Subscriber<? super Alert> s) {
				
				s.onSubscribe(new Subscription() {
					
					public void request(long n) {
						for (long i = 0; i < n; i++) {
							if(loops-- > 0){
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								s.onNext(new Alert((long)1, "my message" + Math.random()));					
							}else{
								s.onComplete();
								i = n;
							}
						}
					}
					@Override
					public void cancel() {
						loops = 0;
					}
				});
			}
		};
	}*/
}

