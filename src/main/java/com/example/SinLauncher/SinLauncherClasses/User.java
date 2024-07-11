package com.example.SinLauncher.SinLauncherClasses;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User {

    @Id
    private String uuid;

    private String username;
    private String password;
    private String email;
    private boolean isLoggedIn;
    private boolean cracked;



    //pass in generatedUUID or legit UUID here in String format
    //UUID.randomUUID() generates a unique UUID with a very low chance for the same UUID
    public User(String uuid, String username, String password, String email, boolean cracked) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.cracked = cracked;
    }
}
