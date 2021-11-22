package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import cl.gpsmain.datasource.service.repository.KeyRepository;
import cl.gpsmain.datasource.service.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KeyRepository keyRepository;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> loginAccount(String mail, String password) {
        Account account = loginRepository.findByMailAndPassword(mail, password);
        if (account != null) {
            RESPONSE.setStatus(HttpStatus.OK);
            // Omitir información no requerida
            account.setPassword(null);
            account.setActivity(null);
            RESPONSE.setBody(account);
        } else {
            RESPONSE.setStatus(HttpStatus.NOT_FOUND);
            RESPONSE.setBody("Cuenta no encontrada.");
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> getClientId(String mail, UUID clientId) {
        Account account = accountRepository.findFirstByBusinessIdAndMail(clientId, mail);
        if (account != null) {
            RESPONSE.setStatus(HttpStatus.OK);
            RESPONSE.setBody(keyRepository.findByBusiness_Name(account.getBusinessName()).getOauth().getClientSecret().toString());
        } else {
            RESPONSE.setStatus(HttpStatus.NOT_FOUND);
            RESPONSE.setBody("Las credenciales no coinciden.");
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }
}