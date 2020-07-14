package pl.ttpsc.smartparking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.ttpsc.smartparking.api.AccessApi;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.service.AccessService;

@RestController
public class AccessController implements AccessApi {

    private final AccessService accessService;

    public AccessController(final AccessService accessService) {
        this.accessService = accessService;
    }

    @Override
    public ResponseEntity<AccessEntity> update(AccessEntity accessEntity, Long id) {

        AccessEntity updatedAccess = accessService.update(id, accessEntity);

        return ResponseEntity.ok(updatedAccess);
    }
}
