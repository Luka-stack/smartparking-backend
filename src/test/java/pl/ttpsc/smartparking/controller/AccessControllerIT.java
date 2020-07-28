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
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;
import pl.ttpsc.smartparking.persistence.repository.AccessRepository;
import pl.ttpsc.smartparking.persistence.repository.PlateRepository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccessControllerIT {

    private static final String PATH = "http://localhost:%d/api/accesses/%s";

    private String url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private PlateRepository plateRepository;

    private AccessEntity inBaseAccess;
    private AccessEntity newAccess;

    @BeforeEach
    void setUp() {

        inBaseAccess = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(1));
        newAccess = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(1));
        PlateEntity plateInBase = createPlateEntity("FKR12345");

        plateRepository.save(plateInBase);

        inBaseAccess.setPlate(plateInBase);
        newAccess.setPlate(plateInBase);
        accessRepository.save(inBaseAccess);
    }

    @AfterEach
    void tearDown() {

        accessRepository.deleteAllInBatch();
    }

    @Test
    void shouldReturnOneAccess() {

        // given
        url = String.format(PATH, port, inBaseAccess.getId());

        // when
        ResponseEntity<AccessEntity> response = restTemplate.getForEntity(url, AccessEntity.class);

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(Objects.requireNonNull(response.getBody()).getDateFrom(), inBaseAccess.getDateFrom());
        assertEquals(response.getBody().getDateTo(), inBaseAccess.getDateTo());
    }

    @Test
    void shouldReturnListOfAccess() {

        // given
        url = String.format(PATH, port, "");

        // when
        ResponseEntity<AccessEntity[]> response = restTemplate.getForEntity(url, AccessEntity[].class);

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).length);
        assertEquals(response.getBody()[0].getDateFrom(), inBaseAccess.getDateFrom());
    }

    @Test
    void getShouldThrowNotFoundAccessException() {

        // given
        url = String.format(PATH, port, 99);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(inBaseAccess, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreateAccess() {

        // given
        url = String.format(PATH, port, "");

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(newAccess, new HttpHeaders()), AccessEntity.class);

        System.out.println(response);

//        Optional<AccessEntity> accessReturned = accessRepository.findById(
//                Objects.requireNonNull(response.getBody()).getId());
//
//        //then
//        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
//        assertEquals(Objects.requireNonNull(response.getBody()).getDateFrom(), newAccess.getDateFrom());
//        assertEquals(response.getBody().getDateTo(), newAccess.getDateTo());
    }

    @Test
    void createShouldThrowInvalidInputWhenDateFromIsNull() {

        // given
        url = String.format(PATH, port, "");
        AccessEntity createAccess = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));
        createAccess.setDateTo(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(createAccess, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void createShouldThrowInvalidInputWhenDateToIsNull() {

        // given
        url = String.format(PATH, port, "");
        AccessEntity createAccess = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));
        createAccess.setDateTo(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(createAccess, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateAccess() {

        // given
        url = String.format(PATH, port, inBaseAccess.getId());
        AccessEntity accessUpdate = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(accessUpdate, new HttpHeaders()), AccessEntity.class);
        Optional<AccessEntity> accessReturned = accessRepository.findById(inBaseAccess.getId());

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(accessReturned.get().getDateFrom(), accessUpdate.getDateFrom());
        assertEquals(accessReturned.get().getDateTo(), accessUpdate.getDateTo());
    }

    @Test
    void updateShouldThrowNotFoundAccessException() {

        // given
        url = String.format(PATH, port, 99);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(newAccess, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateShouldThrowInvalidInputWhenDateFromIsNull() {

        // given
        url = String.format(PATH, port, inBaseAccess.getId());
        newAccess.setPlate(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(newAccess, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateShouldThrowInvalidInputWhenDateToIsNull() {
        // given
        url = String.format(PATH, port, inBaseAccess.getId());
        newAccess.setDateTo(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(newAccess, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldDeleteAccessById() {

        //given
        url = String.format(PATH, port, inBaseAccess.getId());

        //when
        restTemplate.delete(url);
        boolean isExist = accessRepository.existsById(inBaseAccess.getId());

        //then
        assertFalse(isExist);
    }

    private AccessEntity createAccessEntity(LocalDate dateFrom, LocalDate dateTo) {

        AccessEntity accessEntity = new AccessEntity();
        accessEntity.setDateFrom(dateFrom);
        accessEntity.setDateTo(dateTo);

        return accessEntity;
    }


    private PlateEntity createPlateEntity(String plateStr) {

        PlateEntity plateEntity = new PlateEntity();
        plateEntity.setPlateStr(plateStr);

        return plateEntity;
    }

}
