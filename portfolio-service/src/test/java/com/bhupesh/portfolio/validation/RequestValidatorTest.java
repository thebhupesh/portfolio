package com.bhupesh.portfolio.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.bhupesh.portfolio.message.Message;
import com.bhupesh.portfolio.model.Detail;
import com.bhupesh.portfolio.model.Experience;
import com.bhupesh.portfolio.model.Project;
import com.bhupesh.portfolio.model.common.Link;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

class RequestValidatorTest {

    @Test
    void returnsSortedValidationMessages() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            RequestValidator requestValidator = new RequestValidator(factory.getValidator());
            Experience experience = new Experience();

            assertEquals(List.of(
                    "company: Company is required",
                    "location: Location is required",
                    "role: Role is required",
                    "startDate: Start date is required"),
                    requestValidator.validate(experience));
        }
    }

    @Test
    void validatesNestedRequestObjects() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            RequestValidator requestValidator = new RequestValidator(factory.getValidator());
            Detail detail = validDetail();
            detail.setLinks(List.of(new Link("", "")));

            List<String> errors = requestValidator.validate(detail);

            assertEquals(2, errors.size());
            assertTrue(errors.contains("links[0].name: Link name is required"));
            assertTrue(errors.contains("links[0].url: Link URL is required"));
        }
    }

    @Test
    void acceptsValidClientFieldsWithoutServerManagedKeys() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            RequestValidator requestValidator = new RequestValidator(factory.getValidator());
            Project project = new Project();
            project.setName("Portfolio");
            project.setDescription("Project description");
            project.setLink(new Link("Preview", "https://example.com"));

            assertTrue(requestValidator.validate(project).isEmpty());
        }
    }

    @Test
    void acceptsDisposableEmailDomainsWhenEmailFormatIsValid() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            RequestValidator requestValidator = new RequestValidator(factory.getValidator());
            Message message = new Message();
            message.setSenderName("Name");
            message.setSenderEmail("person@sub.mailinator.com");
            message.setSenderContact("123456789");
            message.setSubject("Subject");
            message.setMessageBody("Message");

            assertTrue(requestValidator.validate(message).isEmpty());
        }
    }

    @Test
    void validatesProjectFieldsAndNestedLinks() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            RequestValidator requestValidator = new RequestValidator(factory.getValidator());
            Project project = new Project();
            project.setName("Portfolio");
            project.setDescription("Project description");
            project.setLink(new Link("Preview", "javascript:alert(1)"));
            project.setImages(List.of(new Link("Preview", "not a url")));

            List<String> errors = requestValidator.validate(project);

            assertEquals(2, errors.size());
            assertTrue(errors.contains("link.url: Must be a valid HTTP or HTTPS URL"));
            assertTrue(errors.contains("images[0].url: Must be a valid HTTP or HTTPS URL"));
        }
    }

    private Detail validDetail() {
        Detail detail = new Detail();
        detail.setName("Portfolio owner");
        detail.setEmail("owner@example.com");
        detail.setLocation("Earth");
        detail.setAbout("About the owner");
        detail.setImage("profile.png");
        detail.setResume("resume.pdf");
        detail.setLogo("logo.svg");
        return detail;
    }
}
