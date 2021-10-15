package cl.gpsmain.datasource.controller.supervisor;


import cl.gpsmain.datasource.model.GPS;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.GPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("GPSController")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/api/gps")
public class GPSController {

    @Autowired
    private GPSService gpsService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST},
            path = {"/create-gps", "/get-gps", "delete-delete"})
    public ResponseEntity<Response> gpsController(@RequestHeader("Xoption") String option,
                                                  @RequestHeader("XclientSecret") UUID clientSecret,
                                                  @RequestHeader("Xmail") String mail,
                                                  @RequestBody(required = false) GPS gps) {
        return gpsService.gpsService(clientSecret, mail, option, gps);

    }
}
