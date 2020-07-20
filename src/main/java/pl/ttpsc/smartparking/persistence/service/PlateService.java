package pl.ttpsc.smartparking.persistence.service;

import org.springframework.stereotype.Service;
import pl.ttpsc.smartparking.error.exception.ErrorCode;
import pl.ttpsc.smartparking.error.exception.InvalidInputException;
import pl.ttpsc.smartparking.error.exception.NotFoundAccessException;
import pl.ttpsc.smartparking.error.exception.NotFoundPlateException;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;
import pl.ttpsc.smartparking.persistence.repository.PlateRepository;

import java.util.List;

@Service
public class PlateService {

    private final PlateRepository plateRepository;

    public PlateService(final PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }

    public PlateEntity getPlateById(Long id) {

        return plateRepository.findById(id).orElseThrow(() ->
                new NotFoundAccessException("Access not found", ErrorCode.ACCESS_NOT_FOUND));
    }

    public List<PlateEntity> getAllPlates() {

        return plateRepository.findAll();
    }

    public PlateEntity createPlate(PlateEntity plateEntity) {

        if (plateEntity.getPlateStr() == null) {
            throw new InvalidInputException("Invalid input -> plate", ErrorCode.PLATE_INVALID_INPUT);
        }

        return plateRepository.save(plateEntity);
    }

    public PlateEntity updatePlate(Long id, PlateEntity plateEntity) {

        if (plateEntity.getPlateStr() == null) {
            throw new InvalidInputException("Invalid input -> plate", ErrorCode.PLATE_INVALID_INPUT);
        }

        plateRepository.findById(id).orElseThrow(() ->
                new NotFoundPlateException("Plate not found", ErrorCode.PLATE_NOT_FOUND));
        plateEntity.setId(id);

        return plateRepository.save(plateEntity);
    }

    public void deletePlateById(Long id) {

        plateRepository.deleteById(id);
    }
}
