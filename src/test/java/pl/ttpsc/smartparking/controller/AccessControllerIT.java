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
import pl.ttpsc.smartparking.persistence.repository.AccessRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccessControllerIT {

    private static final String PATH = "http://localhost:%d/api/access/%d";

    private String url;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccessRepository accessRepository;

    private AccessEntity accessBeforeUpdate;
    private AccessEntity accessUpdate;

    @BeforeEach
    void setUp() {

        accessBeforeUpdate = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(1));
        accessRepository.save(accessBeforeUpdate);

        accessUpdate = createAccessEntity(LocalDate.now(), LocalDate.now().plusDays(5));
    }

    @AfterEach
    void tearDown() {

        accessRepository.deleteAllInBatch();
    }

    @Test
    void shouldUpdateAccess() {

        // given
        url = String.format(PATH, port, accessBeforeUpdate.getId());

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(accessUpdate, new HttpHeaders()), AccessEntity.class);
        Optional<AccessEntity> accessReturned = accessRepository.findById(accessBeforeUpdate.getId());

        //then
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(accessReturned.get().getDateFrom(), accessUpdate.getDateFrom());
        assertEquals(accessReturned.get().getDateTo(), accessUpdate.getDateTo());
    }

    @Test
    void shouldThrowNotFoundAccessException() {

        // given
        url = String.format(PATH, port, 99);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(accessUpdate, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldThrowInvalidInputWhenDateFromIsNull() {

        // given
        url = String.format(PATH, port, accessBeforeUpdate.getId());
        accessUpdate.setDateFrom(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(accessUpdate, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldThrowInvalidInputWhenDateToIsNull() {

        // given
        url = String.format(PATH, port, accessBeforeUpdate.getId());
        accessUpdate.setDateTo(null);

        // when
        ResponseEntity<AccessEntity> response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(accessUpdate, new HttpHeaders()), AccessEntity.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    private AccessEntity createAccessEntity(LocalDate dateFrom, LocalDate dateTo) {

        AccessEntity accessEntity = new AccessEntity();
        accessEntity.setDateFrom(dateFrom);
        accessEntity.setDateTo(dateTo);

        return accessEntity;
    }
}
