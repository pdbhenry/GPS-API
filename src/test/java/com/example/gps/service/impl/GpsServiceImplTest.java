package com.example.gps.service.impl;

import com.example.gps.model.Location;
import com.example.gps.repository.GpsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GpsServiceImplTest {

    @Mock
    private GpsRepo gpsRepo;

    @InjectMocks
    private GpsServiceImpl gpsService;

    private Location location;

    @BeforeEach
    public void setup(){
        location = Location.builder()
                .id(1L)
                .name("McDonald's")
                .latitude(25.0)
                .longitude(50.0)
                .build();
    }

    @Test
    public void should_find_no_locations_if_repository_is_empty() {
        Iterable<Location> locations = gpsService.findAllLocations();

        assertThat(locations).isEmpty();
    }

    @Test
    public void should_store_a_location() {
        given(gpsRepo.save(location)).willReturn(location);

        Location loc1 = gpsService.saveLocation(location);

        System.out.println(loc1);
        assertThat(loc1).isNotNull();
    }
}