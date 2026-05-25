package com.proyeto.phishingdetector.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_analysis")
public class EmailAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String resultado;

    private Integer nivelRiesgo;

    private LocalDateTime fecha;

    private String source;

    @Column(nullable = false)
    private Boolean manualReview = false;
    

    public EmailAnalysis() {
    }

    public EmailAnalysis(String email, String resultado, Integer nivelRiesgo, LocalDateTime fecha) {
        this.email = email;
        this.resultado = resultado;
        this.nivelRiesgo = nivelRiesgo;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Integer getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(Integer nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getManualReview() {
        return manualReview;
    }

    public void setManualReview(Boolean manualReview) {
        this.manualReview = manualReview;
    }
}