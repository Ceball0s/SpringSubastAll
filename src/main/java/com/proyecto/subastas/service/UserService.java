package com.proyecto.subastas.service;

import com.proyecto.subastas.model.User;
import com.proyecto.subastas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptar la contraseña
        return userRepository.save(user);
    }

    public User findByCorreoElectronico(String correoElectronico) {
        return userRepository.findByCorreoElectronico(correoElectronico).orElse(null);
    }
}
