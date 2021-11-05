package cl.gpsmain.datasource.controller.supervisor;

import cl.gpsmain.datasource.model.Fleet;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.FleetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("FleetController")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping(value = "/api/fleet")
public class FleetController {

    @Autowired
    private FleetService fleetService;

    @GetMapping("/get-all-fleets")
    public ResponseEntity<Response> getAllFleets(@RequestHeader("XclientSecret") UUID clientSecret,
                                                 @RequestHeader("Xmail") String mail) {
        return fleetService.getAllFleet(clientSecret, mail);
    }

    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT},
            path = {"create-fleet", "delete-fleet"})
    public ResponseEntity<Response> gPSService(@RequestHeader("XclientSecret") UUID clientSecret,
                                               @RequestHeader("Xmail") String mail,
                                               @RequestHeader("Xoption") String option,
                                               @RequestHeader(value = "XFleetPatent", required = false) String fleetPatent,
                                               @RequestBody(required = false) Fleet fleet) {
        return fleetService.fleetService(clientSecret, mail, option, fleetPatent, fleet);
    }
}
