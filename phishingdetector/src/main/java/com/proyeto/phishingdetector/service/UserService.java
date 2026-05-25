package com.proyeto.phishingdetector.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyeto.phishingdetector.model.User;
import com.proyeto.phishingdetector.repository.UserRepository;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public List<User> getAllUsers() {
        return repository.findAll();
    }
    
    public void deleteUser(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        repository.deleteById(id);
    }
    
    public User updatePassword(Long id, String newPassword) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));

        return repository.save(user);
    }
    
    public User register(User user) {

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }
}