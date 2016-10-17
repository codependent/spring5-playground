package com.codependent.spring5.playground;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import rx.Single;

@Test
public class ParallellizationTests {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private String url = "https://www.google.es/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=%s";

	private Flux<String> flux = Flux.<String>just("Spring", "Hibernate", "rxjava", "reactor-core", "reactive streams", "rxjava 2", "java 8", "stream vs observable", 
												  "reactor-core vs rxjava", "akka vs rxjava");
	
	private Flux<String> flux2 = Flux.<String>just("red", "white", "blue", "black", "yellow", "brown", "1", "2", "3", "4", "5");
	
	/**
	 * Ejecuta las peticiones en serie
	 */
	public void testBlockingIO(){
		final long start = System.currentTimeMillis();
		flux
		.map( value -> request(url, value))
		.doOnNext( val -> logger.info("GOT [{}]", val) )
		.doOnComplete(() -> logger.info("TOTAL TIME -> {}ms", (System.currentTimeMillis()-start ) ))
		.collectList()
		.subscribe();
	}
	
	/**
	 * Ejecuta las peticiones en serie
	 */
	public void testBlockingIO2(){
		long start = System.currentTimeMillis();
		flux
		.flatMap( value -> requestMono(url, value))
		.doOnNext( val -> logger.info("GOT [{}]", val) )
		.doOnComplete(() -> logger.info("TOTAL TIME -> {}ms", (System.currentTimeMillis()-start ) ))
		.collectList()
		.subscribe( res -> logger.info("{}", res));

	}
	
	/**
	 * Este ejemplo muestra otro mal uso por el que no se paraleliza. El Flux principal se procesa en otro hilo (parallel-1) para cada elemento,
	 * la llamada requestMono del flatMap se ejecuta en ese mismo parallel-1 y los resultados se reciben en otros hilos. PERO, la petición HTTP
	 * se realiza antes de onNext(), bloqueando parallel-1 
	 */
	public void testBlocking3IO() throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(1);
		long start = System.currentTimeMillis();
		flux
		.log()
		.flatMap( value -> requestMono(url, value)
				.subscribeOn(Schedulers.parallel()))
		.doOnNext( val -> logger.info("GOT [{}]", val) )
		.doOnComplete(() -> {
			logger.info("TOTAL TIME -> {}ms", (System.currentTimeMillis()-start ) );
			latch.countDown();
		})
		.subscribeOn(Schedulers.parallel())
		.collectList()
		.subscribe( res -> logger.info("{}", res));

