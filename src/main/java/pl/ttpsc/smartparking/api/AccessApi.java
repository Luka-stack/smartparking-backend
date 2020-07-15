package pl.ttpsc.smartparking.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;

@RequestMapping("/api/access")
public interface AccessApi {

    @PostMapping
    ResponseEntity<AccessEntity> createAccess(@RequestBody AccessEntity accessEntity);

    @PutMapping("/{id}")
    ResponseEntity<AccessEntity> updateAccess(@RequestBody AccessEntity accessEntity, @PathVariable Long id);
}
