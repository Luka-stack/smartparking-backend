package pl.ttpsc.smartparking.persistence.service;

import org.springframework.stereotype.Service;
import pl.ttpsc.smartparking.error.exception.ErrorCode;
import pl.ttpsc.smartparking.error.exception.InvalidInputException;
import pl.ttpsc.smartparking.error.exception.NotFoundAccessException;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.repository.AccessRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccessService {

    private final AccessRepository accessRepository;

    public AccessService(final AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    public AccessEntity getAccessById(Long id) {

        return accessRepository.findById(id).orElseThrow(() ->
                new NotFoundAccessException("Access not found", ErrorCode.ACCESS_NOT_FOUND));
    }

    public List<AccessEntity> getAllAccesses() {

        return accessRepository.findAll();
    }

//    public AccessEntity getAccessByPlate(String plate) {
//
//        Optional<AccessEntity> accessEntity = accessRepository.getAccessByPlate(plate);
//
//        if (!accessEntity.isPresent()) {
//            throw new NotFoundAccessException("Access not found", ErrorCode.ACCESS_NOT_FOUND);
//        }
//
//        return accessEntity.get();
//    }

    public AccessEntity createAccess(AccessEntity accessEntity) {

        if (accessEntity.getDateFrom() == null) {
            throw new InvalidInputException("Invalid input -> dateFrom", ErrorCode.ACCESS_INVALID_INPUT);
        }

        if (accessEntity.getDateTo() == null) {
            throw new InvalidInputException("Invalid input -> dateTo", ErrorCode.ACCESS_INVALID_INPUT);
        }

        if (accessEntity.getPlate() == null) {
            throw new InvalidInputException("Invalid input -> plate", ErrorCode.ACCESS_INVALID_INPUT);
        }

        return accessRepository.save(accessEntity);
    }

    public AccessEntity updateAccess(Long id, AccessEntity accessEntity) {

        if (accessEntity.getDateFrom() == null) {
            throw new InvalidInputException("Invalid input -> dateFrom", ErrorCode.ACCESS_INVALID_INPUT);
        }

        if (accessEntity.getDateTo() == null) {
            throw new InvalidInputException("Invalid input -> dateTo", ErrorCode.ACCESS_INVALID_INPUT);
        }

        accessRepository.findById(id).orElseThrow(() ->
                new NotFoundAccessException("Access not found", ErrorCode.ACCESS_NOT_FOUND));
        accessEntity.setId(id);

        return accessRepository.save(accessEntity);
    }

    public void deleteAccessById(Long id) {

        accessRepository.deleteById(id);
    }
}
