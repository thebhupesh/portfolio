package com.bhupesh.portfolio.service;

import java.util.List;

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

    public Project fetchProjectById(String id) {
        return projectRepository.findById(id);
    }
}
