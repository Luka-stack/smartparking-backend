package pl.ttpsc.smartparking.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;
import pl.ttpsc.smartparking.persistence.repository.PlateRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlateControllerUT {

    private static final String PATH = "http://localhost:%d/api/plate/%d";

    private String url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlateRepository plateRepository;

    private PlateEntity plateBeforeUpdate;
    private PlateEntity plateUpdate;

    @BeforeEach
    void setUp() {

        plateBeforeUpdate = createPlateEntity("FKR12345");
        plateRepository.save(plateBeforeUpdate);
        
        plateUpdate = createPlateEntity("FZ12345");
    }

    @AfterEach
    void tearDown() {
        
        plateRepository.deleteAllInBatch();
    }

    @Test
    void shouldUpdatePlate() {

        // given
        url = String.format(PATH, port, plateBeforeUpdate.getId());

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(plateUpdate, new HttpHeaders()), PlateEntity.class);
        Optional<PlateEntity> plateReturned = plateRepository.findById(plateBeforeUpdate.getId());

        // then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(plateReturned.get().getPlate(), plateUpdate.getPlate());
    }

    @Test
    void shouldThrowNotFoundPlateException() {

        // given
        url = String.format(PATH, port, 190);

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(plateUpdate, new HttpHeaders()), PlateEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldThrowInvalidInputWhenPlateIsNull() {

        // given
        url = String.format(PATH, port, plateBeforeUpdate.getId());
        plateUpdate.setPlate(null);

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(plateUpdate, new HttpHeaders()), PlateEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    private PlateEntity createPlateEntity(String plate) {

        PlateEntity plateEntity = new PlateEntity();
        plateEntity.setPlate(plate);

        return plateEntity;
    }
}