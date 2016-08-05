package com.codependent.spring5.playground.reactive.web;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StringsController {
		
	@GetMapping("/singleStrings")
    public Publisher<String> getSingleString(){
        return new Publisher<String>() {
			@Override
			public void subscribe(Subscriber<? super String> s) {
				s.onNext("message"+Math.random());
				s.onComplete();
			}
        };
	}
	
	@GetMapping("/strings")
    public Publisher<String> getStrings(){
        return new Publisher<String>() {
			@Override
			public void subscribe(Subscriber<? super String> s) {
				int i = 0;
				while(++i <= 5){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					s.onNext("message"+Math.random());
				}
				s.onComplete();
			}
        };
	}
}
