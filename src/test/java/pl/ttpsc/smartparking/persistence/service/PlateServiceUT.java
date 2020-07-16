package pl.ttpsc.smartparking.persistence.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.ttpsc.smartparking.error.exception.ErrorCode;
import pl.ttpsc.smartparking.error.exception.InvalidInputException;
import pl.ttpsc.smartparking.error.exception.NotFoundAccessException;
import pl.ttpsc.smartparking.error.exception.NotFoundPlateException;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;
import pl.ttpsc.smartparking.persistence.repository.PlateRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PlateServiceUT {

    private PlateEntity plateEntity;
    private PlateService plateService;

    @Mock
    private PlateRepository plateRepository;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        plateService = new PlateService(plateRepository);

        plateEntity = new PlateEntity();
        plateEntity.setPlate("FZI12345");
    }

    @Test
    void shouldReturnOnePlate() {

        // given
        when(plateRepository.findById(anyLong())).thenReturn(Optional.ofNullable(plateEntity));

        // when
        PlateEntity returnedPlate = plateService.getPlateById(1L);

        // then
        assertEquals(returnedPlate, plateEntity);
    }

    @Test
    void shouldReturnListOfPlates() {

        // given
        when(plateRepository.findAll()).thenReturn(Arrays.asList(plateEntity, plateEntity));

        // when
        List<PlateEntity> returnedListOfPlates = plateService.getAllPlates();

        // then
        assertEquals(returnedListOfPlates.size(), 2);
        assertEquals(returnedListOfPlates.get(0), plateEntity);
        assertEquals(returnedListOfPlates.get(1), plateEntity);
    }

    @Test
    void getShouldThrowNotFoundPlateException() {

        // when
        NotFoundAccessException exception = assertThrows(
                NotFoundAccessException.class, () -> plateService.getPlateById(1L)
        );

        // then
        assertEquals(exception.getMessage(), "Access not found");
        assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_NOT_FOUND);
    }

    @Test
    void shouldCreatePlate() {

        // given
        when(plateRepository.save(plateEntity)).thenReturn(plateEntity);

        // when
        PlateEntity createdPlate = plateService.createPlate(plateEntity);

        // then
        assertEquals(createdPlate, plateEntity);
    }

    @Test
    void createShouldThrowsInvalidInputExceptionWhenPlateIsNull() {

        // given
        plateEntity.setPlate(null);

        // when
        InvalidInputException exception = assertThrows(
                InvalidInputException.class, () -> plateService.createPlate(plateEntity)
        );

        // then
        assertEquals(exception.getMessage(), "Invalid input -> plate");
        assertEquals(exception.getErrorCode(), ErrorCode.PLATE_INVALID_INPUT);
    }

    @Test
    void shouldUpdatePlate() {

        // given
        when(plateRepository.findById(anyLong())).thenReturn(Optional.of(plateEntity));
        when(plateRepository.save(plateEntity)).thenReturn(plateEntity);

        // when
        PlateEntity updatedPlate = plateService.updatePlate(1L, plateEntity);

        // then
        assertEquals(updatedPlate, plateEntity);
    }

    @Test
    void updateShouldThrowNotFoundException() {

        // when
        NotFoundPlateException exception = assertThrows(
                NotFoundPlateException.class, () -> plateService.updatePlate(1L, plateEntity)
        );

        // then
        assertEquals(exception.getMessage(), "Plate not found");
        assertEquals(exception.getErrorCode(), ErrorCode.PLATE_NOT_FOUND);
    }

    @Test
    void updateShouldThrowsInvalidInputExceptionWhenPlateIsNull() {

        // given
        plateEntity.setPlate(null);

        // when
        InvalidInputException exception = assertThrows(
                InvalidInputException.class, () -> plateService.updatePlate(1L, plateEntity)
        );

        // then
        assertEquals(exception.getMessage(), "Invalid input -> plate");
        assertEquals(exception.getErrorCode(), ErrorCode.PLATE_INVALID_INPUT);
    }

    @Test
    void shouldDeleteAccessById() {

        // given

        // when
        plateService.deletePlateById(1L);

        // then
        verify(plateRepository, times(1)).deleteById(anyLong());
    }
}
