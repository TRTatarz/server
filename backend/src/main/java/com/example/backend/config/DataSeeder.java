package com.example.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.backend.entity.ChatCharacter;
import com.example.backend.repository.CharacterRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(CharacterRepository repository) {
        return args -> {
            // Check if data already exists to avoid duplicates
            if (repository.count() == 0) {
                ChatCharacter mockAi = new ChatCharacter();
                mockAi.setName("Beta-Zero");
                mockAi.setCreatorName("System");
                mockAi.setUserId(1L); // Assign to your main user ID
                mockAi.setAvatarUrl("https://api.dicebear.com/7.x/bottts/svg?seed=Beta");
                mockAi.setPersonality("Helpful, technical, and slightly witty AI assistant.");
                mockAi.setGreeting("Hello Tatar! I am Beta-Zero. How can I help with your code today?");
                mockAi.setSessionId("mock-session-001");
                mockAi.setHasMessages(false); // Sidebar hidden until first talk

                repository.save(mockAi);
                System.out.println("Successfully seeded Mock AI Character: " + mockAi.getName());
            }
        };
    }
}