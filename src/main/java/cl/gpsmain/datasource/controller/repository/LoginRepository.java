package cl.gpsmain.datasource.controller.repository;


import cl.gpsmain.datasource.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginRepository extends MongoRepository<Account, String> {

    Account findByMailAndPassword(String mail, String password);
}
