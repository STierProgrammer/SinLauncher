package com.example.SinLauncher.config;

import com.example.SinLauncher.SinLauncherClasses.User;
import com.example.SinLauncher.SinLauncherRepositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class Testing {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        //just to check db
        return args -> {
            UUID uuid = UUID.randomUUID();
            User user = new User(uuid.toString(), "ICantSnipe", "testPass", "test123321@gmail.com", true);
            user.setLoggedIn(false);
            userRepository.saveAll(List.of(user));
        };
    }
}
