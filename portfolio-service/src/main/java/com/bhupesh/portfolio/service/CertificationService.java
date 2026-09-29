package com.bhupesh.portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.CertificationModel;
import com.bhupesh.portfolio.repository.CertificationRepository;

@Service 
public class CertificationService {
    
    @Autowired 
    private CertificationRepository certificationRepository;

    public List<CertificationModel> fetchCertifications() {
        return certificationRepository.findAll();
    }

    public CertificationModel fetchCertificationById(String id) {
        return certificationRepository.findById(id);
    }
}
