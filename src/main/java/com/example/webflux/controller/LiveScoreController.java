package com.example.webflux.controller;

import com.example.webflux.model.LiveScore;
import com.example.webflux.service.LiveScoreHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/live/score")
@RequiredArgsConstructor
public class LiveScoreController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiveScoreController.class);

    private final LiveScoreHandler handler;

    @PostMapping("/live-scores")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Mono<LiveScore> send(@RequestBody LiveScore liveScore){
        LOGGER.info("Received '{}'", liveScore);
        handler.publish(liveScore);
        return Mono.just(liveScore);
    }

    @GetMapping(path="/live-scores", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<ServerSentEvent<Object>> consumer(){
        return Flux.create(serverSentEventFluxSink -> handler.subscribe(serverSentEventFluxSink::next))
                .map(liveScore -> ServerSentEvent.builder()
                        .data(liveScore)
                        .event("goal")
                        .build());
    }
}
