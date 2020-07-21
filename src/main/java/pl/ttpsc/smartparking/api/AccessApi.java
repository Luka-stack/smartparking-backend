package pl.ttpsc.smartparking.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;

import java.util.List;

@RequestMapping("/api/accesses")
public interface AccessApi {

    @GetMapping("/{id}")
    ResponseEntity<AccessEntity> getAccessById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<List<AccessEntity>> getAllAccesses();

    @GetMapping("/alpr/{plate}")
    ResponseEntity<AccessEntity> getAccessByPlate(@PathVariable String plate);

    @PostMapping
    ResponseEntity<AccessEntity> createAccess(@RequestBody AccessEntity accessEntity);

    @PutMapping("/{id}")
    ResponseEntity<AccessEntity> updateAccess(@RequestBody AccessEntity accessEntity, @PathVariable Long id);

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteAccess(@PathVariable Long id);
}
