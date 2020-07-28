package pl.ttpsc.smartparking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.ttpsc.smartparking.api.AccessApi;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.service.AccessService;

import java.util.List;

@RestController
public class AccessController implements AccessApi {

    private final AccessService accessService;

    public AccessController(final AccessService accessService) {
        this.accessService = accessService;
    }

    @Override
    public ResponseEntity<AccessEntity> getAccessById(Long id) {

        return ResponseEntity.ok(accessService.getAccessById(id));
    }

    @Override
    public ResponseEntity<List<AccessEntity>> getAllAccesses() {

        return ResponseEntity.ok(accessService.getAllAccesses());
    }

    @Override
    public ResponseEntity<AccessEntity> getAccessByPlate(String plate) {

        AccessEntity accessEntity = accessService.getAccessByPlate(plate);

        return ResponseEntity.ok(accessEntity);
    }

    @Override
    public ResponseEntity<AccessEntity> createAccess(AccessEntity accessEntity) {

        return ResponseEntity.ok(accessService.createAccess(accessEntity));
    }

    @Override
    public ResponseEntity<AccessEntity> updateAccess(AccessEntity accessEntity, Long id) {

        return ResponseEntity.ok(accessService.updateAccess(id, accessEntity));
    }

    @Override
    public ResponseEntity<?> deleteAccess(Long id) {

        accessService.deleteAccessById(id);

        return ResponseEntity.ok().body(null);
    }
}
