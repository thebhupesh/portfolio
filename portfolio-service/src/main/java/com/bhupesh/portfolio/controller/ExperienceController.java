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
import com.bhupesh.portfolio.model.Experience;
import com.bhupesh.portfolio.service.ExperienceService;
import com.bhupesh.portfolio.validation.RequestValidator;

@RestController 
@RequestMapping("/v1/experience")
public class ExperienceController {
    
    private final ExperienceService experienceService;
    private final RequestValidator requestValidator;

    public ExperienceController(ExperienceService experienceService, RequestValidator requestValidator) {
        this.experienceService = experienceService;
        this.requestValidator = requestValidator;
    }

    @GetMapping
    public ResponseEntity<Response<List<Experience>>> getExperiences() {
        List<Experience> experiences = experienceService.fetchExperiences();
        if (experiences.isEmpty()) {
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
        return experienceService.fetchExperienceById(id)
                .map(experience -> ResponseEntity.ok(Response.<Experience>builder()
                        .success(true)
                        .data(experience)
                        .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Response<?>> createExperience(@RequestBody Experience experience) {
        List<String> errors = requestValidator.validate(experience);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(requestValidator.errorResponse("Validation failed", errors));
        }

        Experience createdExperience = experienceService.createExperience(experience);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(createdExperience.getId())
                        .toUri())
                .body(Response.<Experience>builder()
                        .success(true)
                        .message("Experience created successfully")
                        .data(createdExperience)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateExperience(@PathVariable String id, @RequestBody Experience experience) {
        List<String> errors = requestValidator.validate(experience);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(requestValidator.errorResponse("Validation failed", errors));
        }

        return experienceService.updateExperience(id, experience)
                .<ResponseEntity<Response<?>>>map(updatedExperience -> ResponseEntity.ok(
                        Response.<Experience>builder()
                        .success(true)
                        .message("Experience updated successfully")
                        .data(updatedExperience)
                        .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable String id) {
        if (!experienceService.deleteExperience(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
