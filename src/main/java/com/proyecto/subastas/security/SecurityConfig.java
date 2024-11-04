package com.proyecto.subastas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .requestMatchers("/api/home/login", "/api/home/registro","/","/login","/signup", "/css/**", "/js/**", "/images/**","/feed").permitAll() // Permite acceso a login y registro
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login") // URL de la página de inicio de sesión personalizada
            .permitAll()
            .and()
            .logout()
            .permitAll();
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
