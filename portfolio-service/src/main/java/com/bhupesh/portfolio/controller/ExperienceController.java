package com.bhupesh.portfolio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.dto.Response;
import com.bhupesh.portfolio.model.Experience;
import com.bhupesh.portfolio.service.ExperienceService;

@RestController 
@RequestMapping("/v1/experience")
public class ExperienceController {
    
    private final ExperienceService experienceService;

    private ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    public ResponseEntity<Response<List<Experience>>> getExperiences() {
        List<Experience> experiences = experienceService.fetchExperiences();

        if(experiences == null || experiences.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(Response.<List<Experience>>builder()
                .success(true)
                .data(experiences)
                .count(experiences.size())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Experience>> getExperienceById(@PathVariable String id) {
        Experience experience = experienceService.fetchExperienceById(id);

        if(experience == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Response.<Experience>builder()
                .success(true)
                .data(experience)
                .build());
    }

    //TODO: Add POST, PUT, DELETE endpoints for experience management
}
