package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.config.UpdateDocumentMongoDB;
import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Fleet;
import cl.gpsmain.datasource.model.GPS;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.core.ActivityService;
import cl.gpsmain.datasource.service.core.ValidationService;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import cl.gpsmain.datasource.service.repository.FleetRepository;
import cl.gpsmain.datasource.service.repository.GPSRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FleetService {

    @Autowired
    private FleetRepository fleetRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private GPSRepository gpsRepository;

    @Autowired
    private UpdateDocumentMongoDB updateDocumentMongoDB;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> fleetService(UUID clientSecret, String mail, String option, String fleetPatent, Fleet fleet) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor);
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        switch (option) {
            case "CREATE":
                fleet.setId(new ObjectId().toString());
                fleet.setBusinessId(accountSupervisor.getBusinessId());
                fleetRepository.insert(fleet);
                activityService.logActivity(accountSupervisor, "Creación Flota", "Creación de nueva Flota | ID: ".concat(fleet.getId()));
                RESPONSE.setStatus(HttpStatus.CREATED);
                RESPONSE.setBody("flota Creada exitosamente. ID: ".concat(fleet.getId()));
                break;
            case "DELETE":
                fleetRepository.deleteByPatent(fleetPatent);
                activityService.logActivity(accountSupervisor, "Elimianción Flota", "Flota con patente: ".concat(fleetPatent));
                RESPONSE.setStatus(HttpStatus.OK);
                RESPONSE.setBody("Flota con patente: ".concat(fleetPatent).concat(" eliminado exitosamente."));
                break;
            default:
                RESPONSE.setBody("la operación: ".concat(option).concat(" no es válida (Header: Xoption)."));
                RESPONSE.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                break;
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
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

    public ResponseEntity<Response> assignedFleet(UUID clientSecret, String mail, String option, String fleetPatent) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor);
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        Fleet fleet;
        switch (option) {
            case "ASSIGNER":
                fleet = fleetRepository.findByPatent(fleetPatent);
                if (fleet.getGpsAssigned() != null) {
                    RESPONSE.setBody("No se puede asignar GPS a la flota porque ya tiene uno asignado | ID GPS Asignado: ".concat(fleet.getGpsAssigned()));
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                    break;
                }
                List<GPS> gPSs = gpsRepository.findAllByIsInstalledAndClientId(false, accountSupervisor.getBusinessId());
                if (gPSs.size() == 0) {
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                    RESPONSE.setBody("No hay GPSs disponibles (sin instalar) registrados en su empresa. Debe solicitar nuevas unidades antes de asignar uno nuevo.");
                    break;
                }
                fleet.setGpsAssigned(gPSs.get(0).getId()); // Se asigna el primero disponible
                gPSs.get(0).setInstalled(true);
                updateDocumentMongoDB.updateGPS(gPSs.get(0));
                updateDocumentMongoDB.updateFleet(fleet);
                activityService.logActivity(accountSupervisor, "Vinculación GPS a flota", "Se vincula GPS con ID: "
                        .concat(fleet.getGpsAssigned())
                        .concat(" a flota con patente: ")
                        .concat(fleet.getPatent()));
                RESPONSE.setStatus(HttpStatus.OK);
                RESPONSE.setBody("A la flota con patente: ".concat(fleetPatent).concat(" Se le acaba de asignar el GPS con ID: ".concat(fleet.getGpsAssigned())));
                break;
            case "REMOVE":
                fleet = fleetRepository.findByPatent(fleetPatent);
                if (fleet.getGpsAssigned() == null) {
                    RESPONSE.setBody("La flota con patente: ".concat(fleet.getPatent()).concat(" no tiene un GPS Asignado"));
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                    break;
                }
                GPS gps = gpsRepository.findByIdAndClientId(new ObjectId(fleet.getGpsAssigned()), accountSupervisor.getBusinessId());
                fleet.setGpsAssigned(null);
                gps.setInstalled(false);
                gps.setActive(false);
                activityService.logActivity(accountSupervisor, "Desvinculación GPS a flota", "Se desvincula GPS con ID: "
                        .concat(gps.getId())
                        .concat(" de la flota con patente: ")
                        .concat(fleet.getId()));
                RESPONSE.setStatus(HttpStatus.OK);
                RESPONSE.setBody("A la flota con patente: "
                        .concat(fleet.getPatent())
                        .concat(" se le ha removido el GPS con ID: ")
                        .concat(gps.getId()));
                updateDocumentMongoDB.updateFleet(fleet);
                updateDocumentMongoDB.updateGPS(gps);
                break;
            default:
                RESPONSE.setBody("la operación: ".concat(option).concat(" no es válida (Header: Xoption)."));
                RESPONSE.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                break;
        }
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
