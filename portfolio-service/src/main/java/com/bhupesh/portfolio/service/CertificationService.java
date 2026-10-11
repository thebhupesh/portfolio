package com.bhupesh.portfolio.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.Certification;
import com.bhupesh.portfolio.repository.CertificationRepository;

@Service 
public class CertificationService {

    private final CertificationRepository certificationRepository;

    public CertificationService(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    public List<Certification> fetchCertifications() {
        return certificationRepository.findAll();
    }

    public Optional<Certification> fetchCertificationById(String id) {
        return certificationRepository.findById(id);
    }

    public Certification createCertification(Certification certification) {
        certification.setType("certification");
        certification.setId(UUID.randomUUID().toString());
        certification.setUpdatedAt(Instant.now());
        certificationRepository.save(certification);

        return certification;
    }

    public Optional<Certification> updateCertification(String id, Certification certification) {
        if (certificationRepository.findById(id).isEmpty()) {
            return Optional.empty();
        }
        certification.setType("certification");
        certification.setId(id);
        certification.setUpdatedAt(Instant.now());
        certificationRepository.update(certification);

        return Optional.of(certification);
    }

    public boolean deleteCertification(String id) {
        if (certificationRepository.findById(id).isEmpty()) {
            return false;
        }

        certificationRepository.deleteById(id);
        return true;
    }
}
