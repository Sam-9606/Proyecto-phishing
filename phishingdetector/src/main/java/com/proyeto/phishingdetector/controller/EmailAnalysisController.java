package com.proyeto.phishingdetector.controller;

import com.proyeto.phishingdetector.model.EmailAnalysis;
import com.proyeto.phishingdetector.service.EmailAnalysisService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin("*")
public class EmailAnalysisController {

    private final EmailAnalysisService service;

    public EmailAnalysisController(EmailAnalysisService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmailAnalysis> getAll() {
        return service.getAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(service.getById(id));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {

        try {

            service.deleteById(id);

            return ResponseEntity.ok("Análisis eliminado correctamente");

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody EmailAnalysis emailAnalysis) {

        try {

            return ResponseEntity.ok(service.update(id, emailAnalysis));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> analyzeEmail(@RequestBody EmailAnalysis emailAnalysis) {

        try {
            EmailAnalysis saved = service.analyzeAndSave(emailAnalysis);

            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}