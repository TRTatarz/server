package com.example.backend.controller;

import com.example.backend.dto.ChatRequest;
import com.example.backend.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {
        String response = chatService.processChat(request);
        return ResponseEntity.ok(response);
    }
}
