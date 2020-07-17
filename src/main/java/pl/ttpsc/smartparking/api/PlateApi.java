package pl.ttpsc.smartparking.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;

import java.util.List;

@RequestMapping("/api/plate")
public interface PlateApi {

    @GetMapping("/{id}")
    ResponseEntity<PlateEntity> getPlateById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<List<PlateEntity>> getAllPlate();

    @PostMapping
    ResponseEntity<PlateEntity> createPlate(@RequestBody PlateEntity plateEntity);

    @PutMapping("/{id}")
    ResponseEntity<PlateEntity> updatePlate(@PathVariable Long id, @RequestBody PlateEntity plateEntity);

    @DeleteMapping("/{id}")
    ResponseEntity<?> deletePlate(@PathVariable Long id);
}
