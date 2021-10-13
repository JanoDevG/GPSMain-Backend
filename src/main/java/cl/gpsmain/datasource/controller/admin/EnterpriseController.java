package cl.gpsmain.datasource.controller.admin;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.admin.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("EnterpriseController")
@RequestMapping(value = "/api/admin/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Response> enterpriseController(@RequestHeader("Xoption") String option,
                                                         @RequestHeader("Xmail") String mail,
                                                         @RequestHeader("XenterpriseName") String enterpriseName,
                                                         @RequestBody(required = false) Account account) {
        return enterpriseService.enterpriseService(option, enterpriseName, mail, account);

    }
}
