package cl.gpsmain.datasource.service.repository;

import cl.gpsmain.datasource.model.Fleet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface FleetRepository extends MongoRepository <Fleet, String> {

    void deleteAllByBusinessId(UUID businessId);

    List<Fleet> findAllByBusinessId(UUID businessId);

    void deleteByPatent(String patent);
}
