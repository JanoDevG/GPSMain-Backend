package cl.gpsmain.datasource.controller.supervisor;

import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.GPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("GPSController")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping(value = "/api/gps")
public class GPSController {

    @Autowired
    private GPSService gpsService;

    @GetMapping("/get-all-gps")
    public ResponseEntity<Response> getAllGPS(@RequestHeader("XclientSecret") UUID clientSecret,
                                              @RequestHeader("Xmail") String mail) {
        return gpsService.getAllGPS(clientSecret, mail);
    }

    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT},
            path = {"create-gps", "delete-gps"})
    public ResponseEntity<Response> gPSService(@RequestHeader("XclientSecret") UUID clientSecret,
                                               @RequestHeader("Xmail") String mail,
                                               @RequestHeader("Xoption") String option,
                                               @RequestHeader(value = "XgpsId", required = false) String gpsId) {
        return gpsService.gpsService(clientSecret, mail, option, gpsId);
    }

    @GetMapping(path = "get-count-gps-available")
    public ResponseEntity<Response> getCountGPSAvailable(@RequestHeader("Xmail") String mail) {
        return gpsService.getAllGPSAvailable(mail);
    }

}
