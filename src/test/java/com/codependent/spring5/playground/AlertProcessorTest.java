package com.codependent.spring5.playground;

import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.codependent.spring5.playground.reactive.dto.Alert;
import com.codependent.spring5.playground.reactive.message.AlertEmitterProcessor;

import reactor.core.publisher.Flux;

@Test
public class AlertProcessorTest {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private AlertEmitterProcessor processor;
	
	@BeforeClass
	public void beforeClass(){
		this.processor = new AlertEmitterProcessor();
	}
	
	@Test
	public void testAlertEmmiterProcessor() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(2);
		Flux<Alert> testProcessor = processor.getProcessor().doOnComplete(() -> latch.countDown());
		
		processor.onNext(new Alert(1, (long)1, "Message"));
		processor.onNext(new Alert(2, (long)2, "Message"));
		
		testProcessor.subscribe(new AlertSubscriber("SUBSCRIBER1"));
		
		processor.onNext(new Alert(3, (long)3, "Message"));
		
		testProcessor.subscribe(new AlertSubscriber("SUBSCRIBER2"));
		
		processor.onNext(new Alert(4, (long)4, "Message"));
		
		processor.onComplete();
		
		latch.await();
	}
	
	public class AlertSubscriber implements Subscriber<Alert>{
		
		private String name;
		
		public AlertSubscriber(String name){
			this.name = name;
		}
		
		@Override
		public void onSubscribe(Subscription s) {
			logger.info("{} subscribing", name, s);
			s.request(Long.MAX_VALUE);
		}

		@Override
		public void onNext(Alert t) {
			logger.info("{} got [{}]", name, t);
		}

		@Override
		public void onError(Throwable t) {
			t.printStackTrace();
		}

		@Override
		public void onComplete() {
			logger.info("{} completed", name);
		}
	}
	
}
