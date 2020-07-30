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
import pl.ttpsc.smartparking.error.Error;
import pl.ttpsc.smartparking.error.exception.ErrorCode;
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
        newAccess = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(2));
        PlateEntity plateInBase = createPlateEntity("FKR12345");

        plateInBase = plateRepository.save(plateInBase);

        inBaseAccess.setPlate(plateInBase);
        newAccess.setPlate(plateInBase);
        accessRepository.save(inBaseAccess);
    }

    @AfterEach
    void tearDown() {

        accessRepository.deleteAllInBatch();
        plateRepository.deleteAllInBatch();
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
        url = String.format(PATH, port, 123L);
        Error expectedError = new Error("Access not found", ErrorCode.ACCESS_NOT_FOUND);

        // when
        ResponseEntity<Error> response = restTemplate.getForEntity(url, Error.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedError.getErrorCode(), Objects.requireNonNull(response.getBody()).getErrorCode());
        assertEquals(expectedError.getMessage(), response.getBody().getMessage());
    }

    @Test
    void shouldCreateAccess() {

        // given
        url = String.format(PATH, port, "");

        // when
        ResponseEntity<AccessEntity> response = restTemplate.postForEntity(url, newAccess, AccessEntity.class);
        Optional<AccessEntity> accessReturned = accessRepository.findById(
                Objects.requireNonNull(response.getBody()).getId());

        assert accessReturned.isPresent();

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(accessReturned.get().getDateFrom(), newAccess.getDateFrom());
        assertEquals(accessReturned.get().getDateTo(), newAccess.getDateTo());
    }

    @Test
    void createShouldThrowInvalidInputWhenDateFromIsNull() {

        // given
        url = String.format(PATH, port, "");
        Error expectedError = new Error("Invalid input -> dateFrom", ErrorCode.ACCESS_INVALID_INPUT);
        newAccess.setDateFrom(null);

        // when
        ResponseEntity<Error> response = restTemplate.postForEntity(url, newAccess, Error.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedError.getErrorCode(), Objects.requireNonNull(response.getBody()).getErrorCode());
        assertEquals(expectedError.getMessage(), response.getBody().getMessage());
    }

    @Test
    void createShouldThrowInvalidInputWhenDateToIsNull() {

        // given
        url = String.format(PATH, port, "");
        Error expectedError = new Error("Invalid input -> dateTo", ErrorCode.ACCESS_INVALID_INPUT);
        newAccess.setDateTo(null);

        // when
        ResponseEntity<Error> response = restTemplate.postForEntity(url, newAccess, Error.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedError.getErrorCode(), Objects.requireNonNull(response.getBody()).getErrorCode());
        assertEquals(expectedError.getMessage(), response.getBody().getMessage());
    }

    @Test
    void shouldUpdateAccess() {

        // given
        url = String.format(PATH, port, inBaseAccess.getId());

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(newAccess, new HttpHeaders()), AccessEntity.class);
        Optional<AccessEntity> accessReturned = accessRepository.findById(inBaseAccess.getId());

        assert accessReturned.isPresent();

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(newAccess.getDateFrom(), accessReturned.get().getDateFrom());
        assertEquals(newAccess.getDateTo(), accessReturned.get().getDateTo());
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
        newAccess.setDateFrom(null);

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
        plateEntity.setPlateNum(plateStr);

        return plateEntity;
    }

}
