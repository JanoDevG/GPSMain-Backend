package cl.gpsmain.datasource.service.repository;

import cl.gpsmain.datasource.model.GPS;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface GPSRepository extends MongoRepository<GPS, String> {

    void deleteAllByClientId(UUID enterpriseId);
}
