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
    
    public void deleteUserByEmail(String email) {

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        repository.delete(user);
    }
    
    public User updatePassword(Long id, String newPassword) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));

        return repository.save(user);
    }
    
    public User updatePasswordByEmail(String email, String newPassword) {

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        if (newPassword == null || newPassword.isBlank()) {
            throw new RuntimeException("La nueva contraseña es obligatoria");
        }

        if (newPassword.length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        return repository.save(user);
    }
    
    public User register(User user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new RuntimeException("El username es obligatorio");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }

        if (!user.getEmail().contains("@")) {
            throw new RuntimeException("El email no es válido");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        if (user.getPassword().length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            throw new RuntimeException("El rol es obligatorio");
        }

        if (!user.getRole().equals("USER")
                && !user.getRole().equals("ADMIN")) {

            throw new RuntimeException("Rol inválido");
        }

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }
}