package com.bhupesh.portfolio.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.bhupesh.portfolio.model.Certification;
import com.bhupesh.portfolio.model.Experience;
import com.bhupesh.portfolio.model.Project;
import com.bhupesh.portfolio.repository.CertificationRepository;
import com.bhupesh.portfolio.repository.ExperienceRepository;
import com.bhupesh.portfolio.repository.ProjectRepository;

class ModelUpdatedAtServiceTest {

    @Test
    void setsUpdatedAtForExperienceCreateAndUpdate() {
        ExperienceRepository repository = mock(ExperienceRepository.class);
        ExperienceService service = new ExperienceService(repository);
        Experience created = new Experience();

        service.createExperience(created);

        assertNotNull(created.getUpdatedAt());
        verify(repository).save(created);

        Experience update = new Experience();
        when(repository.findById("experience-id")).thenReturn(Optional.of(new Experience()));

        service.updateExperience("experience-id", update);

        assertNotNull(update.getUpdatedAt());
        verify(repository).update(update);
    }

    @Test
    void setsUpdatedAtForProjectCreateAndUpdate() {
        ProjectRepository repository = mock(ProjectRepository.class);
        ProjectService service = new ProjectService(repository);
        Project created = new Project();

        service.createProject(created);

        assertNotNull(created.getUpdatedAt());
        verify(repository).save(created);

        Project update = new Project();
        when(repository.findById("project-id")).thenReturn(Optional.of(new Project()));

        service.updateProject("project-id", update);

        assertNotNull(update.getUpdatedAt());
        verify(repository).update(update);
    }

    @Test
    void setsUpdatedAtForCertificationCreateAndUpdate() {
        CertificationRepository repository = mock(CertificationRepository.class);
        CertificationService service = new CertificationService(repository);
        Certification created = new Certification();

        service.createCertification(created);

        assertNotNull(created.getUpdatedAt());
        verify(repository).save(created);

        Certification update = new Certification();
        when(repository.findById("certification-id")).thenReturn(Optional.of(new Certification()));

        service.updateCertification("certification-id", update);

        assertNotNull(update.getUpdatedAt());
        verify(repository).update(update);
    }
}
