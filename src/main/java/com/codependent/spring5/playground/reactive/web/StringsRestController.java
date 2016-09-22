package com.codependent.spring5.playground.reactive.web;

import java.time.Duration;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.codec.ServerSentEvent;
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
        return new StringsPublisher(5);
	}
	
	@RequestMapping("/strings/sse/event")
    Flux<ServerSentEvent<String>> sse() {
        return Flux.interval(Duration.ofMillis(100)).map(l -> {
        	ServerSentEvent<String> event = ServerSentEvent.builder("foo").build();
            return event;
        }).take(2);
    }
	
	class StringsPublisher implements Publisher<String>{

		private int numberOfStrings;
		
		public StringsPublisher(int numberOfStrings){
			this.numberOfStrings = numberOfStrings;
		}
		
		@Override
		public void subscribe(Subscriber<? super String> s) {
			s.onSubscribe(new StringsSubscription(s));
		}
		
		class StringsSubscription implements Subscription{
			
			private Subscriber<? super String> subscriber;
			
			public StringsSubscription(Subscriber<? super String> s){
				this.subscriber = s;
			}
			
			@Override
			public void request(long n) {
				if(numberOfStrings>0){
					boolean completed = false;
					for (long i = 0; i < n && !completed; i++) {
						System.out.println(numberOfStrings);
						if(numberOfStrings-- > 0){
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							subscriber.onNext("message"+Math.random());		
						}
						if(numberOfStrings <= 0){
							subscriber.onComplete();
							completed = true;
						}
					}
				}
			}

			@Override
			public void cancel() {
				numberOfStrings = 0;
			}
		}
	}
	
	class StringsSubscriber implements Subscriber<String>{

		private Subscription subscription;
		
		@Override
		public void onSubscribe(Subscription s) {
			this.subscription = s;
			//s.request(Long.MAX_VALUE);
		}

		@Override
		public void onNext(String t) {
			System.out.println(t);
		}

		@Override
		public void onError(Throwable t) {
			t.printStackTrace();
		}

		@Override
		public void onComplete() {
			System.out.println("onComplete");
		}
		
		public void request(long n){
			subscription.request(n);
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		StringsRestController rsc = new StringsRestController();
		
		Publisher<String> publisher = rsc.new StringsPublisher(2);
		StringsSubscriber subscriber = rsc.new StringsSubscriber();
		publisher.subscribe(subscriber);
		
		subscriber.request(4);
		subscriber.request(1);
		subscriber.request(1);
		
		Thread.sleep(5000);
	}
}
