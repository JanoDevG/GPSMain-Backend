package cl.gpsmain.datasource.service.repository;

import cl.gpsmain.datasource.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {

    Account findByMail(String mail);
}
