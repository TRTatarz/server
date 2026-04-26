package com.example.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ChatRequest;
import com.example.backend.entity.ChatLog;
import com.example.backend.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public String sendMessage(
            @RequestBody ChatRequest request, 
            @RequestHeader(value = "X-Session-ID", defaultValue = "default-session") String sessionId
    ) {
        return chatService.processChat(request, sessionId);
    }

    @GetMapping("/{sessionId}")
    public List<ChatLog> getChatHistory(@PathVariable String sessionId) {
        return chatService.getHistoryBySession(sessionId);
    }

    @DeleteMapping("/message/{id}")
    public void deleteMessage(@PathVariable Long id) {
        chatService.deleteMessage(id);
    }

    @PutMapping("/message/{id}")
    public void updateMessage(@PathVariable Long id, @RequestBody String newContent) {
        chatService.updateMessage(id, newContent);
    }
}