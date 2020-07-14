package pl.ttpsc.smartparking.persistence.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.ttpsc.smartparking.error.exception.ErrorCode;
import pl.ttpsc.smartparking.error.exception.InvalidInputException;
import pl.ttpsc.smartparking.error.exception.NotFoundAccessException;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.repository.AccessRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class AccessServiceUT {

    private AccessEntity accessEntity;
    private AccessService accessService;

    @Mock
    private AccessRepository accessRepository;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        accessService = new AccessService(accessRepository);

        accessEntity = new AccessEntity();
        accessEntity.setDateFrom(LocalDate.now());
        accessEntity.setDateTo(LocalDate.now().plusDays(4));
    }

    @Test
    void shouldUpdateAccess() {

        // given
        when(accessRepository.findById(anyLong())).thenReturn(Optional.of(accessEntity));
        when(accessRepository.save(accessEntity)).thenReturn(accessEntity);

        // when
        AccessEntity updatedAccess = accessService.update(1L, accessEntity);

        // then
        assertEquals(updatedAccess, accessEntity);
    }

    @Test
    void shouldThrowNotFoundAccessException() {

        // when
        NotFoundAccessException exception = assertThrows(
                NotFoundAccessException.class, () -> accessService.update(1L, accessEntity)
        );

        // then
        assertEquals(exception.getMessage(), "Access not found");
        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_NOT_FOUND);
    }

    @Test
    void shouldThrowsInvalidInputExceptionWhenDateFromIsNull() {

        // given
        accessEntity.setDateFrom(null);

        // when
        InvalidInputException exception = assertThrows(
                InvalidInputException.class, () -> accessService.update(1L, accessEntity)
        );

        // then
        assertEquals(exception.getMessage(), "Invalid input -> dateFrom");
        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_INVALID_INPUT);
    }

    @Test
    void shouldThrowsInvalidInputExceptionWhenDateToIsNull() {

        // given
        accessEntity.setDateTo(null);

        // when
        InvalidInputException exception = assertThrows(
                InvalidInputException.class, () -> accessService.update(1L, accessEntity)
        );

        // then
        assertEquals(exception.getMessage(), "Invalid input -> dateTo");
        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_INVALID_INPUT);
    }
}