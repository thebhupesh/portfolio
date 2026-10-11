package com.bhupesh.portfolio.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.Project;
import com.bhupesh.portfolio.repository.ProjectRepository;

@Service 
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> fetchProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> fetchProjectById(String id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        project.setType("project");
        project.setId(UUID.randomUUID().toString());
        project.setUpdatedAt(Instant.now());
        projectRepository.save(project);

        return project;
    }

    public Optional<Project> updateProject(String id, Project project) {
        if (projectRepository.findById(id).isEmpty()) {
            return Optional.empty();
        }
        project.setType("project");
        project.setId(id);
        project.setUpdatedAt(Instant.now());
        projectRepository.update(project);

        return Optional.of(project);
    }

    public boolean deleteProject(String id) {
        if (projectRepository.findById(id).isEmpty()) {
            return false;
        }

        projectRepository.deleteById(id);

        return true;
    }
}
