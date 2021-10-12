package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Activity;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.core.ActivityService;
import cl.gpsmain.datasource.service.core.ValidationService;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import cl.gpsmain.datasource.service.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ValidationService validationService;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> accountService(Account account, UUID clientSecret, String option, String mail) {
        Account accountApplicant = accountRepository.findByMail(mail);
        Account acc = accountRepository.findByMail(account.getMail());
        validations(accountApplicant.getBusinessId(), clientSecret, accountApplicant, mail);
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        switch (option) {
            case "CREATE": // create account if not exist
                if (acc != null) {
                    RESPONSE.setBody("La cuenta ya existe, se debe actualizar si se requiere modificar.");
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    // completar credenciales para cuenta nueva
                    account.setBusinessId(accountApplicant.getBusinessId());
                    account.setBusinessName(accountApplicant.getBusinessName());
                    account.getActivity().add(new Activity(LocalDateTime.now(), "Creación de cuenta", "Cuenta nueva creada bajo el perfil de: ".concat(account.getProfile())));
                    //TODO se cae en el log de actividad
                    accountRepository.insert(account);
                    activityService.logActivity(accountApplicant, "Creación de cuenta nueva",
                            "Se crea cuenta nueva para: "
                                    .concat(account.getNames())
                                    .concat(" con el perfil de: ")
                                    .concat(account.getProfile()));
                    RESPONSE.setBody("La cuenta fue creada exitosamente.");
                    RESPONSE.setStatus(HttpStatus.OK);
                }
                break;
            case "UPDATE": // update account if exist
                if (acc == null) {
                    RESPONSE.setBody("La cuenta no existe. No hay datos que actualizar.");
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    accountRepository.save(account);
                    activityService.logActivity(accountApplicant, "Actualización de cuenta",
                            "Se actualiza cuenta para: ".concat(account.getNames()));
                    RESPONSE.setBody("Cuenta actualizada exitosamente.");
                    RESPONSE.setStatus(HttpStatus.OK);
                }
                break;
            case "DELETE": // delete account if exist
                if (acc == null) {
                    RESPONSE.setBody("La cuenta no existe. No hay datos que eliminar.");
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    accountRepository.delete(acc);
                    activityService.logActivity(accountApplicant, "Eliminación de cuenta",
                            "Se elimina la cuenta de: ".concat(account.getNames()));
                    RESPONSE.setBody("Cuenta actualizada exitosamente.");
                    RESPONSE.setBody("La cuenta de: ".concat(acc.getNames()).concat(" fue eliminada exitosamente."));
                    RESPONSE.setStatus(HttpStatus.OK);
                }
                break;
            default:
                RESPONSE.setBody("la opción: ".concat(option).concat(" no es válida (Header: X-option)."));
                RESPONSE.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                break;
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    private void validations(UUID clientId, UUID clientSecret, Account account, String mail) {
        RESPONSE.setBody(null);
        RESPONSE.setStatus(HttpStatus.OK);
        if (validationService.validateClientSecret(clientId, clientSecret)) { // Se debe autorizar OAuth2.0
            RESPONSE.setBody("el clientSecret no es válido para el clientId informado");
            RESPONSE.setStatus(HttpStatus.UNAUTHORIZED);
            return;
        }
        if (account == null) { // La cuenta X debe existir para proceder con cualquier operción
            RESPONSE.setBody("El Mail informado no se encuentra registrado, no se permiten operaciones con cuentas no registradas previamente.");
            RESPONSE.setStatus(HttpStatus.UNAUTHORIZED);
            return;
        }
        //TODO supervisor solo puede actualziar y eliminar cuentas de su empresa (crear asume el código que es para su empresa)
    }
}