		latch.await();
	}
	
	/**
	 * Descompone el ejemplo de testBlocking3IO para ver más claramente por qué no se paraleliza
	 */
	public void testBlocking4IO() throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(1);
		long start = System.currentTimeMillis();
		flux
		.log()
		.flatMap( value -> {
		     Mono<HttpStatus> mono = Mono.just(request(url, value));
		     mono = mono.subscribeOn(Schedulers.parallel());
		     return mono;
		})
		.doOnNext( val -> logger.info("GOT [{}]", val) )
		.doOnComplete(() -> {
			logger.info("TOTAL TIME -> {}ms", (System.currentTimeMillis()-start ) );
			latch.countDown();
		})
		.subscribeOn(Schedulers.parallel())
		.collectList()
		.subscribe( res -> logger.info("{}", res));

		latch.await();
	}
	
	/**
	 * IMPORTANTE defer tampoco paraleliza, proporciona una factoría de Mono al que se realizará las suscripción (evaluando 
	 * la petición HTTP)
	 */
	public void testBlocking5IO() throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(1);
		long start = System.currentTimeMillis();
		flux
		.log()
		.flatMap( value -> {
			logger.info("Processing flatMap for [{}]", value);
			return requestMonoDefer(url, value);})
		.doOnNext( val -> logger.info("GOT [{}]", val) )
		.doOnComplete(() -> {
			logger.info("TOTAL TIME -> {}ms", (System.currentTimeMillis()-start ) );
			latch.countDown();
		})
		.collectList()
		.subscribe( res -> logger.info("{}", res));
		
		latch.await();
	}
	
	/**
	 * Paralelización correcta, el flatMap ejecuta sobre main PERO la invocación está diferida
	 */
	public void testAsyncIO() throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(1);
		long start = System.currentTimeMillis();
		flux
		.log()
		.flatMap( value -> {
			logger.info("Processing flatMap for [{}]", value);
			return requestMonoFromCallable(url, value);})
		.doOnNext( val -> logger.info("GOT [{}]", val) )
		.doOnComplete(() -> {
			logger.info("TOTAL TIME -> {}ms", (System.currentTimeMillis()-start ) );
			latch.countDown();
		})
		.collectList()
		.subscribe( res -> logger.info("{}", res));
		
		latch.await();
	}
	

	
	public void testSerial() throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(12);
		long start = System.currentTimeMillis();
		flux2
		.log()
		.flatMap( value -> {
			return Flux.just(value, value+"fm", value+"fm2", value+"fm3", value+"fm4", value+"fm5", value+"fm6")
				.doOnNext( val -> logger.info(" VAL -> [{}]", val)) 
				.doOnComplete( 
						() -> latch.countDown())
				.subscribeOn(Schedulers.elastic());
		}, 1)
		.doOnComplete(() -> {
			logger.info("TOTAL TIME -> {}ms", (System.currentTimeMillis()-start ) );
			latch.countDown();
		})
		.subscribe( res -> logger.info("SUSC {}", res));
		
		latch.await();
	}
	
	public void testParallel() throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(12);
		long start = System.currentTimeMillis();
		flux2
		.log()
		.flatMap( value -> {
			return Flux.just(value, value+"fm", value+"fm2", value+"fm3", value+"fm4", value+"fm5", value+"fm6")
				.doOnNext( val -> logger.info(" VAL -> [{}]", val)) 
				.doOnComplete( 
						() -> {
							logger.info("flux from flatMap onComplete()");
							latch.countDown();
						})
				.subscribeOn(Schedulers.elastic());
		}, 11)
		.doOnComplete(() -> {
			logger.info("TOTAL TIME -> {}ms", (System.currentTimeMillis()-start ) );
			latch.countDown();
		})
		.subscribe( res -> logger.info("SUSC {}", res));
		
		latch.await();
	}
	
	public void testMonoDefer() throws InterruptedException{
		Mono<HttpStatus> mono = Mono
			.defer( () -> {
				logger.info("Preparing Mono");
				return requestMono(url, "rxjava");
			})
			.log();
		
		logger.info("Before subscribing");
		mono.subscribe( value -> logger.info("{}",value));

	}
	
	public void testSingleDefer() throws InterruptedException{
		Single<HttpStatus> observable = Single.defer( () -> {
			logger.info("Preparing Observable");
			return Single.just(request(url, "rxjava"));
		}).doOnSubscribe(() -> logger.info("Subscribing"));
		
		logger.info("Before subscribing");
		observable.subscribe( value -> logger.info("{}",value));

	}
	
	public void testMonoFromCallable() throws InterruptedException{
		Mono<HttpStatus> mono = Mono
			.fromCallable( () -> {
				logger.info("Preparing Mono");
				return request(url, "rxjava");
			})
			.log();
		
		logger.info("Before subscribing");
		mono.subscribe( value -> logger.info("{}",value));
	
	}
	
	private HttpStatus request(String urlTemplate, String value) {
		logger.info("REQUESTING");
		return restTemplate.getForEntity(String.format(urlTemplate, value), String.class, value).getStatusCode();
	}
	
	private Mono<HttpStatus> requestMono(String urlTemplate, String value) {
		return Mono.just(request(urlTemplate, value));
	}
	
	private Mono<HttpStatus> requestMonoFromCallable(String urlTemplate, String value) {
		return Mono.fromCallable( () -> request(urlTemplate, value) )
				.subscribeOn(Schedulers.parallel());
	}	
	
	private Mono<HttpStatus> requestMonoDefer(String urlTemplate, String value) {
		return Mono.defer( () -> 
			Mono.just(request(urlTemplate, value))
				.subscribeOn(Schedulers.parallel()) 
		);
	}	
}
