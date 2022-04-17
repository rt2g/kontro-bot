package info.kontro.mongo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import java.util.ArrayList;

@Entity(value = "line", noClassnameStored = true)
public class Line extends ClassWithoutObjectId {
    @Id
    private String name;
    @Reference(lazy = true)
    private Vehicle vehicle;
    private ArrayList<StationOrder> stationOrders;
    @Transient
    private ArrayList<TicketControl> controls;

    public Line() {
    }

    public Line(String name) {
        this.name = name;
    }

    public static Line getLine(String id, Datastore cityConnection) {
        return cityConnection.get(Line.class, id);
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getName() {
        return name;
    }

    public void addStationOrder(StationOrder stationOrder) {
        if (stationOrders == null) {
            ArrayList<StationOrder> stations = new ArrayList<>();
            stations.add(stationOrder);
            this.stationOrders = stations;
        } else {
            stationOrders.add(stationOrder);
        }
    }

    public ArrayList<StationOrder> getStationOrders() {
        return stationOrders;
    }

    public ArrayList<String> getEndStationsNames() {
        ArrayList<String> endStations = new ArrayList<>();
        StationOrder begin = null;
        StationOrder end = null;
        for (StationOrder stationOrder : getStationOrders()) {
            if (begin == null) {
                begin = stationOrder;
                end = stationOrder;
            } else {
                if (begin.getOrder() > stationOrder.getOrder()) {
                    begin = stationOrder;
                }
                if (end.getOrder() < stationOrder.getOrder()) {
                    end = stationOrder;
                }
            }
        }
        endStations.add(begin.getStationName());
        endStations.add(end.getStationName());
        return endStations;
    }
}
