package com.proyeto.phishingdetector.service;

import com.proyeto.phishingdetector.model.EmailAnalysis;
import com.proyeto.phishingdetector.repository.EmailAnalysisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailAnalysisService {

    private final EmailAnalysisRepository repository;

    public EmailAnalysisService(EmailAnalysisRepository repository) {
        this.repository = repository;
    }

    public List<EmailAnalysis> getAll() {
        return repository.findAll();
    }

    public EmailAnalysis analyzeAndSave(EmailAnalysis emailAnalysis) {

        String email = emailAnalysis.getEmail().toLowerCase();

        int riesgo = 0;

        // Reglas básicas
        if (email.contains("verify")) {
            riesgo += 2;
        }

        if (email.contains("security")) {
            riesgo += 2;
        }

        if (email.contains("support")) {
            riesgo += 1;
        }

        if (email.contains("bank")) {
            riesgo += 2;
        }

        if (email.contains("paypal")) {
            riesgo += 3;
        }

        // Resultado final
        String resultado;

        if (riesgo >= 5) {
            resultado = "Posible phishing";
        } else {
            resultado = "Seguro";
        }

        emailAnalysis.setNivelRiesgo(riesgo);
        emailAnalysis.setResultado(resultado);
        emailAnalysis.setFecha(LocalDateTime.now());

        return repository.save(emailAnalysis);
    }
}