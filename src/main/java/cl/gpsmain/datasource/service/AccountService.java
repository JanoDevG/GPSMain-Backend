package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.config.UpdateDocumentMongoDB;
import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Activity;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.core.ActivityService;
import cl.gpsmain.datasource.service.core.ValidationService;
import cl.gpsmain.datasource.service.repository.AccountRepository;
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
    private UpdateDocumentMongoDB updateDocumentMongoDB;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ValidationService validationService;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> accountService(Account account, UUID clientSecret, String option, String mail, String mailDeleteAccount) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        Account acc;
        if (mailDeleteAccount != null){
            acc = accountRepository.findFirstByMail(mailDeleteAccount);
        }else{
            acc = accountRepository.findFirstByMail(account.getMail());
        }
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor, acc, option);
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        switch (option) {
            case "CREATE": // create account if not exist
                if (acc != null) {
                    RESPONSE.setBody("La cuenta ya existe, se debe actualizar si se requiere modificar.");
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    // completar credenciales para cuenta nueva
                    account.setBusinessId(accountSupervisor.getBusinessId());
                    account.setBusinessName(accountSupervisor.getBusinessName());
                    account.getActivity().add(new Activity(LocalDateTime.now(), "Creación de cuenta", "Cuenta nueva creada bajo el perfil de: ".concat(account.getProfile())));
                    accountRepository.insert(account);
                    activityService.logActivity(accountSupervisor, "Creación de cuenta nueva",
                            "Se crea cuenta nueva para: "
                                    .concat(account.getNames())
                                    .concat(" con el perfil de: ")
                                    .concat(account.getProfile()));
                    RESPONSE.setBody("La cuenta fue creada exitosamente.");
                    RESPONSE.setStatus(HttpStatus.CREATED);
                }
                break;
            case "UPDATE": // update account if exist
                if (acc == null) {
                    RESPONSE.setBody("La cuenta no existe. No hay datos que actualizar.");
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    account.setActivity(acc.getActivity());
                    account.setBusinessId(acc.getBusinessId());
                    account.setBusinessName(acc.getBusinessName());
                    account.setGPSAssigned(acc.getGPSAssigned());
                    updateDocumentMongoDB.updateDocument(account);
                    activityService.logActivity(accountSupervisor, "Actualización de cuenta",
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
                    accountRepository.deleteByMailAndBusinessId(acc.getMail(), acc.getBusinessId());
                    activityService.logActivity(accountSupervisor, "Eliminación de cuenta",
                            "Se elimina la cuenta de: ".concat(acc.getNames()));
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

    public ResponseEntity<Response> returnAccounts(UUID clientSecret, String mail, String profile) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor, null, "");
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        RESPONSE.setStatus(HttpStatus.OK);
        RESPONSE.setBody(accountRepository.findAllByBusinessIdAndProfile(accountSupervisor.getBusinessId(), profile));
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> returnAccount(UUID clientSecret, String mail, String mailAccount) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor, null, "");
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        RESPONSE.setStatus(HttpStatus.OK);
        RESPONSE.setBody(accountRepository.findFirstByBusinessIdAndMail(accountSupervisor.getBusinessId(), mailAccount));
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    private void validations(UUID clientId, UUID clientSecret, Account accountSupervisor, Account account, String option) {
        RESPONSE.setBody(null);
        RESPONSE.setStatus(HttpStatus.OK);
        // Se debe autorizar OAuth2.0
        if (!accountSupervisor.getProfile().equals("supervisor")) {
            RESPONSE.setBody("Solo se permite acceder a información de las cuentas desde una cuenta de supervisor");
            RESPONSE.setStatus(HttpStatus.UNAUTHORIZED);
            return;
        }
        if (validationService.validateClientSecret(clientId, clientSecret)) {
            RESPONSE.setBody("el clientSecret no es válido para el clientId informado");
            RESPONSE.setStatus(HttpStatus.UNAUTHORIZED);
            return;
        }
        // La cuenta X debe existir para proceder con cualquier operción
        if (accountSupervisor == null) {
            RESPONSE.setBody("El Mail informado no se encuentra registrado, no se permiten operaciones con cuentas no registradas previamente.");
            RESPONSE.setStatus(HttpStatus.UNAUTHORIZED);
            return;
        }
        // Solo se puede eliminar o actualziar cuentas de la misma organziación
        if (option.equals("UPDATE") || option.equals("DELETE") && account != null) {
            if (!accountSupervisor.getBusinessId().equals(account.getBusinessId())) {
                RESPONSE.setBody("La cuenta que se desea actualziar no es parte de la organziación.");
                RESPONSE.setStatus(HttpStatus.UNAUTHORIZED);
            }
        }
    }

}
