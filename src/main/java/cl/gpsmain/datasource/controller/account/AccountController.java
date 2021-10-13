package cl.gpsmain.datasource.controller.account;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("AccountController")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT},
            path = {"/create-account", "/update-account", "delete-account"})
    public ResponseEntity<Response> accountController(@RequestHeader("Xoption") String option,
                                                      @RequestHeader("XclientSecret") UUID clientSecret,
                                                      @RequestHeader("Xmail") String mail,
                                                      @RequestBody(required = false) Account account) {
        return accountService.accountService(account, clientSecret, option, mail);

    }

}
