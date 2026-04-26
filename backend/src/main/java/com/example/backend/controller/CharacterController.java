package com.example.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entity.ChatCharacter;
import com.example.backend.repository.CharacterRepository;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final CharacterRepository characterRepository;

    public CharacterController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @GetMapping
    public List<ChatCharacter> getAllCharacters(@RequestParam(required = false) Long userId) {
        if (userId != null) {
            return characterRepository.findByUserId(userId);
        }
        return characterRepository.findAll();
    }

    @PostMapping
    public ChatCharacter createCharacter(@RequestBody ChatCharacter character) {
        // Just save it—since you added userId to the Entity, 
        // it will be saved correctly if the frontend sends it.
        return characterRepository.save(character);
    }


    @PutMapping("/{id}")
    public ChatCharacter updateCharacter(@PathVariable Long id, @RequestBody ChatCharacter characterDetails) {
        return characterRepository.findById(id).map(character -> {
            character.setName(characterDetails.getName());
            character.setAvatarUrl(characterDetails.getAvatarUrl());
            character.setPersonality(characterDetails.getPersonality());
            character.setGreeting(characterDetails.getGreeting());
            
            // Ensure the userId stays with the character during updates
            if (characterDetails.getUserId() != null) {
                character.setUserId(characterDetails.getUserId());
            }

            return characterRepository.save(character);
        }).orElseThrow(() -> new RuntimeException("Character not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteCharacter(@PathVariable Long id) {
        characterRepository.deleteById(id);
    }

    @PatchMapping("/{id}/activate")
    public ChatCharacter activateCharacter(@PathVariable Long id) {
        return characterRepository.findById(id).map(character -> {
            character.setHasMessages(true);
            return characterRepository.save(character);
        }).orElseThrow(() -> new RuntimeException("Character not found"));
    }

    @PatchMapping("/{id}/clear-chat")
    public ChatCharacter clearChat(@PathVariable Long id) {
        return characterRepository.findById(id).map(character -> {
            character.setHasMessages(false);
            character.setSessionId("session-" + System.currentTimeMillis());
            return characterRepository.save(character);
        }).orElseThrow(() -> new RuntimeException("Character not found"));
    }
}