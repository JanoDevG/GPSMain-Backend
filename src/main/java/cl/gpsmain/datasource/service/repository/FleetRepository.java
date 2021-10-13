package cl.gpsmain.datasource.service.repository;

import cl.gpsmain.datasource.model.Fleet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface FleetRepository extends MongoRepository <Fleet, String> {

    void deleteAllByBusinessId(UUID businessId);
}
