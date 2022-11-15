package com.example.webflux.controller;


import com.example.webflux.model.LiveScore;
import com.example.webflux.model.Message;
import com.example.webflux.service.LiveScoreHandler;
import com.example.webflux.service.WebFluxService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class WebFluxController {
    private final WebFluxService webFluxService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LiveScoreController.class);
    private final LiveScoreHandler handler;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping(value="/last/{name}")
    @ResponseBody
    public Flux<ServerSentEvent<Message>> getMessage(@PathVariable("name") String name){
        return Flux.create(serverSentEventFluxSink -> handler.subscribeMessage(serverSentEventFluxSink::next))
                .map(message -> ServerSentEvent.<Message>builder()
                        .data(webFluxService.getLastMessage(name))
//                        .event("goal")
                        .build());
    }

    @PostMapping("/send")
    @ResponseBody
    public Mono<Message> sendMessage(@RequestParam("name") String name, @RequestParam("body") String body){
        Message message = webFluxService.saveMessage(name, body);
        handler.publishMessage(message);
        return Mono.just(message);
    }
}
