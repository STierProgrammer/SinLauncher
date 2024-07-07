package com.example.SinLauncher.External.Mojang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MojangService {

    private final WebClient webClient;

    @Autowired
    public MojangService(WebClient.Builder webClientBuilder) { //Builds mojang api for this class using uri and constructor
        webClient = webClientBuilder.baseUrl("https://api.mojang.com").build();
    }

    public Mono<String> getUUUIDByUsername(String username) {
        return webClient.get()
                .uri("/users/profiles/minecraft/{username}", username)
                .retrieve()
                .bodyToMono(String.class);
        // finds user by username if they don't exist returns empty Mono<String>
    }
}
