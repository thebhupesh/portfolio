package com.bhupesh.portfolio.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.Experience;
import com.bhupesh.portfolio.repository.ExperienceRepository;

@Service 
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public List<Experience> fetchExperiences() {
        return experienceRepository.findAll();
    }

    public Optional<Experience> fetchExperienceById(String id) {
        return experienceRepository.findById(id);
    }

    public Experience createExperience(Experience experience) {
        experience.setType("experience");
        experience.setId(UUID.randomUUID().toString());
        experience.setUpdatedAt(Instant.now());
        experienceRepository.save(experience);

        return experience;
    }

    public Optional<Experience> updateExperience(String id, Experience experience) {
        if (experienceRepository.findById(id).isEmpty()) {
            return Optional.empty();
        }
        experience.setType("experience");
        experience.setId(id);
        experience.setUpdatedAt(Instant.now());
        experienceRepository.update(experience);

        return Optional.of(experience);
    }

    public boolean deleteExperience(String id) {
        if (experienceRepository.findById(id).isEmpty()) {
            return false;
        }

        experienceRepository.deleteById(id);

        return true;
    }
}
