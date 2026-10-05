package com.bhupesh.portfolio.service;

import java.util.List;

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

    public Certification fetchCertificationById(String id) {
        return certificationRepository.findById(id);
    }
}
