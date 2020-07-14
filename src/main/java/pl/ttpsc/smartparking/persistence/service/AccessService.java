package pl.ttpsc.smartparking.persistence.service;

import org.springframework.stereotype.Service;
import pl.ttpsc.smartparking.error.exception.ErrorCode;
import pl.ttpsc.smartparking.error.exception.InvalidInputException;
import pl.ttpsc.smartparking.error.exception.NotFoundAccessException;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.repository.AccessRepository;

@Service
public class AccessService {

    private final AccessRepository accessRepository;

    public AccessService(final AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    public AccessEntity update(Long id, AccessEntity accessEntity) {

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
}
