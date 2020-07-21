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
import pl.ttpsc.smartparking.persistence.repository.AccessRepository;
import pl.ttpsc.smartparking.persistence.repository.PlateRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccessControllerIT {

    private static final String PATH = "http://localhost:%d/api/accesses/%s";

    private String uri;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private PlateRepository plateRepository;

    private AccessEntity accessInBase;

    @BeforeEach
    void setUp() {

        accessInBase = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(1));
        accessRepository.save(accessInBase);
    }

    @AfterEach
    void tearDown() {

        accessRepository.deleteAllInBatch();
    }

    @Test
    void shouldReturnOneAccess() {

        // given
        uri = String.format(PATH, port, accessInBase.getId());

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(uri, HttpMethod.GET,
                new HttpEntity<>(accessInBase, new HttpHeaders()), AccessEntity.class);

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(response.getBody().getDateFrom(), accessInBase.getDateFrom());
        assertEquals(response.getBody().getDateTo(), accessInBase.getDateTo());
    }

    @Test
    void shouldReturnListOfAccess() {

        // given
        uri = String.format(PATH, port, "");

        // when
        ResponseEntity<List<AccessEntity>> response = restTemplate.exchange(uri, HttpMethod.GET,
                new HttpEntity<>(Collections.singletonList(accessInBase), new HttpHeaders()),
                new ParameterizedTypeReference<List<AccessEntity>>() {});

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(response.getBody().size(), 1);
        assertEquals(response.getBody().get(0).getDateFrom(), accessInBase.getDateFrom());
    }

    @Test
    void getShouldThrowNotFoundAccessException() {

        // given
        uri = String.format(PATH, port, 99);
        AccessEntity accessReturned = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(uri, HttpMethod.GET,
                new HttpEntity<>(accessReturned, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    // TODO implement this test
    @Test
    void shouldCreateAccess() {

        // given
        uri = String.format(PATH, port, "");
        AccessEntity createAccess = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(uri, HttpMethod.POST,
                new HttpEntity<>(createAccess, new HttpHeaders()), AccessEntity.class);

        System.out.println(response.getBody().getDateTo());

        //Optional<AccessEntity> accessReturned = accessRepository.findById(response.getBody().getId());

        //then
//        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
//        assertEquals(accessReturned.get().getDateFrom(), createAccess.getDateFrom());
//        assertEquals(accessReturned.get().getDateTo(), createAccess.getDateTo());
    }

    @Test
    void createShouldThrowInvalidInputWhenDateFromIsNull() {

        // given
        uri = String.format(PATH, port, "");
        AccessEntity createAccess = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));
        createAccess.setDateTo(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(uri, HttpMethod.POST,
                new HttpEntity<>(createAccess, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void createShouldThrowInvalidInputWhenDateToIsNull() {

        // given
        uri = String.format(PATH, port, "");
        AccessEntity createAccess = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));
        createAccess.setDateTo(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(uri, HttpMethod.POST,
                new HttpEntity<>(createAccess, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateAccess() {

        // given
        uri = String.format(PATH, port, accessInBase.getId());
        AccessEntity accessUpdate = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(uri, HttpMethod.PUT,
                new HttpEntity<>(accessUpdate, new HttpHeaders()), AccessEntity.class);
        Optional<AccessEntity> accessReturned = accessRepository.findById(accessInBase.getId());

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(accessReturned.get().getDateFrom(), accessUpdate.getDateFrom());
        assertEquals(accessReturned.get().getDateTo(), accessUpdate.getDateTo());
    }

    @Test
    void updateShouldThrowNotFoundAccessException() {

        // given
        uri = String.format(PATH, port, 99);
        AccessEntity accessUpdate = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(uri, HttpMethod.PUT,
                new HttpEntity<>(accessUpdate, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void updateShouldThrowInvalidInputWhenDateFromIsNull() {

        // given
        uri = String.format(PATH, port, accessInBase.getId());
        AccessEntity accessUpdate = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));
        accessUpdate.setDateFrom(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(uri, HttpMethod.PUT,
                new HttpEntity<>(accessUpdate, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateShouldThrowInvalidInputWhenDateToIsNull() {

        // given
        uri = String.format(PATH, port, accessInBase.getId());
        AccessEntity accessUpdate = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(10));
        accessUpdate.setDateTo(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(uri, HttpMethod.PUT,
                new HttpEntity<>(accessUpdate, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldDeleteAccessById() {

        //given
        uri = String.format(PATH, port, accessInBase.getId());

        //when
        restTemplate.delete(uri);
        boolean isExist = accessRepository.existsById(accessInBase.getId());

        //then
        assertFalse(isExist);
    }

    private AccessEntity createAccessEntity(LocalDate dateFrom, LocalDate dateTo) {

        AccessEntity accessEntity = new AccessEntity();
        accessEntity.setDateFrom(dateFrom);
        accessEntity.setDateTo(dateTo);

        return accessEntity;
    }
}
