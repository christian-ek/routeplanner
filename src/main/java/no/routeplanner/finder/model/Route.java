package no.routeplanner.finder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class Route {
    private BusStop source;
    private BusStop destination;
    private int distance;
}
