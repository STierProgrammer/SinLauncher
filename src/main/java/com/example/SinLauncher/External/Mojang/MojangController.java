package com.example.SinLauncher.External.Mojang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "api/v1/mojang")
public class MojangController {
    private final MojangService mojangService;

    @Autowired
    public MojangController(MojangService mojangService) {
        this.mojangService = mojangService;
    }

    // Meant for internal use only to check if mc acc already exists for any user
    @GetMapping(path = "/uuid/{username}")
    public Mono<String> UUIDFromName(@PathVariable String username) {
        return mojangService.getUUUIDByUsername(username);
    }

}
