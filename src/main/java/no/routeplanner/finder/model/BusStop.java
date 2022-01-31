package no.routeplanner.finder.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@RequiredArgsConstructor
public class BusStop implements Comparable<BusStop>{
    final private String name;
    @ToString.Exclude
    private List<Route> connectedStops = new ArrayList<>();
    private BusStop predecessor;
    private boolean visited;
    private int distance = Integer.MAX_VALUE;

    public void addConnectedStop(Route route) {
        this.connectedStops.add(route);
    }

    @Override
    public int compareTo(BusStop b) {
        return Integer.compare(this.distance, b.getDistance());
    }
}
