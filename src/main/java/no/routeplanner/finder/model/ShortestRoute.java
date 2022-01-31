package no.routeplanner.finder.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShortestRoute {
    List<String> route;
    Integer totalTime;
}
