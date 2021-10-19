package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.GPS;
import cl.gpsmain.datasource.model.Key;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.core.ActivityService;
import cl.gpsmain.datasource.service.core.ValidationService;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import cl.gpsmain.datasource.service.repository.GPSRepository;
import cl.gpsmain.datasource.service.repository.KeyRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GPSService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private GPSRepository gpsRepository;

    @Autowired
    private ValidationService validationService;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> gpsService(UUID clientSecret, String mail, String option, String gpsId) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor);
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        switch (option) {
            case "CREATE":
                GPS newGps = new GPS();
                newGps.setId(new ObjectId().toString());
                newGps.setClientId(accountSupervisor.getBusinessId());
                newGps.setClientSecretId(clientSecret);
                gpsRepository.insert(newGps);
                activityService.logActivity(accountSupervisor, "Creación GPS", "Creación de nuevo dispositivo GPS | ID: ".concat(newGps.getId()));
                RESPONSE.setStatus(HttpStatus.CREATED);
                RESPONSE.setBody("GPS Creado exitosamente. ID:".concat(newGps.getId()));
                break;
            case "GET":
                List<GPS> GPSs = gpsRepository.findAllByClientId(accountSupervisor.getBusinessId());
                RESPONSE.setStatus(HttpStatus.OK);
                RESPONSE.setBody(GPSs);
                break;
            case "DELETE":
                gpsRepository.deleteById(gpsId);
                activityService.logActivity(accountSupervisor, "Elimianción GPS", "Se elimina GPS con ID: ".concat(gpsId));
                RESPONSE.setStatus(HttpStatus.OK);
                RESPONSE.setBody("GPS con ID: ".concat(gpsId).concat(" eliminado exitosamente."));
                break;
            default:
                RESPONSE.setBody("la operación: ".concat(option).concat(" no es válida (Header: X-option)."));
                RESPONSE.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                break;
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> getAllGPS(UUID clientSecret, String mail) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor);
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        RESPONSE.setStatus(HttpStatus.OK);
        RESPONSE.setBody(gpsRepository.findAllByClientId(accountSupervisor.getBusinessId()));
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
