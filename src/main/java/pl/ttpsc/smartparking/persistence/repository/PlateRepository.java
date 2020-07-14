package pl.ttpsc.smartparking.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;

@Repository
public interface PlateRepository extends JpaRepository<PlateEntity, Long> {

}
