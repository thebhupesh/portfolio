package com.bhupesh.portfolio.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bhupesh.portfolio.model.Detail;
import com.bhupesh.portfolio.repository.DetailRepository;

class DetailServiceTest {

    private DetailRepository detailRepository;
    private DetailService detailService;

    @BeforeEach
    void setUp() {
        detailRepository = mock(DetailRepository.class);
        detailService = new DetailService(detailRepository);
    }

    @Test
    void preservesPersistedDetailKeysWhenUpdating() {
        Detail existing = new Detail();
        existing.setType("detail");
        existing.setId("profile");
        when(detailRepository.findAll()).thenReturn(List.of(existing));

        Detail update = new Detail();
        update.setType("project");
        update.setId("another-record");

        assertTrue(detailService.updateDetails(update));

        assertEquals("detail", update.getType());
        assertEquals("profile", update.getId());
        assertNotNull(update.getUpdatedAt());
        verify(detailRepository).update(update);
    }

    @Test
    void doesNotCreateOrOverwriteARecordWhenNoDetailExists() {
        when(detailRepository.findAll()).thenReturn(List.of());

        assertFalse(detailService.updateDetails(new Detail()));

        verify(detailRepository, never()).update(org.mockito.ArgumentMatchers.any(Detail.class));
    }
}
