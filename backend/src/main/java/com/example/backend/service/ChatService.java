package com.example.backend.service;

import com.example.backend.client.OpenRouterClient;
import com.example.backend.dto.ChatRequest;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final OpenRouterClient openRouterClient;

    public ChatService(OpenRouterClient openRouterClient) {
        this.openRouterClient = openRouterClient;
        System.out.println("Calling OpenRouter...");
    }

    public String processChat(ChatRequest request) {
        return openRouterClient.sendChat(request);
    }
}
