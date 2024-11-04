package com.proyecto.subastas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // Asegúrate de que tienes un archivo 'index.html' en templates
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Asegúrate de que tienes un archivo 'index.html' en templates
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/feed")
    public String feed(){
        return "feed";
    }
}
