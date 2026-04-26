package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.ChatCharacter;

@Repository
public interface CharacterRepository extends JpaRepository<ChatCharacter, Long> {
    ChatCharacter findBySessionId(String sessionId);
}