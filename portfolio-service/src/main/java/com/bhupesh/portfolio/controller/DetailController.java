package com.bhupesh.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.dto.Response;
import com.bhupesh.portfolio.model.Detail;
import com.bhupesh.portfolio.service.DetailService;

@RestController
@RequestMapping("/v1/detail")
public class DetailController {

    private final DetailService detailService;

    public DetailController(DetailService detailService) {
        this.detailService = detailService;
    }

    @GetMapping
    ResponseEntity<Response<Detail>> getDetails() {
        Detail detail = detailService.fetchDetails();

        if(detail == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(Response.<Detail>builder()
                .success(true)
                .data(detail)
                .build());
    }

    @GetMapping("/name")
    ResponseEntity<Response<String>> getName() {
        String name = detailService.fetchName();

        if(name == null || name.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(Response.<String>builder()
                .success(true)
                .data(name)
                .build());
    }

    //TODO: Add PUT endpoints for detail management
}
