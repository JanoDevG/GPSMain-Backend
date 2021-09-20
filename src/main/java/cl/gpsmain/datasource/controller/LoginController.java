package cl.gpsmain.datasource.controller;

import cl.gpsmain.datasource.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api/loggin")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/admin")
    public ResponseEntity<?> loginAdmin(@RequestHeader(value = "X-mail") String mail,
                                        @RequestHeader(value = "X-password") String password) {
        return loginService.loginAdmin(mail, new String(Base64.getDecoder().decode(password)));
    }

    @GetMapping("/backoffce")
    public ResponseEntity<?> loginBackOffice(@RequestHeader(value = "X-mail") String mail,
                                             @RequestHeader(value = "X-password") String password) {
        return loginService.loginBackOffice(mail, new String(Base64.getDecoder().decode(password)));
    }

    @GetMapping("/supervisor")
    public ResponseEntity<?> loginSupervisor(@RequestHeader(value = "X-mail") String mail,
                                             @RequestHeader(value = "X-password") String password) {
        return loginService.loginSupervisor(mail, new String(Base64.getDecoder().decode(password)));
    }

    @GetMapping("/manager")
    public ResponseEntity<?> loginManager(@RequestHeader(value = "X-mail") String mail,
                                          @RequestHeader(value = "X-password") String password) {
        return loginService.loginManager(mail, new String(Base64.getDecoder().decode(password)));
    }
}
