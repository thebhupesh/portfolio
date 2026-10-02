package com.bhupesh.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.ExperienceModel;
import com.bhupesh.portfolio.repository.ExperienceRepository;

@Service 
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public List<ExperienceModel> fetchExperiences() {
        return experienceRepository.findAll();
    }

    public ExperienceModel fetchExperienceById(String id) {
        return experienceRepository.findById(id);
    }
}
