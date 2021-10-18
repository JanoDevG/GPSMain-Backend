package cl.gpsmain.datasource.service.admin;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Activity;
import cl.gpsmain.datasource.model.Key;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.core.ActivityService;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import cl.gpsmain.datasource.service.repository.FleetRepository;
import cl.gpsmain.datasource.service.repository.GPSRepository;
import cl.gpsmain.datasource.service.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EnterpriseService {

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private GPSRepository gpsRepository;

    @Autowired
    private FleetRepository fleetRepository;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> enterpriseService(String option, String enterpriseName, String mail, Account account) {
        Key key = keyRepository.findByBusiness_Name(enterpriseName);
        Account accountAdmin = accountRepository.findFirstByMail(mail);
        switch (option) {
            case "CREATE":
                if (key != null) {
                    RESPONSE.setBody("La empresa ya se encuentra registrada. No se puede volver a crear una con el mismo nombre de: ".concat(enterpriseName));
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    // Creación y registro empresa
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
                    activityService.logActivity(accountAdmin, "Creación empresa nueva",
                            "Creación de nueva empresa: ".concat(enterpriseName).concat(" | clientSecret: ".concat(keyEnterpriseNew.getOauth().getClientSecret().toString())));
                    // Creación y registro cliente
                    account.setBusinessName(enterpriseName);
                    account.setBusinessId(keyEnterpriseNew.getBusiness().getBusinessId());
                    account.setProfile("supervisor");
                    account.getActivity().add(new Activity(LocalDateTime.now(), "Creación cuenta nueva", "Se crea cuenta como primer supervisor de la empresa:".concat(account.getBusinessName())));
                    accountRepository.insert(account);
                    activityService.logActivity(accountAdmin, "Creción primer supervisor", "Se crea el primer supervisor para la empresa: "
                            .concat(enterpriseName)
                            .concat(" | Superviso: ".concat(account.getNames())));
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
                    activityService.logActivity(accountAdmin,"Eliminación empresa: ".concat(key.getBusiness().getName()),
                            "Se elimina empresa junto a todas sus credenciales");
                    accountRepository.deleteAllByBusinessId(key.getBusiness().getBusinessId());
                    activityService.logActivity(accountAdmin, "eliminación cuentas empresa: ".concat(key.getBusiness().getName()),
                            "Se eliminan todas las cuentas asociadas a la empresa: ".concat(key.getBusiness().getName()));
                    gpsRepository.deleteAllByClientId(key.getBusiness().getBusinessId());
                    activityService.logActivity(accountAdmin, "Eliminación dispositivos GPS",
                            "Se eliminan todos los GPS asociados a la empresa: ".concat(key.getBusiness().getName()));
                    fleetRepository.deleteAllByBusinessId(key.getBusiness().getBusinessId());
                    activityService.logActivity(accountAdmin, "Eliminación de flotas",
                            "Se eliminan todas las flotas asociadas a la empresa: ".concat(key.getBusiness().getName()));
                    RESPONSE.setBody("La empresa: ".concat(enterpriseName).concat(" se a eliminado correctamente junto a todas sus cuentas, GPS y flotas registradas ."));
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

    public int countBusiness(){
        return keyRepository.findAll().size();
    }

    public int countUsers(){
        return accountRepository.findAll().size();
    }
}
