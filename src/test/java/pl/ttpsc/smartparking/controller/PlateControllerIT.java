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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlateControllerIT {

    private static final String PATH = "http://localhost:%d/api/plates/%s";

    private String url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlateRepository plateRepository;

    private PlateEntity plateInBase;

    @BeforeEach
    void setUp() {

        plateInBase = createPlateEntity("FKR12345");
        plateRepository.save(plateInBase);
    }

    @AfterEach
    void tearDown() {
        
        plateRepository.deleteAllInBatch();
    }

    @Test
    void shouldReturnOnePlate() {

        // given
        url = String.format(PATH, port, plateInBase.getId());

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(plateInBase, new HttpHeaders()), PlateEntity.class);

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(Objects.requireNonNull(response.getBody()).getPlateStr(), plateInBase.getPlateStr());
    }

    @Test
    void shouldReturnListOfPlates() {

        // given
        url = String.format(PATH, port, "");

        // when
        ResponseEntity<PlateEntity[]> response = restTemplate.getForEntity(url, PlateEntity[].class);

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).length);
        assertEquals(response.getBody()[0].getPlateStr(), plateInBase.getPlateStr());
    }

    @Test
    void getShouldThrowNotFoundPlateException() {

        // given
        url = String.format(PATH, port, 99);
        PlateEntity plateReturned = createPlateEntity("FZ12345");

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(plateReturned, new HttpHeaders()), PlateEntity.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreatePlate() {

        // given
        url = String.format(PATH, port, "");
        PlateEntity plateCreate = createPlateEntity("FZ12345");

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(plateCreate, new HttpHeaders()), PlateEntity.class);
        Optional<PlateEntity> plateReturned = plateRepository.findById(
                Objects.requireNonNull(response.getBody()).getId());

        // then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(plateReturned.get().getPlateStr(), plateCreate.getPlateStr());
    }

    @Test
    void createShouldThrowInvalidInputWhenPlateIsNull() {

        // given
        url = String.format(PATH, port, "");
        PlateEntity plateCreate = createPlateEntity("FZ12345");
        plateCreate.setPlateStr(null);

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(plateCreate, new HttpHeaders()), PlateEntity.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldUpdatePlate() {

        // given
        url = String.format(PATH, port, plateInBase.getId());
        PlateEntity plateUpdate = createPlateEntity("FZ12345");

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(plateUpdate, new HttpHeaders()), PlateEntity.class);
        Optional<PlateEntity> plateReturned = plateRepository.findById(plateInBase.getId());

        // then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(plateReturned.get().getPlateStr(), plateUpdate.getPlateStr());
    }

    @Test
    void updateShouldThrowNotFoundPlateException() {

        // given
        url = String.format(PATH, port, 190);
        PlateEntity plateUpdate = createPlateEntity("FZ12345");

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(plateUpdate, new HttpHeaders()), PlateEntity.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateShouldThrowInvalidInputWhenPlateIsNull() {

        // given
        url = String.format(PATH, port, plateInBase.getId());
        PlateEntity plateUpdate = createPlateEntity("FZ12345");
        plateUpdate.setPlateStr(null);

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(plateUpdate, new HttpHeaders()), PlateEntity.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldDeleteAccessById() {

        //given
        url = String.format(PATH, port, plateInBase.getId());

        //when
        restTemplate.delete(url);
        boolean isExist = plateRepository.existsById(plateInBase.getId());

        //then
        assertFalse(isExist);
    }

    private PlateEntity createPlateEntity(String plate) {

        PlateEntity plateEntity = new PlateEntity();
        plateEntity.setPlateStr(plate);

        return plateEntity;
    }
}