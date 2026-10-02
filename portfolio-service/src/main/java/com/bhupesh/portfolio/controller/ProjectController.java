package com.bhupesh.portfolio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.model.ProjectModel;
import com.bhupesh.portfolio.service.ProjectService;

@RestController 
@RequestMapping("/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    ResponseEntity<List<ProjectModel>> getProjects() {
        List<ProjectModel> projects = projectService.fetchProjects();

        if(projects == null || projects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    ResponseEntity<ProjectModel> getProjectById(@PathVariable String id) {
        ProjectModel project = projectService.fetchProjectById(id);

        if(project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(project);
    }

    //TODO: Add POST, PUT, DELETE endpoints for project management
}
