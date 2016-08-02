package com.codependent.spring5.playground.reactive.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.codependent.spring5.playground.reactive.dto.Alert;

import reactor.core.publisher.Flux;

@RestController
public class AccountsController {
	
	@GetMapping("/accounts/alerts")
	public Flux<Alert> getAccountAlertsNoPathVariable(/*@PathVariable Long id*/) {
		return Flux.<Alert>just(new Alert((long)1, "Alert message"));
	}
	
	@GetMapping("/accounts/{id}/alerts")
	public Flux<Alert> getAccountAlertsWithPathVariable(@PathVariable Long id) {
		return Flux.<Alert>just(new Alert((long)1, "Alert message"));
	}
	
	/*@GetMapping("/accounts/{id}/alerts")
	public Flux<SensorRead> getSensorRead(){
		//return Flux.<SensorRead>just(new SensorRead("asdf", Math.random()));
		return new Flux<SensorRead>() {

			@Override
			public void subscribe(Subscriber<? super SensorRead> s) {
				int i = 0;
				while(++i <= 5){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					s.onNext(new SensorRead("asdf", Math.random()));
				}
				s.onComplete();
			}
		};
	}*/
}
