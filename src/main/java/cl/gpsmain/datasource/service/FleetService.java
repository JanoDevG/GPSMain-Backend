package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Fleet;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.core.ValidationService;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import cl.gpsmain.datasource.service.repository.FleetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FleetService {

    @Autowired
    private FleetRepository fleetRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ValidationService validationService;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> fleetService(UUID clientSecret, String mail, String option, String fleetPatent, Fleet fleet) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);

    }

    public ResponseEntity<Response> getAllFleet(UUID clientSecret, String mail) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor);
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        RESPONSE.setStatus(HttpStatus.OK);
        RESPONSE.setBody(fleetRepository.findAllByBusinessId(accountSupervisor.getBusinessId()));
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    private void validations(UUID clientId, UUID clientSecret, Account accountSupervisor) {
        RESPONSE.setBody(null);
        RESPONSE.setStatus(HttpStatus.OK);
        // Se debe autorizar OAuth2.0
        if (!accountSupervisor.getProfile().equals("supervisor")) {
            RESPONSE.setBody("Solo se permite acceder a información de los GPS desde una cuenta de supervisor");
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

        }
    }
}
