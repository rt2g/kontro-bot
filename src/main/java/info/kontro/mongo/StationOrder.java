package info.kontro.mongo;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class StationOrder {
    @Property(value = "station")
    private String stationName;
    private Integer order;

    StationOrder() {
    }

    StationOrder(Integer order, Station station) {
        this.order = order;
        this.stationName = station.getName();
    }

    public Integer getOrder() {
        return order;
    }

    public String getStationName() {
        return stationName;
    }
}
