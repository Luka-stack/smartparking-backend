package pl.ttpsc.smartparking.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;

@Repository
public interface AccessRepository extends JpaRepository<AccessEntity, Long> {

}
