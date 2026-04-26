package com.example.backend.repository;

import java.util.List; // Add this import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.ChatCharacter;

@Repository
public interface CharacterRepository extends JpaRepository<ChatCharacter, Long> {
    List<ChatCharacter> findByUserId(Long userId);

    ChatCharacter findBySessionId(String sessionId);
}