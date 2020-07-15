package pl.ttpsc.smartparking.persistence.service;

import org.springframework.stereotype.Service;
import pl.ttpsc.smartparking.error.exception.ErrorCode;
import pl.ttpsc.smartparking.error.exception.InvalidInputException;
import pl.ttpsc.smartparking.error.exception.NotFoundPlateException;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;
import pl.ttpsc.smartparking.persistence.repository.PlateRepository;

@Service
public class PlateService {

    private final PlateRepository plateRepository;

    public PlateService(final PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }

    public PlateEntity createPlate(PlateEntity plateEntity) {

        if (plateEntity.getPlate() == null) {
            throw new InvalidInputException("Invalid input -> plate", ErrorCode.PLATE_INVALID_INPUT);
        }

        return plateRepository.save(plateEntity);
    }

    public PlateEntity updatePlate(Long id, PlateEntity plateEntity) {

        if (plateEntity.getPlate() == null) {
            throw new InvalidInputException("Invalid input -> plate", ErrorCode.PLATE_INVALID_INPUT);
        }

        plateRepository.findById(id).orElseThrow(() ->
                new NotFoundPlateException("Plate not found", ErrorCode.PLATE_NOT_FOUND));
        plateEntity.setId(id);

        return plateRepository.save(plateEntity);
    }
}
