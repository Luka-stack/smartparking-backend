package pl.ttpsc.smartparking.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;

@RequestMapping("/api/plate")
public interface PlateApi {

    @PostMapping
    ResponseEntity<PlateEntity> createPlate(@RequestBody PlateEntity plateEntity);

    @PutMapping("/{id}")
    ResponseEntity<PlateEntity> updatePlate(@PathVariable Long id, @RequestBody PlateEntity plateEntity);
}
