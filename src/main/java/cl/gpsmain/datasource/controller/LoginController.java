package cl.gpsmain.datasource.controller;

import cl.gpsmain.datasource.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loggin")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/admin")
    public ResponseEntity<?> loginAdmin(@RequestHeader(value = "X-mail") String mail,
                                        @RequestHeader(value = "X-password") String password) {
        return loginService.loginAdmin(mail, password);
    }

    @GetMapping("/backoffce")
    public ResponseEntity<?> loginBackOffice(@RequestHeader(value = "X-mail") String mail,
                                             @RequestHeader(value = "X-password") String password) {
        return loginService.loginBackOffice(mail, password);
    }

    @GetMapping("/supervisor")
    public ResponseEntity<?> loginSupervisor(@RequestHeader(value = "X-mail") String mail,
                                             @RequestHeader(value = "X-password") String password) {
        return loginService.loginSupervisor(mail, password);
    }

    @GetMapping("/manager")
    public ResponseEntity<?> loginManager(@RequestHeader(value = "X-mail") String mail,
                                          @RequestHeader(value = "X-password") String password) {
        return loginService.loginManager(mail, password);
    }
}
