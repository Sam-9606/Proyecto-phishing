package com.proyeto.phishingdetector.service;

import com.proyeto.phishingdetector.model.EmailAnalysis;
import com.proyeto.phishingdetector.repository.EmailAnalysisRepository;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

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

    public EmailAnalysis getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Análisis no encontrado"));
    }
    
    public void deleteById(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Análisis no encontrado");
        }

        repository.deleteById(id);
    }
    
    public EmailAnalysis update(Long id, EmailAnalysis updatedEmail) {

        EmailAnalysis existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Análisis no encontrado"));

        existing.setEmail(updatedEmail.getEmail());

        return analyzeAndSave(existing);
    }
    
    public EmailAnalysis analyzeAndSave(EmailAnalysis emailAnalysis) {

        if (emailAnalysis.getEmail() == null || emailAnalysis.getEmail().isBlank()) {
            throw new RuntimeException("El email no puede estar vacío");
        }

        String email = emailAnalysis.getEmail().toLowerCase();

        int riesgo = 0;

        // Regex básico de email válido
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (!Pattern.matches(regexEmail, email)) {
            riesgo += 4;
        }

        // Palabras sospechosas
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

        if (email.contains("login")) {
            riesgo += 2;
        }

        if (email.contains("update")) {
            riesgo += 2;
        }

        // Imitaciones comunes
        if (email.contains("paypa1")) {
            riesgo += 4;
        }

        if (email.contains("g00gle")) {
            riesgo += 4;
        }

        if (email.contains("micros0ft")) {
            riesgo += 4;
        }

        if (email.contains("arnazon")) {
            riesgo += 4;
        }

        // Muchos guiones
        long guiones = email.chars().filter(ch -> ch == '-').count();

        if (guiones >= 2) {
            riesgo += 2;
        }

        // Muchos números
        long numeros = email.chars().filter(Character::isDigit).count();

        if (numeros >= 4) {
            riesgo += 2;
        }

        // Muchos puntos
        long puntos = email.chars().filter(ch -> ch == '.').count();

        if (puntos >= 3) {
            riesgo += 1;
        }

        // Dominios sospechosos
        if (email.endsWith(".ru") ||
            email.endsWith(".xyz") ||
            email.endsWith(".tk") ||
            email.endsWith(".top")) {

            riesgo += 3;
        }
        
     // Dominios confiables
        if (email.endsWith("@gmail.com") ||
            email.endsWith("@outlook.com") ||
            email.endsWith("@hotmail.com") ||
            email.endsWith("@yahoo.com")) {

            riesgo -= 1;
        }
        
        //validacion de valor de riesgo
        if (riesgo < 0) {
            riesgo = 0;
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