package no.routeplanner.finder.api;

import no.routeplanner.finder.service.ShortestRouteService;
import no.routeplanner.finder.model.BusStop;
import no.routeplanner.finder.model.ShortestRoute;
import no.routeplanner.finder.service.RouteLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestController
public class RouteApiController {

    @Autowired
    RouteLoaderService routeLoaderService;

    @Autowired
    ShortestRouteService shortestRouteService;

    @ResponseBody
    @RequestMapping(value = "/route", method=RequestMethod.GET, produces = "application/json")
    public ShortestRoute route(@RequestParam(value = "from") String from,
                               @RequestParam(value = "to") String to) {

        routeLoaderService.loadBusStops();

        BusStop source = routeLoaderService.getBusStopByName(from);
        BusStop dest = routeLoaderService.getBusStopByName(to);

        if (source == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("From bus stop not found (%s)", from)
            );
        } else if (dest == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("To bus stop not found (%s)", to)
            );
        }

        shortestRouteService.calculateShortestRoute(source);

        return ShortestRoute.builder()
                .route(shortestRouteService.getShortestRouteTo(dest).stream().map(BusStop::getName).collect(Collectors.toList()))
                .totalTime(dest.getDistance())
                .build();
    }


}
