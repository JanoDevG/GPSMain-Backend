package cl.gpsmain.datasource.service.admin;

import cl.gpsmain.datasource.model.Key;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.core.ActivityService;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import cl.gpsmain.datasource.service.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EnterpriseService {

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ActivityService activityService;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> enterpriseService(String option, String enterpriseName, String mail) {
        Key key = keyRepository.findByBusiness_Name(enterpriseName);
        switch (option) {
            case "CREATE":
                if (key != null) {
                    RESPONSE.setBody("La empresa ya se encuentra registrada. No se puede volver a crear una con el mismo nombre de: ".concat(enterpriseName));
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    Key keyEnterpriseNew = new Key();
                    keyEnterpriseNew.setUser(new Key.User());
                    keyEnterpriseNew.getUser().setBackoffice(UUID.randomUUID());
                    keyEnterpriseNew.getUser().setSupervisor(UUID.randomUUID());
                    keyEnterpriseNew.getUser().setManager(UUID.randomUUID());
                    keyEnterpriseNew.setOauth(new Key.OAuth());
                    keyEnterpriseNew.getOauth().setClientSecret(UUID.randomUUID());
                    keyEnterpriseNew.setBusiness(new Key.Business());
                    keyEnterpriseNew.getBusiness().setBusinessId(UUID.randomUUID());
                    keyEnterpriseNew.getBusiness().setName(enterpriseName);

                    keyRepository.insert(keyEnterpriseNew);
                    activityService.logActivity(accountRepository.findFirstByMail(mail),"Creación empresa nueva",
                            "Creación de nueva empresa: ".concat(enterpriseName).concat(" | clientSecret: ".concat(keyEnterpriseNew.getOauth().getClientSecret().toString())));
                    RESPONSE.setBody("La empresa: ".concat(enterpriseName).concat(" se a creado exitosamente junto a sus credenciales. clientSecret: ").concat(keyEnterpriseNew.getBusiness().getBusinessId().toString()));
                    RESPONSE.setStatus(HttpStatus.CREATED);
                }
                break;
            case "DELETE":
                if (key == null) {
                    RESPONSE.setBody("El nombre ingresado: ".concat(enterpriseName).concat(" no se encuentra registrado, no se puede eliminar."));
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    keyRepository.delete(key);
                    RESPONSE.setBody("La empresa: ".concat(enterpriseName).concat(" se a eliminado correctamente."));
                    RESPONSE.setStatus(HttpStatus.OK);
                }
                break;
            default:
                RESPONSE.setBody("la operación: ".concat(option).concat(" no es válida (Header: X-option)."));
                RESPONSE.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                break;
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }
}
