package com.bhupesh.portfolio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.dto.Response;
import com.bhupesh.portfolio.model.Project;
import com.bhupesh.portfolio.service.ProjectService;

@RestController 
@RequestMapping("/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    ResponseEntity<Response<List<Project>>> getProjects() {
        List<Project> projects = projectService.fetchProjects();

        if(projects == null || projects.isEmpty()) {
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
        Project project = projectService.fetchProjectById(id);

        if(project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Response.<Project>builder()
                .success(true)
                .data(project)
                .build());
    }

    //TODO: Add POST, PUT, DELETE endpoints for project management
}
