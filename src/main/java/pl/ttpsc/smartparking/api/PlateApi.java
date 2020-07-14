package pl.ttpsc.smartparking.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;

@RequestMapping("/api/plate")
public interface PlateApi {

    @PutMapping("/{id}")
    ResponseEntity<PlateEntity> update(@PathVariable Long id, @RequestBody PlateEntity plateEntity);
}
