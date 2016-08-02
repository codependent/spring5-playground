package com.codependent.spring5.playground.reactive.web;

import org.reactivestreams.Subscriber;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.codependent.spring5.playground.reactive.dto.Alert;

import reactor.core.publisher.Flux;

@RestController
public class AccountsController {
	
	@GetMapping("/accounts/alerts")
	public Flux<Alert> getAccountAlertsNoPathVariable() {
		return Flux.<Alert>just(new Alert((long)1, "Alert message"));
	}
	
	@GetMapping("/accounts/{id}/alerts")
	public Flux<Alert> getAccountAlertsWithPathVariable(@PathVariable Long id) {
		return Flux.<Alert>just(new Alert(id, "Alert message"));
	}
	
	@GetMapping("/accounts/alerts2")
	public Flux<Alert> getAsyncAlerts(){

		return new Flux<Alert>() {

			@Override
			public void subscribe(Subscriber<? super Alert> s) {
				int i = 0;
				while(++i <= 5){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					s.onNext(new Alert((long)1, "my message" + Math.random()));
				}
				s.onComplete();
			}
		};
	}
}
