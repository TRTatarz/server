package com.example.backend.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.backend.dto.ChatRequest;
import com.example.backend.dto.OpenRouterResponse;

@Component
public class OpenRouterClient {

    private final RestTemplate restTemplate;

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.model:openai/gpt-oss-120b}") 
    private String modelName;

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
        body.put("model", modelName);
        body.put("messages", request.getMessages());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {

            ResponseEntity<OpenRouterResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    OpenRouterResponse.class
            );

     
            OpenRouterResponse responseBody = response.getBody();
            if (responseBody != null && 
                responseBody.getChoices() != null && 
                !responseBody.getChoices().isEmpty()) {
                
                return responseBody.getChoices().get(0).getMessage().getContent();
            }

            return "AI returned an empty response.";

        } catch (RestClientException e) {
            return "Error calling AI: " + e.getMessage();
        }
    }
}