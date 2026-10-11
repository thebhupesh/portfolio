package com.bhupesh.portfolio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bhupesh.portfolio.dto.Response;
import com.bhupesh.portfolio.model.Certification;
import com.bhupesh.portfolio.service.CertificationService;
import com.bhupesh.portfolio.validation.RequestValidator;

@RestController 
@RequestMapping("/v1/certification")
public class CertificationController {
    
    private final CertificationService certificationService;
    private final RequestValidator requestValidator;

    public CertificationController(CertificationService certificationService, RequestValidator requestValidator) {
        this.certificationService = certificationService;
        this.requestValidator = requestValidator;
    }

    @GetMapping
    public ResponseEntity<Response<List<Certification>>> getCertifications() {
        List<Certification> certifications = certificationService.fetchCertifications();
        if (certifications.isEmpty()) {
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
        return certificationService.fetchCertificationById(id)
                .map(certification -> ResponseEntity.ok(Response.<Certification>builder()
                        .success(true)
                        .data(certification)
                        .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Response<?>> createCertification(@RequestBody Certification certification) {
        List<String> errors = requestValidator.validate(certification);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(requestValidator.errorResponse("Validation failed", errors));
        }

        Certification createdCertification = certificationService.createCertification(certification);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(createdCertification.getId())
                        .toUri())
                .body(Response.<Certification>builder()
                        .success(true)
                        .message("Certification created successfully")
                        .data(createdCertification)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateCertification(@PathVariable String id, @RequestBody Certification certification) {
        List<String> errors = requestValidator.validate(certification);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(requestValidator.errorResponse("Validation failed", errors));
        }

        return certificationService.updateCertification(id, certification)
                .<ResponseEntity<Response<?>>>map(updatedCertification -> ResponseEntity.ok(
                        Response.<Certification>builder()
                        .success(true)
                        .message("Certification updated successfully")
                        .data(updatedCertification)
                        .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable String id) {
        if (!certificationService.deleteCertification(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
