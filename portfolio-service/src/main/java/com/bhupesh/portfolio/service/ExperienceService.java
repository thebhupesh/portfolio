package com.bhupesh.portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.ExperienceModel;
import com.bhupesh.portfolio.repository.ExperienceRepository;

@Service 
public class ExperienceService {
    
    @Autowired 
    private ExperienceRepository experienceRepository;

    public List<ExperienceModel> fetchExperiences() {
        return experienceRepository.findAll();
    }

    public ExperienceModel fetchExperienceById(String id) {
        return experienceRepository.findById(id);
    }
}
