package com.bhupesh.portfolio.service;

import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.Detail;
import com.bhupesh.portfolio.repository.DetailRepository;

@Service 
public class DetailService {

    private final DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }

    public Detail fetchDetails() {
        return detailRepository.findAll().stream().findFirst().orElse(null);
    }

    public String fetchName() {
        Detail detail = this.fetchDetails();
        if (detail == null) {
            return null;
        }

        return detail.getName();
    }
}