package com.proyeto.phishingdetector.controller;

import com.proyeto.phishingdetector.model.EmailAnalysis;
import com.proyeto.phishingdetector.service.EmailAnalysisService;
import org.springframework.web.bind.annotation.*;

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
    public EmailAnalysis getById(@PathVariable Long id) {
        return service.getById(id);
    }
    
    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id) {

        service.deleteById(id);

        return "Análisis eliminado correctamente";
    }
    
    @PutMapping("/{id}")
    public EmailAnalysis update(
            @PathVariable Long id,
            @RequestBody EmailAnalysis emailAnalysis) {

        return service.update(id, emailAnalysis);
    }
    
    @PostMapping
    public EmailAnalysis analyzeEmail(@RequestBody EmailAnalysis emailAnalysis) {
        return service.analyzeAndSave(emailAnalysis);
    }
}