package com.proyeto.phishingdetector.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyeto.phishingdetector.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
