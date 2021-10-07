package cl.gpsmain.datasource.controller;

import cl.gpsmain.datasource.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping({"/admin", "/backoffce", "/supervisor", "/manager"})
    public ResponseEntity<?> loginAdmin(@RequestHeader(value = "X-mail") String mail,
                                        @RequestHeader(value = "X-password") String password){
        return loginService.loginAccount(mail, password);
    }
}