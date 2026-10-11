package com.bhupesh.portfolio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.dto.Response;
import com.bhupesh.portfolio.model.Detail;
import com.bhupesh.portfolio.service.DetailService;
import com.bhupesh.portfolio.validation.RequestValidator;

@RestController
@RequestMapping("/v1/detail")
public class DetailController {

    private final DetailService detailService;
    private final RequestValidator requestValidator;

    public DetailController(DetailService detailService, RequestValidator requestValidator) {
        this.detailService = detailService;
        this.requestValidator = requestValidator;
    }

    @GetMapping
    ResponseEntity<Response<Detail>> getDetails() {
        return detailService.fetchDetails()
                .map(detail -> ResponseEntity.ok(Response.<Detail>builder()
                        .success(true)
                        .data(detail)
                        .build()))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/name")
    ResponseEntity<Response<String>> getName() {
        return detailService.fetchName()
                .map(name -> ResponseEntity.ok(Response.<String>builder()
                        .success(true)
                        .data(name)
                        .build()))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping
    ResponseEntity<Response<?>> updateDetails(@RequestBody Detail detail) {
        List<String> errors = requestValidator.validate(detail);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(requestValidator.errorResponse("Validation failed", errors));
        }

        if (!detailService.updateDetails(detail)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Response.<String>builder()
                .success(true)
                .message("Details updated successfully")
                .build());
    }
}
