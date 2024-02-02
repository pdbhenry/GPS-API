package com.example.gps;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.gps.model.Location;
import com.example.gps.repository.GpsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class JpaUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    GpsRepo gpsRepo;

    @Test
    public void should_find_no_locations_if_repository_is_empty() {
        Iterable<Location> locations = gpsRepo.findAll();

        assertThat(locations).isEmpty();
    }

    @Test
    public void should_store_a_location() {
        Location tutorial = gpsRepo.save(new Location("McDonald's", 25.0, 50.0));

        assertThat(tutorial).hasFieldOrPropertyWithValue("name", "McDonald's");
        assertThat(tutorial).hasFieldOrPropertyWithValue("latitude", 25.0);
        assertThat(tutorial).hasFieldOrPropertyWithValue("longitude", 50.0);
    }

    @Test
    public void should_find_all_locations() {
        Location loc1 = new Location("McDonald's", 25.0, 50.0);
        entityManager.persist(loc1);

        Location loc2 = new Location("McDonald's", 25.0, 50.0);
        entityManager.persist(loc2);

        Location loc3 = new Location("McDonald's", 25.0, 50.0);
        entityManager.persist(loc3);

        Iterable<Location> locations = gpsRepo.findAll();

        assertThat(locations).hasSize(3).contains(loc1, loc2, loc3);
    }

    @Test
    public void should_find_tutorial_by_id() {
        Location tut1 = new Location("McDonald's", 25.0, 50.0);
        entityManager.persist(tut1);

        Location tut2 = new Location("McDonald's", 25.0, 50.0);
        entityManager.persist(tut2);

        Location foundTutorial = gpsRepo.findById(tut2.getId()).get();

        assertThat(foundTutorial).isEqualTo(tut2);
    }

    @Test
    public void should_find_tutorials_by_title_containing_string() {
        Location loc1 = new Location("McDonald's", 25.0, 50.0);
        entityManager.persist(loc1);

        Location loc2 = new Location("Burger King", 50.0, 75.0);
        entityManager.persist(loc2);

        Location loc3 = new Location("McDonald's", 10.0, 90.0);
        entityManager.persist(loc3);

        Iterable<Location> tutorials = gpsRepo.findByNameContaining("nald");

        assertThat(tutorials).hasSize(2).contains(loc1, loc3);
    }

    @Test
    public void should_update_tutorial_by_id() {
        Location tut1 = new Location("McDonald's", 25.0, 50.0);
        entityManager.persist(tut1);

        Location tut2 = new Location("Chipotle", 50.0, 20.0);
        entityManager.persist(tut2);

        Location updatedTut = new Location("Chipotle 2", 55.0, 25.0);

        Location tut = gpsRepo.findById(tut2.getId()).get();
        tut.setName(updatedTut.getName());
        tut.setLatitude(updatedTut.getLatitude());
        tut.setLongitude(updatedTut.getLongitude());
        gpsRepo.save(tut);

        Location checkTut = gpsRepo.findById(tut2.getId()).get();

        assertThat(checkTut.getId()).isEqualTo(tut2.getId());
        assertThat(checkTut.getName()).isEqualTo(updatedTut.getName());
        assertThat(checkTut.getLatitude()).isEqualTo(updatedTut.getLatitude());
        assertThat(checkTut.getLongitude()).isEqualTo(updatedTut.getLongitude());
    }

    @Test
    public void should_delete_tutorial_by_id() {
        Location tut1 = new Location("McDonald's", 25.0, 50.0);
        entityManager.persist(tut1);

        Location tut2 = new Location("Burger King", 10.0, 30.0);
        entityManager.persist(tut2);

        Location tut3 = new Location("Chipotle", 45.0, 75.0);
        entityManager.persist(tut3);

        gpsRepo.deleteById(tut2.getId());

        Iterable<Location> tutorials = gpsRepo.findAll();

        assertThat(tutorials).hasSize(2).contains(tut1, tut3);
    }

    @Test
    public void should_delete_all_tutorials() {
        entityManager.persist(new Location("McDonald's", 25.0, 50.0));
        entityManager.persist(new Location("Burger King", 10.0, 30.0));

        gpsRepo.deleteAll();

        assertThat(gpsRepo.findAll()).isEmpty();
    }
}
