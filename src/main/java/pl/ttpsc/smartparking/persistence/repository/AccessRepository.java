package pl.ttpsc.smartparking.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;

import java.util.Optional;

@Repository
public interface AccessRepository extends JpaRepository<AccessEntity, Long> {

//    @Query(value = "SELECT * FROM access a, plate p WHERE a.plates_id = p.id and p.plate = :plate",
//            nativeQuery = true)
//    Optional<AccessEntity> getAccessByPlate(@Param("plate") String plate);
}
