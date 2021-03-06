package cl.gpsmain.datasource.controller.account;

import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("LoginController")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(path = "/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping()
    public ResponseEntity<Response> loginAdmin(@RequestHeader(value = "Xmail") String mail,
                                               @RequestHeader(value = "Xpassword") String password){
        return loginService.loginAccount(mail, password);
    }


}