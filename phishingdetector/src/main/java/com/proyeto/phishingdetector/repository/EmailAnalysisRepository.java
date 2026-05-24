package com.proyeto.phishingdetector.repository;

import com.proyeto.phishingdetector.model.EmailAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAnalysisRepository extends JpaRepository<EmailAnalysis, Long> {

}