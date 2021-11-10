package cl.gpsmain.datasource.controller.supervisor;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.GPS;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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
                                                      @RequestBody(required = false) Account account,
                                                      @RequestParam(value = "mail", required = false) String mailDeleteAccount) {
        return accountService.accountService(account, clientSecret, option, mail, mailDeleteAccount);

    }

    @GetMapping(path = "/get-all-accounts")
    public ResponseEntity<Response> returnAccounts(@RequestHeader("Xmail") String mail,
                                                   @RequestHeader("XclientSecret") UUID clientSecret,
                                                   @RequestHeader("Xprofile") String profile) {
        return accountService.returnAccounts(clientSecret, mail, profile);
    }

    @GetMapping(path = "/get-account")
    public ResponseEntity<Response> returnAccount(@RequestHeader("Xmail") String mail,
                                                  @RequestHeader("XclientSecret") UUID clientSecret,
                                                  @RequestHeader("XmailAccount") String mailAccount) {
        return accountService.returnAccount(clientSecret, mail, mailAccount);
    }

    @PostMapping(path = "/assigned-gps")
    public ResponseEntity<Response> assignedGPS(@RequestHeader("Xmail") String mail,
                                                @RequestHeader("XclientSecret") UUID clientSecret,
                                                @RequestHeader("XmailAccount") String mailAccount,
                                                @RequestHeader("XOption") String option,
                                                @RequestBody() GPS gps) {
        return accountService.assignedGPS(clientSecret, mail, gps, mailAccount, option);
    }

}
