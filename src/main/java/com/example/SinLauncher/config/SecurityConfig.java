// SecurityConfig.java

package com.example.SinLauncher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    // Authentication
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails admin = User.withUsername("Anmol")
                .password(encoder.encode(("test1")))
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("John").username("Sad").username("Mad")
                .password(encoder.encode(("test2")))
                .roles("USER")
                .build();

        // For Testing
        return new InMemoryUserDetailsManager(admin, user);
    }

    // Authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
         * Cross Site Request Forgery (CSRF)
         * http.authorizeHttpRequests(authorize ->
         * authorize.anyRequest().authenticated())
         * .formLogin(Customizer.withDefaults())
         * .httpBasic(Customizer.withDefaults());
         * 
         * return http.build(); example code
         */

        return null;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
