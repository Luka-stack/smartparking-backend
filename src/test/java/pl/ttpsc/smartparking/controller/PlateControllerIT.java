package pl.ttpsc.smartparking.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;
import pl.ttpsc.smartparking.persistence.repository.PlateRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlateControllerIT {

    private static final String PATH = "http://localhost:%d/api/plate/%s";

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
        assertEquals(response.getBody().getPlate(), plateInBase.getPlate());
    }

    @Test
    void shouldReturnListOfPlates() {

        // given
        url = String.format(PATH, port, "");

        // when
        ResponseEntity<List<PlateEntity>> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(Collections.singletonList(plateInBase), new HttpHeaders()),
                new ParameterizedTypeReference<List<PlateEntity>>() {});

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(response.getBody().size(), 1);
        assertEquals(response.getBody().get(0).getPlate(), plateInBase.getPlate());
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
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldCreatePlate() {

        // given
        url = String.format(PATH, port, "");
        PlateEntity plateCreate = createPlateEntity("FZ12345");

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(plateCreate, new HttpHeaders()), PlateEntity.class);
        Optional<PlateEntity> plateReturned = plateRepository.findById(response.getBody().getId());

        // then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(plateReturned.get().getPlate(), plateCreate.getPlate());
    }

    @Test
    void createShouldThrowInvalidInputWhenPlateIsNull() {

        // given
        url = String.format(PATH, port, "");
        PlateEntity plateCreate = createPlateEntity("FZ12345");
        plateCreate.setPlate(null);

        // when
        ResponseEntity<PlateEntity> response = restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(plateCreate, new HttpHeaders()), PlateEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
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
        assertEquals(plateReturned.get().getPlate(), plateUpdate.getPlate());
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
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void updateShouldThrowInvalidInputWhenPlateIsNull() {

        // given
        url = String.format(PATH, port, plateInBase.getId());
        PlateEntity plateUpdate = createPlateEntity("FZ12345");
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