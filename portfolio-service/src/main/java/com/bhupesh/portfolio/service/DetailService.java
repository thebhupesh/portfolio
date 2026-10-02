package com.bhupesh.portfolio.service;

import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.DetailModel;
import com.bhupesh.portfolio.repository.DetailRepository;

@Service 
public class DetailService {

    private final DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }

    public DetailModel fetchDetail() {
        return detailRepository.findById("1");
    }

    public String fetchName() {
        return this.fetchDetail().getName();
    }
}