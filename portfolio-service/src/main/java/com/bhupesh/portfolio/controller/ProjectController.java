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
import com.bhupesh.portfolio.model.Project;
import com.bhupesh.portfolio.service.ProjectService;
import com.bhupesh.portfolio.validation.RequestValidator;

@RestController 
@RequestMapping("/v1/project")
public class ProjectController {

    private final ProjectService projectService;
    private final RequestValidator requestValidator;

    public ProjectController(ProjectService projectService, RequestValidator requestValidator) {
        this.projectService = projectService;
        this.requestValidator = requestValidator;
    }

    @GetMapping
    ResponseEntity<Response<List<Project>>> getProjects() {
        List<Project> projects = projectService.fetchProjects();
        if (projects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(Response.<List<Project>>builder()
                .success(true)
                .data(projects)
                .count(projects.size())
                .build());
    }

    @GetMapping("/{id}")
    ResponseEntity<Response<Project>> getProjectById(@PathVariable String id) {
        return projectService.fetchProjectById(id)
                .map(project -> ResponseEntity.ok(Response.<Project>builder()
                        .success(true)
                        .data(project)
                        .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Response<?>> createProject(@RequestBody Project project) {
        List<String> errors = requestValidator.validate(project);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(requestValidator.errorResponse("Validation failed", errors));
        }

        Project createdProject = projectService.createProject(project);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(createdProject.getId())
                        .toUri())
                .body(Response.<Project>builder()
                        .success(true)
                        .message("Project created successfully")
                        .data(createdProject)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateProject(
            @PathVariable String id, @RequestBody Project project) {
        List<String> errors = requestValidator.validate(project);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(requestValidator.errorResponse("Validation failed", errors));
        }

        return projectService.updateProject(id, project)
                .<ResponseEntity<Response<?>>>map(updatedProject -> ResponseEntity.ok(
                        Response.<Project>builder()
                        .success(true)
                        .message("Project updated successfully")
                        .data(updatedProject)
                        .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        if (!projectService.deleteProject(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
