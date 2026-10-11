package com.bhupesh.portfolio.service;

import java.time.Instant;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.bhupesh.portfolio.model.Detail;
import com.bhupesh.portfolio.repository.DetailRepository;

@Service 
public class DetailService {

    private final DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }

    public Optional<Detail> fetchDetails() {
        return detailRepository.findAll().stream().findFirst();
    }

    public Optional<String> fetchName() {
        return fetchDetails().map(Detail::getName).filter(name -> !name.isBlank());
    }

    public boolean updateDetails(Detail detail) {
        Optional<Detail> existingDetail = fetchDetails();
        if (existingDetail.isEmpty()) {
            return false;
        }

        Detail persistedDetail = existingDetail.orElseThrow();
        detail.setType(persistedDetail.getType());
        detail.setId(persistedDetail.getId());
        detail.setUpdatedAt(Instant.now());
        detailRepository.update(detail);
        return true;
    }
}