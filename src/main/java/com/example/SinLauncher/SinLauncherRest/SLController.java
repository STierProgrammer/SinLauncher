package com.example.SinLauncher.SinLauncherRest;

import com.example.SinLauncher.External.Mojang.MojangController;
import com.example.SinLauncher.External.Mojang.MojangService;
import com.example.SinLauncher.SinLauncherClasses.POJOClasses.CreateAccountRequest;
import com.example.SinLauncher.SinLauncherClasses.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/sin")
public class SLController {

    private final SLService slService;
    private final MojangService mojangService;
    private final MojangController mojangController;

    @Autowired
    public SLController(SLService slService, MojangService mojangService, MojangController mojangController) {
        this.slService = slService;
        this.mojangService = mojangService;
        this.mojangController = mojangController;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return slService.getAllUsers();
    }

    @PostMapping(path = "/adduser")
    public void addUser(@RequestBody User user) {
        //user is valid code must check through if user is valid or not
        //non-valid accounts cannot be created
        slService.addUser(user);
    }

    //creates an account with either legit or cracked Minecraft indicated by cracked boolean
    @PostMapping(path = "/create-account")
    public void createAccount(@RequestBody CreateAccountRequest request) {
        slService.createAccount(request.getUsername(), request.getPassword(), request.getEmail());
    }
}
