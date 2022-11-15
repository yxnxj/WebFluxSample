package com.example.webflux.service;

import com.example.webflux.model.Message;
import com.example.webflux.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class WebFluxService {
    private final MessageRepository messageRepository;
    public Flux<ServerSentEvent<Message>> getMessage(String name){


        Flux<ServerSentEvent<Message>> fluxMessage = Flux.interval(Duration.ofSeconds(1))
                .publishOn(Schedulers.boundedElastic())
                .map(sequence -> ServerSentEvent.<Message>builder()
                        .id(String.valueOf(sequence))
//                        .event("periodic-event")
                        .data(messageRepository.findTopByNameOrderByIdDesc(name))
                        .build());

        return fluxMessage;
    }

    public Message getLastMessage(String name) {
        Message message = messageRepository.findTopByNameOrderByIdDesc(name);
        return message;
    }

    public Message saveMessage(String name, String body) {
        return messageRepository.save(Message.builder().name(name).body(body).build());
    }
}
