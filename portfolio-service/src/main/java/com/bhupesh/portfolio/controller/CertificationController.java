package com.bhupesh.portfolio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.dto.Response;
import com.bhupesh.portfolio.model.Certification;
import com.bhupesh.portfolio.service.CertificationService;

@RestController 
@RequestMapping("/v1/certification")
public class CertificationController {
    
    private final CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping
    public ResponseEntity<Response<List<Certification>>> getCertifications() {
        List<Certification> certifications = certificationService.fetchCertifications();

        if(certifications == null || certifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(Response.<List<Certification>>builder()
                .success(true)
                .data(certifications)
                .count(certifications.size())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Certification>> getCertificationById(@PathVariable String id) {
        Certification certification = certificationService.fetchCertificationById(id);

        if(certification == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Response.<Certification>builder()
                .success(true)
                .data(certification)
                .build());
    }
}
