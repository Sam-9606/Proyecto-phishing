package com.proyeto.phishingdetector.repository;

import com.proyeto.phishingdetector.model.EmailAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAnalysisRepository
        extends JpaRepository<EmailAnalysis, Long> {

    Optional<EmailAnalysis> findByEmail(String email);
}