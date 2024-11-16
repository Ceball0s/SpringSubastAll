package com.proyecto.subastas.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // React corre en el puerto 3000 por defecto
public class HomeController {

    @GetMapping("/api/hello")
    public String sayHello() {
        return "Hola desde Spring Boot!";
    }
}

