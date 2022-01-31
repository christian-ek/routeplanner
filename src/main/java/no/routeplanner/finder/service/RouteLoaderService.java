package no.routeplanner.finder.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.routeplanner.finder.model.BusStop;
import no.routeplanner.finder.model.Route;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Slf4j
@Service
public class RouteLoaderService {

    @Value("${routes:}")
    private String path;

    public static List<BusStop> inMemoryBusStops;

    private final static List<FileContent> fileContent = new ArrayList<>();

    @PostConstruct
    private void readFile() {
        File file = new File(path);
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] values = data.split(",");
                fileContent.add(new FileContent(values[0], values[1], Integer.parseInt(values[2])));
            }
            log.info("Finished parsing routes file into memory.");
            reader.close();
        } catch (FileNotFoundException e) {
            log.error("Unable to read file ({})", path, e);

        }
    }

    public void loadBusStops() {
        inMemoryBusStops = new ArrayList<>();

        for (FileContent content : fileContent) {

            String startLocation = content.getStartLocation();
            String endLocation = content.getEndLocation();
            int distance = content.getDistance();

            /* Add startLocation to our bus stop list if not already added */
            BusStop startBusStop = getBusStopByName(startLocation);
            if (startBusStop == null){
                startBusStop = new BusStop(startLocation);
                inMemoryBusStops.add(startBusStop);
            }
            /* Add endLocation to our bus stop list if not already added */
            BusStop endBusStop = getBusStopByName(endLocation);
            if (endBusStop == null){
                endBusStop = new BusStop(endLocation);
                inMemoryBusStops.add(endBusStop);
            }

            startBusStop.addConnectedStop(Route.builder()
                    .source(startBusStop)
                    .destination(endBusStop)
                    .distance(distance)
                    .build());

            endBusStop.addConnectedStop(Route.builder()
                    .source(endBusStop)
                    .destination(startBusStop)
                    .distance(distance)
                    .build());
        }
    }

    public BusStop getBusStopByName(String name){
        return inMemoryBusStops.stream().filter(b -> b.getName().equals(name)).findFirst().orElse(null);
    }

    @Data
    @AllArgsConstructor
    private static class FileContent {
        String startLocation;
        String endLocation;
        int distance;
    }
}
