package com.example.webflux;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
class WebFluxApplicationTests {
    Logger logger = LoggerFactory.getLogger(WebFluxApplicationTests.class);
    @Test
    void contextLoads() {
        consumeServerSentEvent();
    }

    public void consumeServerSentEvent() {
        WebClient client = WebClient.create("http://localhost:8080/messages");
        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<ServerSentEvent<String>>() {};

        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/last?name=name1")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> logger.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data()),
                error -> logger.error("Error receiving SSE: {}", error),
                () -> logger.info("Completed!!!"));
    }
}
