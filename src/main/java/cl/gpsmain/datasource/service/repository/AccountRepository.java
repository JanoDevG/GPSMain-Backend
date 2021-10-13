package cl.gpsmain.datasource.service.repository;

import cl.gpsmain.datasource.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountRepository extends MongoRepository<Account, String> {

    Account findFirstByMail(String mail);
}
