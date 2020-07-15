package pl.ttpsc.smartparking.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;

import java.util.List;

@RequestMapping("/api/access")
public interface AccessApi {

    @GetMapping("/{id}")
    ResponseEntity<AccessEntity> getAccessById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<List<AccessEntity>> getAllAccesses();

    @PostMapping
    ResponseEntity<AccessEntity> createAccess(@RequestBody AccessEntity accessEntity);

    @PutMapping("/{id}")
    ResponseEntity<AccessEntity> updateAccess(@RequestBody AccessEntity accessEntity, @PathVariable Long id);
}
