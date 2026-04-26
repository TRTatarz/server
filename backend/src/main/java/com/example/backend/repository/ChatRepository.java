package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.ChatLog;

@Repository
public interface ChatRepository extends JpaRepository<ChatLog, Long> {

    List<ChatLog> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    void deleteById(Long id);
}