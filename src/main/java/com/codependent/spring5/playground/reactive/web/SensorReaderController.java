package com.codependent.spring5.playground.reactive.web;

import org.reactivestreams.Subscriber;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.codependent.spring5.playground.reactive.dto.SensorRead;

import reactor.core.publisher.Flux;

@RestController
public class SensorReaderController {

	@GetMapping("/sensors/{sensorId}/values")
	public Flux<SensorRead> getSensorRead(@PathVariable String sensorId){
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
					s.onNext(new SensorRead(sensorId, Math.random()));
				}
				s.onComplete();
			}
		};
	}
}
