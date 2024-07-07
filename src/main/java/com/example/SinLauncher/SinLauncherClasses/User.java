package com.example.SinLauncher.SinLauncherClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    private String username;
    private String password;
    private String email;
    private boolean isLoggedIn;
}
