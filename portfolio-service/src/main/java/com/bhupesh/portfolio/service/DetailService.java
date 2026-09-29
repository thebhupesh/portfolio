package com.bhupesh.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.DetailModel;
import com.bhupesh.portfolio.repository.DetailRepository;

@Service 
public class DetailService {

    @Autowired 
    private DetailRepository detailRepository;

    public DetailModel fetchDetail() {
        return detailRepository.findById("1");
    }

    public String fetchName() {
        return this.fetchDetail().getName();
    }
}