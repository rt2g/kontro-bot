package info.kontro.mongo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;

@Entity(value = "station", noClassnameStored = true)
public class Station extends ClassWithoutObjectId {
    @Id
    private String name;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<String> vehiclesName;
    @Embedded
    private ArrayList<LineOrder> lineOrders;

    public Station() {
    }

    public static ArrayList<Station> getAllStations(Datastore connection) {
        return (ArrayList<Station>) connection.createQuery(Station.class).asList();
    }

    public static Station getStation(String id, Datastore cityConnection) {
        return cityConnection.get(Station.class, id);
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getVehiclesNames() {
        return null;
        //return vehicles;
    }

    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<>();
        for (LineOrder lineOrder : this.lineOrders) {
            lines.add(lineOrder.getLine());
        }
        return lines;
    }

    public void update(Datastore connection) {
        ArrayList<String> names = new ArrayList<>();
        for (String vehicleName : vehiclesName) {
            if (names.contains(vehicleName) == false) {
                names.add(vehicleName);
            }
        }
        System.out.println(vehiclesName);
        System.out.println(names);
        System.out.println("\n");
        this.vehiclesName = names;
        save(connection);
    }
}
