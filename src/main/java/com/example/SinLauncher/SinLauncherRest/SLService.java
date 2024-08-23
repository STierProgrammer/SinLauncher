package com.example.SinLauncher.SinLauncherRest;

import com.example.SinLauncher.External.Mojang.MojangService;
import com.example.SinLauncher.SinLauncherEntites.User;
import com.example.SinLauncher.SinLauncherRepositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SLService {

    private final UserRepository userRepository;
    private final MojangService mojangService;

    @Autowired
    public SLService(UserRepository userRepository, MojangService mojangService) {
        this.userRepository = userRepository;
        this.mojangService = mojangService;
    }

    public void addUser(User user) {
        // A duplicate user cannot be created neither any user can have same name
        userRepository.save(user);
    }

    // Creates a Minecraft account on our database
    @Transactional
    public void createAccount(String username,String password, String email) {
        Mono<String> uuidMono = mojangService.getUUUIDByUsername(username);

        User user;

        Optional<String> monoOptional = uuidMono.blockOptional();
        if (monoOptional.isPresent()) 
            user = new User(monoOptional.get(), username, password, email, false, false);
    
        else {
            UUID uuid = UUID.randomUUID();
            String generatedUUID = uuid.toString();
            user = new User(generatedUUID, username, password, email, true, false);
        }
        
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
