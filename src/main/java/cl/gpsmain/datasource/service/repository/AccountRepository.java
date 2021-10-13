package cl.gpsmain.datasource.service.repository;

import cl.gpsmain.datasource.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface AccountRepository extends MongoRepository<Account, String> {

    Account findFirstByMail(String mail);

    void deleteAllByBusinessId(UUID businessId);

    void deleteByMailAndBusinessId(String mail, UUID businessId);

}
