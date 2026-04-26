package com.example.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.client.OpenRouterClient;
import com.example.backend.dto.ChatRequest;
import com.example.backend.entity.ChatCharacter;
import com.example.backend.entity.ChatLog;
import com.example.backend.repository.CharacterRepository;
import com.example.backend.repository.ChatRepository;


@Service
public class ChatService {

    private final OpenRouterClient openRouterClient;
    private final ChatRepository chatRepository; 
    private final CharacterRepository characterRepository;

    public ChatService(OpenRouterClient openRouterClient, ChatRepository chatRepository, CharacterRepository characterRepository) {
        this.openRouterClient = openRouterClient;
        this.chatRepository = chatRepository;
        this.characterRepository = characterRepository;
    }

    public String processChat(ChatRequest request, String sessionId) { 
            List<ChatRequest.Message> fullHistory = new ArrayList<>();

            
            ChatCharacter character = characterRepository.findBySessionId(sessionId);
            
            String systemContent = "You are an AI character.";
            if (character != null) {
                systemContent = "You are " + character.getName() + ". " +
                                "Your personality is: " + character.getPersonality() + ". " +
                                "Background info: " + character.getAdditionalDetails() + ". " +
                                "Always respond in character!";
            }

            ChatRequest.Message systemMsg = new ChatRequest.Message();
            systemMsg.setRole("system");
            systemMsg.setContent(systemContent);
            fullHistory.add(systemMsg);

            // --- Keep the rest of your logic ---
            List<ChatLog> previousMessages = chatRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
            for (ChatLog log : previousMessages) {
                ChatRequest.Message m = new ChatRequest.Message();
                m.setRole(log.getRole());
                m.setContent(log.getContent());
                fullHistory.add(m);
            }

            if (request.getMessages() != null && !request.getMessages().isEmpty()) {
                ChatRequest.Message lastUserMsg = request.getMessages().get(request.getMessages().size() - 1);
                fullHistory.add(lastUserMsg);
                saveToDb(sessionId, "user", lastUserMsg.getContent());
            }

            request.setMessages(fullHistory);
            String aiResponse = openRouterClient.sendChat(request);
            saveToDb(sessionId, "assistant", aiResponse);

            return aiResponse;
        }

    private void saveToDb(String sessionId, String role, String content) {
        ChatLog log = new ChatLog();
        log.setSessionId(sessionId); 
        log.setRole(role);
        log.setContent(content);
        chatRepository.save(log);
    }
    public List<ChatLog> getHistoryBySession(String sessionId) {
        return chatRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }

    @Transactional
    public void deleteMessage(Long id) {
        chatRepository.deleteById(id);
    }

    @Transactional
    public void updateMessage(Long id, String newContent) {
        chatRepository.findById(id).ifPresent(log -> {
            log.setContent(newContent);
            chatRepository.save(log);
        });
    }
    
}