package com.example.webflux.repository;

import com.example.webflux.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findTopByNameOrderByIdDesc(String name);
}
