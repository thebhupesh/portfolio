package com.bhupesh.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.model.DetailModel;
import com.bhupesh.portfolio.service.DetailService;

@RestController
@RequestMapping("/v1/detail")
public class DetailController {

    @Autowired
    DetailService detailService;

    @GetMapping("/")
    ResponseEntity<DetailModel> getDetail() {
        DetailModel detail = detailService.fetchDetail();

        if(detail == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(detail);
    }

    @GetMapping("/name")
    ResponseEntity<String> getName() {
        String name = detailService.fetchName();

        if(name == null || name.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(name);
    }

    //TODO: Add PUT endpoints for detail management
}
