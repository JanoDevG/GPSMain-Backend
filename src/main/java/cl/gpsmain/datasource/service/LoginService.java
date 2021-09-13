package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.controller.repository.LoginRepository;
import cl.gpsmain.datasource.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public ResponseEntity<?> loginAdmin(String email, String password) {
        Account account = loginRepository.findByMailAndPassword(email, password);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> loginBackOffice(String email, String password) {
        Account account = loginRepository.findByMailAndPassword(email, password);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> loginSupervisor(String email, String password) {
        Account account = loginRepository.findByMailAndPassword(email, password);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> loginManager(String email, String password) {
        Account account = loginRepository.findByMailAndPassword(email, password);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
    }
}
