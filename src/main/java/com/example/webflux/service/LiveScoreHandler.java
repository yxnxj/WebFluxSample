package com.example.webflux.service;

import com.example.webflux.model.LiveScore;
import com.example.webflux.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Service
public class LiveScoreHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiveScoreHandler.class);

    private final List<Consumer<LiveScore>> listeners = new CopyOnWriteArrayList<>();
    private final List<Consumer<Message>> messageListeners = new CopyOnWriteArrayList<>();

    public void subscribe(Consumer<LiveScore> listener){
        listeners.add(listener);
        LOGGER.info("New one added, total consumer: {}", listeners.size());
    }

    public void subscribeMessage(Consumer<Message> listener){
        messageListeners.add(listener);
        LOGGER.info("New one added, total consumer: {}", listeners.size());
    }

    public void publish(LiveScore liveScore){
        LOGGER.info("Processing live score : {}", liveScore);
        listeners.forEach(listener -> listener.accept(liveScore));
    }
    public void publishMessage(Message message){
        LOGGER.info("Processing Message : {}", message);
        messageListeners.forEach(listener -> listener.accept(message));
    }
}
