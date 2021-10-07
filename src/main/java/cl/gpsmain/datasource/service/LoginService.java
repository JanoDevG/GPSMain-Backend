package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> loginAccount(String email, String password) {
        Account account = loginRepository.findByMailAndPassword(email, password);
        if (account != null) {
            RESPONSE.setStatus(HttpStatus.OK);
            RESPONSE.setBody(account);
        } else {
            RESPONSE.setStatus(HttpStatus.NOT_FOUND);
            RESPONSE.setBody("Cuenta no encontrada.");
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

}