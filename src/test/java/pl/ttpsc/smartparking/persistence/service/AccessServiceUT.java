package pl.ttpsc.smartparking.persistence.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.ttpsc.smartparking.error.exception.ErrorCode;
import pl.ttpsc.smartparking.error.exception.InvalidInputException;
import pl.ttpsc.smartparking.error.exception.NotFoundAccessException;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;
import pl.ttpsc.smartparking.persistence.repository.AccessRepository;
import pl.ttpsc.smartparking.persistence.repository.PlateRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class AccessServiceUT {

    private AccessEntity accessEntity;
    private AccessService accessService;

    @Mock
    private AccessRepository accessRepository;

    @Mock
    private PlateRepository plateRepository;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        accessService = new AccessService(accessRepository);

        accessEntity = createAccessEntity();

    }

    private AccessEntity createAccessEntity() {

        accessEntity = new AccessEntity();
        accessEntity.setDateFrom(LocalDate.now());
        accessEntity.setDateTo(LocalDate.now().plusDays(4));
        accessEntity.setPlate(new PlateEntity());

        return accessEntity;
    }

    @Test
    void shouldReturnOneAccess() {

        // given
        when(accessRepository.findById(anyLong())).thenReturn(Optional.ofNullable(accessEntity));

        // when
        AccessEntity returnedAccess = accessService.getAccessById(1L);

        // then
        assertEquals(returnedAccess, accessEntity);
    }

    @Test
    void shouldReturnListOfAccesses() {

        // given
        when(accessRepository.findAll()).thenReturn(Arrays.asList(accessEntity, accessEntity));

        // when
        List<AccessEntity> returnedListOfAccesses = accessService.getAllAccesses();

        // then
        assertEquals(2, returnedListOfAccesses.size());
        assertEquals(returnedListOfAccesses.get(0), accessEntity);
        assertEquals(returnedListOfAccesses.get(1), accessEntity);
    }

    @Test
    void getShouldThrowNotFoundAccessException() {

        // when
        NotFoundAccessException exception = assertThrows(
                NotFoundAccessException.class, () -> accessService.getAccessById(1L)
        );

        // then
        assertEquals("Access not found", exception.getMessage());
        assertEquals(ErrorCode.ACCESS_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldCreateAccess() {

        // given
        when(accessRepository.save(accessEntity)).thenReturn(accessEntity);

        // when
        AccessEntity createdAccess = accessService.createAccess(accessEntity);

        // then
        assertEquals(createdAccess, accessEntity);
    }

    @Test
    void createShouldThrowInvalidInputExceptionWhenDateFromIsNull() {

        // given
        accessEntity.setDateFrom(null);

        // when
        InvalidInputException exception = assertThrows(
                InvalidInputException.class, () -> accessService.createAccess(accessEntity)
        );

        // then
        assertEquals("Invalid input -> dateFrom", exception.getMessage());
        assertEquals(ErrorCode.ACCESS_INVALID_INPUT, exception.getErrorCode());
    }

    @Test
    void createShouldThrowInvalidInputExceptionWhenDateToIsNull() {

        // given
        accessEntity.setDateTo(null);

        // when
        InvalidInputException exception = assertThrows(
                InvalidInputException.class, () -> accessService.createAccess(accessEntity)
        );

        // then
        assertEquals("Invalid input -> dateTo", exception.getMessage());
        assertEquals(ErrorCode.ACCESS_INVALID_INPUT, exception.getErrorCode());
    }

    @Test
    void shouldUpdateAccess() {

        // given
        when(accessRepository.findById(anyLong())).thenReturn(Optional.of(accessEntity));
        when(accessRepository.save(accessEntity)).thenReturn(accessEntity);

        // when
        AccessEntity updatedAccess = accessService.updateAccess(1L, accessEntity);

        // then
        assertEquals(updatedAccess, accessEntity);
    }

    @Test
    void updateShouldThrowNotFoundAccessException() {

        // when
        NotFoundAccessException exception = assertThrows(
                NotFoundAccessException.class, () -> accessService.updateAccess(1L, accessEntity)
        );

        // then
        assertEquals("Access not found", exception.getMessage());
        assertEquals(ErrorCode.ACCESS_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateShouldThrowInvalidInputExceptionWhenDateFromIsNull() {

        // given
        accessEntity.setDateFrom(null);

        // when
        InvalidInputException exception = assertThrows(
                InvalidInputException.class, () -> accessService.updateAccess(1L, accessEntity)
        );

        // then
        assertEquals("Invalid input -> dateFrom", exception.getMessage());
        assertEquals(ErrorCode.ACCESS_INVALID_INPUT, exception.getErrorCode());
    }

    @Test
    void updateShouldThrowInvalidInputExceptionWhenDateToIsNull() {

        // given
        accessEntity.setDateTo(null);

        // when
        InvalidInputException exception = assertThrows(
                InvalidInputException.class, () -> accessService.updateAccess(1L, accessEntity)
        );

        // then
        assertEquals("Invalid input -> dateTo", exception.getMessage());
        assertEquals(ErrorCode.ACCESS_INVALID_INPUT, exception.getErrorCode());
    }

    @Test
    void shouldDeleteAccessById() {

        // given

        // when
        accessService.deleteAccessById(1L);

        // then
        verify(accessRepository, times(1)).deleteById(anyLong());
    }
}