package com.codependent.spring5.playground.reactive.web;

import java.time.Duration;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.codec.SseEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class StringsRestController {
		
	@GetMapping("/singleStrings")
    public Publisher<String> getSingleString(){
        return new Publisher<String>() {
			@Override
			public void subscribe(Subscriber<? super String> s) {
				
				s.onSubscribe(new Subscription() {

					@Override
					public void request(long n) {
						s.onNext("message"+Math.random());
						s.onComplete();
					}

					@Override
					public void cancel() {}
				});
			}
        };
	}
	
	@GetMapping(value="/strings", produces="text/event-stream")
    public Publisher<String> getStrings(){
        return new Publisher<String>() {
        	
        	private int loops = 5;
        	
			@Override
			public void subscribe(Subscriber<? super String> s) {
				
				s.onSubscribe(new Subscription() {
					@Override
					public void request(long n) {
						for (int i = 0; i < n; i++) {
							if(loops-- > 0){
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								s.onNext("message"+Math.random());							
							}else{
								s.onComplete();
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
	
	@RequestMapping("/sse/event")
    Flux<SseEvent> sse() {
        return Flux.interval(Duration.ofMillis(100)).map(l -> {
            SseEvent event = new SseEvent();
            event.setId(Long.toString(l));
            event.setData("foo");
            event.setComment("bar");
            return event;
        }).take(2);
    }
}
