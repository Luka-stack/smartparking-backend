package pl.ttpsc.smartparking.persistence.service;

import java.util.Arrays;
import java.util.List;
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
import static org.mockito.Mockito.*;


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
        assertEquals(returnedListOfAccesses.size(), 2);
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
        assertEquals(exception.getMessage(), "Access not found");
        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_NOT_FOUND);
    }

//    @Test
//    void shouldCreateAccess() {
//
//        // given
//        when(accessRepository.save(accessEntity)).thenReturn(accessEntity);
//
//        // when
//        AccessEntity createdAccess = accessService.createAccess(accessEntity);
//
//        // then
//        assertEquals(createdAccess, accessEntity);
//    }
//
//    @Test
//    void createShouldThrowInvalidInputExceptionWhenDateFromIsNull() {
//
//        // given
//        accessEntity.setDateFrom(null);
//
//        // when
//        InvalidInputException exception = assertThrows(
//                InvalidInputException.class, () -> accessService.createAccess(accessEntity)
//        );
//
//        // then
//        assertEquals(exception.getMessage(), "Invalid input -> dateFrom");
//        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_INVALID_INPUT);
//    }
//
//    @Test
//    void createShouldThrowInvalidInputExceptionWhenDateToIsNull() {
//
//        // given
//        accessEntity.setDateTo(null);
//
//        // when
//        InvalidInputException exception = assertThrows(
//                InvalidInputException.class, () -> accessService.createAccess(accessEntity)
//        );
//
//        // then
//        assertEquals(exception.getMessage(), "Invalid input -> dateTo");
//        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_INVALID_INPUT);
//    }

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
        assertEquals(exception.getMessage(), "Access not found");
        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_NOT_FOUND);
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
        assertEquals(exception.getMessage(), "Invalid input -> dateFrom");
        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_INVALID_INPUT);
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
        assertEquals(exception.getMessage(), "Invalid input -> dateTo");
        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_INVALID_INPUT);
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