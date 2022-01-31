package no.routeplanner.finder.service;

import no.routeplanner.finder.model.BusStop;
import no.routeplanner.finder.model.Route;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class ShortestRouteService {

    public void calculateShortestRoute(BusStop fromLocation){

        fromLocation.setDistance(0);
        PriorityQueue<BusStop> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(fromLocation);
        fromLocation.setVisited(true);

        while(!priorityQueue.isEmpty()){
            BusStop currentStop = priorityQueue.poll();

            for(Route route : currentStop.getConnectedStops()){
                BusStop stop = route.getDestination();
                if(!stop.isVisited())
                {
                    int newDistance = currentStop.getDistance() + route.getDistance();

                    if(newDistance < stop.getDistance()){
                        priorityQueue.remove(stop);
                        stop.setDistance(newDistance);
                        stop.setPredecessor(currentStop);
                        priorityQueue.add(stop);
                    }
                }
            }
            currentStop.setVisited(true);
        }
    }

    public List<BusStop> getShortestRouteTo(BusStop targetLocation){
        List<BusStop> path = new ArrayList<>();

        for(BusStop busStop=targetLocation;busStop!=null;busStop=busStop.getPredecessor()){
            path.add(busStop);
        }

        Collections.reverse(path);
        return path;
    }

}
