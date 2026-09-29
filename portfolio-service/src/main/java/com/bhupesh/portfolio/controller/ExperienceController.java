package com.bhupesh.portfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.model.ExperienceModel;
import com.bhupesh.portfolio.service.ExperienceService;

@RestController 
@RequestMapping("/v1/experience")
public class ExperienceController {
    
    @Autowired 
    private ExperienceService experienceService;

    @GetMapping("/")
    public ResponseEntity<List<ExperienceModel>> getExperiences() {
        List<ExperienceModel> experiences = experienceService.fetchExperiences();

        if(experiences == null || experiences.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceModel> getExperienceById(String id) {
        ExperienceModel experience = experienceService.fetchExperienceById(id);

        if(experience == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(experience);
    }

    //TODO: Add POST, PUT, DELETE endpoints for experience management
}
