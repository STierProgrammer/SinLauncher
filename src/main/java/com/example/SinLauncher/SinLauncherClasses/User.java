package com.example.SinLauncher.SinLauncherClasses;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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


<<<<<<< HEAD
=======
    //pass in generatedUUID here in String format
    //UUID.randomUUID() generates a unique UUID
    public User(String generatedUUID, String username, String password, String email, boolean isLoggedIn) {
        this.uuid = generatedUUID + "--Sin";
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLoggedIn = isLoggedIn;
    }
>>>>>>> 1a72e5f13353cb336d1ddf5b585a09cd5b639e1c
}
