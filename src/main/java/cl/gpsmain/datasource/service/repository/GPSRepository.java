package cl.gpsmain.datasource.service.repository;

import cl.gpsmain.datasource.model.GPS;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface GPSRepository extends MongoRepository<GPS, String> {

    void deleteAllByClientId(UUID enterpriseId);

    void deleteByIdAndClientId(ObjectId id, UUID clientId);

    Object deleteById(ObjectId id);

    List<GPS> findAllByClientId(UUID clientId);


}
