package pl.ttpsc.smartparking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.ttpsc.smartparking.api.PlateApi;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;
import pl.ttpsc.smartparking.persistence.service.PlateService;

@RestController
public class PlateController implements PlateApi {

    private final PlateService plateService;

    public PlateController(final PlateService plateService) {
        this.plateService = plateService;
    }

    @Override
    public ResponseEntity<PlateEntity> createPlate(PlateEntity plateEntity) {

        PlateEntity createdPlate = plateService.createPlate(plateEntity);

        return ResponseEntity.ok(createdPlate);
    }

    @Override
    public ResponseEntity<PlateEntity> updatePlate(Long id, PlateEntity plateEntity) {

        PlateEntity updatedPlate = plateService.updatePlate(id, plateEntity);

        return ResponseEntity.ok(updatedPlate);
    }
}
