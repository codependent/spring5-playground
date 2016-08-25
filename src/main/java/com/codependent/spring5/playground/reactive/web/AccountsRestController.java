package com.codependent.spring5.playground.reactive.web;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.codependent.spring5.playground.reactive.dto.Alert;

import reactor.core.publisher.Flux;

@RestController
public class AccountsRestController {
	
	@GetMapping("/accounts/alerts")
	public Flux<Alert> getAccountAlertsNoPathVariable() {
		return Flux.range(1, 3)
			.map((Integer i) -> {
			   	return new Alert((long)i, "Alert message"+i);
			})
			.delayMillis(1000)
			.log();
	}
	
	@GetMapping(value="/accounts/alertsStreaming", produces="text/event-stream")
	public Flux<Alert> getAccountAlertsNoPathVariableStreaming() {
		return Flux.range(1, 3)
			.map((Integer i) -> {
		    	return new Alert((long)i, "Alert message"+i);
		    })
			.delayMillis(1000)
			.log();
	}
	
	@GetMapping("/accounts/{id}/alerts")
	public Flux<Alert> getAccountAlertsWithPathVariable(@PathVariable Long id) {
		return Flux.<Alert>just(new Alert(id, "Alert message"));
	}
	
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
	}
}
