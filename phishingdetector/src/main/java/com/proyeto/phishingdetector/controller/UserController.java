package com.proyeto.phishingdetector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyeto.phishingdetector.model.User;
import com.proyeto.phishingdetector.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        try {

            return ResponseEntity.ok(service.register(user));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllUsers() {

        return ResponseEntity.ok(service.getAllUsers());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        try {

            service.deleteUser(id);

            return ResponseEntity.ok("Usuario eliminado correctamente");

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    
    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {

        try {

            service.deleteUserByEmail(email);

            return ResponseEntity.ok("Usuario eliminado correctamente");

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long id,
            @RequestBody String newPassword) {

        try {

            return ResponseEntity.ok(
                    service.updatePassword(id, newPassword));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}