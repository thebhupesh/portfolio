package com.bhupesh.portfolio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.model.CertificationModel;
import com.bhupesh.portfolio.service.CertificationService;

@RestController 
@RequestMapping("/v1/certification")
public class CertificationController {
    
    private final CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping
    public ResponseEntity<List<CertificationModel>> getCertifications() {
        List<CertificationModel> certifications = certificationService.fetchCertifications();

        if(certifications == null || certifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(certifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificationModel> getCertificationById(@PathVariable String id) {
        CertificationModel certification = certificationService.fetchCertificationById(id);

        if(certification == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(certification);
    }

    //TODO: Add POST, PUT, DELETE endpoints for project management
}
