package com.bhupesh.portfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.model.CertificationModel;
import com.bhupesh.portfolio.service.CertificationService;

@RestController 
@RequestMapping("/v1/certification")
public class CertificationController {
    
    @Autowired 
    private CertificationService certificationService;

    @GetMapping("/")
    public ResponseEntity<List<CertificationModel>> getCertifications() {
        List<CertificationModel> certifications = certificationService.fetchCertifications();

        if(certifications == null || certifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(certifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificationModel> getCertificationById(String id) {
        CertificationModel certification = certificationService.fetchCertificationById(id);

        if(certification == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(certification);
    }
}
