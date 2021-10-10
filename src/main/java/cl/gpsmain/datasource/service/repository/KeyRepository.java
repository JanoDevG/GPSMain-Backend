package cl.gpsmain.datasource.service.repository;

import cl.gpsmain.datasource.model.Key;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface KeyRepository extends MongoRepository<Key, String> {

    Key findByOAuth_ClientIdAndOAuth_ClientSecret(UUID clientId, UUID ClientSecret);

    Key findByBusiness_Name(String businessName);

}
