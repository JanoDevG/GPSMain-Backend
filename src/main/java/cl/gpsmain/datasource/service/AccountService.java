package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> accountService(Account account, String clientId, String clientSecret, String option) {
        switch (option) {
            case "CREATE":
                break;
            case "UPDATE":
                break;
            case "DELETE":
                break;
            default:
                RESPONSE.setBody("la operación: ".concat(option).concat(" no es válida."));
                RESPONSE.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                break;
        }
        Account acc = accountRepository.findByMailAndBusinessName(account.getMail(), account.getBusinessName());
        if (acc != null) {
            accountRepository.insert(account);
            RESPONSE.setBody("Cuenta creada con éxito.");
            RESPONSE.setStatus(HttpStatus.CREATED);
        } else {
            RESPONSE.setBody("La cuenta ya existe.");
            RESPONSE.setStatus(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

}
