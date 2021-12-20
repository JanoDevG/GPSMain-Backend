package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.config.UpdateDocumentMongoDB;
import cl.gpsmain.datasource.model.*;
import cl.gpsmain.datasource.service.core.ActivityService;
import cl.gpsmain.datasource.service.core.ValidationService;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import cl.gpsmain.datasource.service.repository.FleetRepository;
import cl.gpsmain.datasource.service.repository.GPSRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

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

    @Autowired
    private FleetRepository fleetRepository;

    @Autowired
    private UpdateDocumentMongoDB updateDocumentMongoDB;

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
                Fleet fleet = fleetRepository.findByGpsAssigned(gpsId);
                if (fleet != null) {
                    fleet.setGpsAssigned(null);
                    updateDocumentMongoDB.updateFleet(fleet);
                }
                gpsRepository.deleteById(new ObjectId(gpsId));
                activityService.logActivity(accountSupervisor, "Elimianción GPS", "Se elimina GPS con ID: ".concat(gpsId));
                RESPONSE.setStatus(HttpStatus.OK);
                RESPONSE.setBody("GPS con ID: ".concat(gpsId).concat(" eliminado exitosamente."));
                break;
            default:
                RESPONSE.setBody("la operación: ".concat(option).concat(" no es válida (Header: Xoption)."));
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

    public ResponseEntity<Response> getAllGPSAvailable(String mail) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        RESPONSE.setStatus(HttpStatus.OK);
        RESPONSE.setBody(gpsRepository.countByIsInstalledAndClientId(false, accountSupervisor.getBusinessId()));
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> activateGPS(UUID clientSecret, String mail, GPS gps) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor);
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        gps.setActive(true);
        Fleet fleet = fleetRepository.findByGpsAssigned(gps.getId());
        if (fleet != null) {
            fleet.setStatusGPS(true);
            updateDocumentMongoDB.updateFleet(fleet);
        }
        activityService.logActivity(accountSupervisor, "Activación de GPS", "Se activa GPS con ID: "
                .concat(gps.getId()));
        RESPONSE.setStatus(HttpStatus.OK);
        RESPONSE.setBody("Se activa GPS con ID: ".concat(gps.getId()));
        updateDocumentMongoDB.updateGPS(gps);
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> invalidateGPS(UUID clientSecret, String mail, GPS gps) {
        Account accountSupervisor = accountRepository.findFirstByMail(mail);
        validations(accountSupervisor.getBusinessId(), clientSecret, accountSupervisor);
        if (RESPONSE.getStatus().isError())
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus()); // capa de validaciones no aprobada se detiene flujo para enviar Response
        gps.setActive(false);
        Fleet fleet = fleetRepository.findByGpsAssigned(gps.getId());
        if (fleet != null) {
            fleet.setStatusGPS(false);
            updateDocumentMongoDB.updateFleet(fleet);
        }
        activityService.logActivity(accountSupervisor, "Desactivación de GPS", "Se desactiva GPS con ID: "
                .concat(gps.getId()));
        RESPONSE.setStatus(HttpStatus.OK);
        RESPONSE.setBody("Se desactiva GPS con ID: ".concat(gps.getId()));
        updateDocumentMongoDB.updateGPS(gps);
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> insertCoordinates(Trip trip) {
        Fleet fleet = fleetRepository.findByPatent(trip.getPatent());
        if (fleet != null) {
            int indexUpdating = IntStream.range(0, fleet.getTrip().size()).filter(value -> trip.getDestiny().getDestiny().equals(fleet.getTrip().get(value).getDestiny().getDestiny())).findFirst().orElse(-1);
            if (indexUpdating >= 0) {
                fleet.getTrip().set(indexUpdating, trip);
            }
            updateDocumentMongoDB.updateFleet(fleet);
            RESPONSE.setBody("Se agregó coordenadas para el viaje de: ".concat(trip.getDeparture().getDeparture()).concat(" hacia: ").concat(trip.getDestiny().getDestiny()));
            RESPONSE.setStatus(HttpStatus.OK);
        } else {
            RESPONSE.setBody("No se encontró flota con la patente: ".concat(trip.getPatent()));
            RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> insertTrip(Trip trip) {
        Fleet fleet = fleetRepository.findByPatent(trip.getPatent());
        if (fleet != null) {
            fleet.getTrip().add(trip);
            updateDocumentMongoDB.updateFleet(fleet);
            RESPONSE.setBody("Se agregó el nuevo viaje para la flota: ".concat(fleet.getPatent()));
            RESPONSE.setStatus(HttpStatus.OK);
        } else {
            RESPONSE.setBody("No se encontró flota con la patente: ".concat(trip.getPatent()));
            RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> insertNewRoute(String[] route) {
        Fleet fleet = fleetRepository.findByPatent(route[2]);
        if (fleet != null) {
            Trip trip = new Trip();
            trip.getDeparture().setDeparture(route[0]);
            trip.getDestiny().setDestiny(route[1]);
            fleet.getTrip().add(trip);
            updateDocumentMongoDB.updateFleet(fleet);
            RESPONSE.setBody("Ruta añadida correctamente");
            RESPONSE.setStatus(HttpStatus.OK);
        } else {
            RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
            RESPONSE.setBody("No se encuentra la patente buscada");
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> getGPSAccountAndBusiness(String mailSupervisor, String mailAccount) {
        Account accountSupervisor = accountRepository.findFirstByMail(mailSupervisor);
        List<GPS> gpsAccount = accountRepository.findFirstByMail(mailAccount).getGPSAssigned();
        List<GPS> gpsBusiness = gpsRepository.findAllByClientId(accountSupervisor.getBusinessId());
        for (GPS gps : gpsAccount) {
            gpsBusiness.removeIf(gps2 -> gps2.getId().equals(gps.getId()));
        }
        ResponseGPSs responseGPSs = new ResponseGPSs();
        responseGPSs.setGpsAccount(gpsAccount);
        responseGPSs.setGpsBusiness(gpsBusiness);
        RESPONSE.setBody(responseGPSs);
        RESPONSE.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> deleteTrip(String patent, Trip trip) {
        Fleet fleet = fleetRepository.findByPatent(patent);
        if (fleet != null) {
            int indexUpdating = -1;
            for (int i = 0; i < fleet.getTrip().size(); i++) {
                if (trip.getDestiny().getDestiny().equals(fleet.getTrip().get(i).getDestiny().getDestiny())) {
                    if (trip.getDeparture().getDeparture().equals(fleet.getTrip().get(i).getDeparture().getDeparture()))
                        if (trip.getCoordinates().size() == fleet.getTrip().get(i).getCoordinates().size())
                            indexUpdating = i;
                    break;
                }
            }
            if (indexUpdating >= 0) {
                fleet.getTrip().remove(indexUpdating);
                updateDocumentMongoDB.updateFleet(fleet);
                RESPONSE.setBody(null);
                RESPONSE.setStatus(HttpStatus.OK);
            } else {
                RESPONSE.setBody(null);
                RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
            }
        } else {
            RESPONSE.setBody(null);
            RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> getTrips(String patent) {
        Fleet fleet = fleetRepository.findByPatent(patent);
        if (fleet != null) {
            RESPONSE.setBody(fleet.getTrip());
            RESPONSE.setStatus(HttpStatus.OK);
        } else {
            RESPONSE.setBody(null);
            RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> assignGPS(String mailAccount, String gpsId) {
        Account accountBackoffice = accountRepository.findFirstByMail(mailAccount);
        GPS gps = gpsRepository.findByIdAndClientId(new ObjectId(gpsId), accountBackoffice.getBusinessId());
        accountBackoffice.getGPSAssigned().add(gps);
        updateDocumentMongoDB.updateAccount(accountBackoffice);
        RESPONSE.setBody(null);
        RESPONSE.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    public ResponseEntity<Response> removeGPS(String mailAccount, String gpsId) {
        Account accountBackoffice = accountRepository.findFirstByMail(mailAccount);
        GPS gps = gpsRepository.findByIdAndClientId(new ObjectId(gpsId), accountBackoffice.getBusinessId());
        boolean remove = accountBackoffice.getGPSAssigned().removeIf(gps1 -> gps1.getId().equals(gps.getId()));
        if (remove) {
            RESPONSE.setBody(null);
            RESPONSE.setStatus(HttpStatus.OK);
            updateDocumentMongoDB.updateAccount(accountBackoffice);
        } else {
            RESPONSE.setBody("No se pudo eliminar el GPS Asignado");
            RESPONSE.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
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

    public static class ResponseGPSs {

        @JsonProperty("gpsAccount")
        @Getter
        @Setter
        private List<GPS> gpsAccount;

        @JsonProperty("gpsBusiness")
        @Getter
        @Setter
        private List<GPS> gpsBusiness;
    }
}
