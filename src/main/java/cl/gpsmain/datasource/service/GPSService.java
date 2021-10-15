package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.GPS;
import cl.gpsmain.datasource.model.Key;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.core.ActivityService;
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

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> gpsService(UUID clientSecret, String mail, String option, GPS gps) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
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
                gpsRepository.deleteById(gps.getId());
                activityService.logActivity(accountSupervisor, "Elimianción GPS", "Se elimina GPS con ID: ".concat(gps.getId()));
                RESPONSE.setStatus(HttpStatus.OK);
                RESPONSE.setBody("GPS con ID: ".concat(gps.getId()).concat(" eliminado exitosamente."));
                break;
            default:
                RESPONSE.setBody("la operación: ".concat(option).concat(" no es válida (Header: X-option)."));
                RESPONSE.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                break;
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }
}
