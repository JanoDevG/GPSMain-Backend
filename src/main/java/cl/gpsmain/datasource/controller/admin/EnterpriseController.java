package cl.gpsmain.datasource.controller.admin;


import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.admin.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("EnterpriseController")
@RequestMapping(value = "/api/admin/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    public ResponseEntity<Response> enterpriseController(@RequestHeader("X-ClientSecret") String clientSecret,
                                                         @RequestHeader("X-ClientId") String clientId,
                                                         @RequestHeader("X-option") String option,
                                                         @RequestHeader("X-enterpriseName") String enterpriseName) {
        return enterpriseService.enterpriseService(option, clientId, clientSecret, enterpriseName);

    }
}
