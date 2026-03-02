package com.example.backend.client;

import com.example.backend.dto.ChatRequest;
import com.example.backend.dto.OpenRouterResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class OpenRouterClient {

    private final RestTemplate restTemplate;

    @Value("${openrouter.api.key}")
    private String apiKey;

    public OpenRouterClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendChat(ChatRequest request) {

        if (request.getMessages() == null || request.getMessages().isEmpty()) {
            return "No messages provided.";
        }

        String url = "https://openrouter.ai/api/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "openai/gpt-oss-120b");
        body.put("messages", request.getMessages());

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<OpenRouterResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        OpenRouterResponse.class
                );

        return response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
