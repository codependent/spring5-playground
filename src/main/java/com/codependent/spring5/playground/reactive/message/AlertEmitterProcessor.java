package com.codependent.spring5.playground.reactive.message;

import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.codependent.spring5.playground.reactive.dto.Alert;

import reactor.core.publisher.EmitterProcessor;

@Component
public class AlertEmitterProcessor {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private EmitterProcessor<Alert> processor;
	
	public AlertEmitterProcessor(){
		processor = EmitterProcessor.<Alert>create();
	}
	
	public EmitterProcessor<Alert> getProcessor() {
		return processor;
	}
	
	public void subscribe(Subscriber<? super Alert> subscriber){
		logger.info("subscribe [{}]", subscriber);
		processor.subscribe(subscriber);
	}
	
	public void onNext(Alert alert){
		logger.info("onNext [{}]", alert);
		processor.onNext(alert);
	}
	
	public void onComplete(){
		logger.info("onComplete");
		processor.onComplete();
	}

	public void onError(Throwable t){
		logger.error("onError", t);
		processor.onError(t);
	}
	
}
