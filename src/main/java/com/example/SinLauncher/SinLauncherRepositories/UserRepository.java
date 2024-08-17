package com.example.SinLauncher.SinLauncherRepositories;

import com.example.SinLauncher.SinLauncherEntites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Allows for the service layer to interact with the database
    // which allows the controller to interact with the service layer
}
