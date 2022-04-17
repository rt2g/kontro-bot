package info.kontro.mongo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;

@Entity(value = "vehicle", noClassnameStored = true)
public class Vehicle extends ClassWithoutObjectId {
    @Id
    String name;
    @Reference(lazy = false)
    ArrayList<Line> lines;

    public Vehicle() {
    }

    public Vehicle(String name, ArrayList<Line> lines) {
        this.name = name;
        this.lines = lines;
    }

    public Vehicle(String name) {
        this.name = name;
    }

    public static Vehicle getVehicle(String id, Datastore connection) {
        return connection.get(Vehicle.class, id);
    }

    public static ArrayList<Vehicle> getAllVehicles(Datastore connection) {
        return (ArrayList<Vehicle>) connection.createQuery(Vehicle.class).asList();
    }

    public String getName() {
        return name;
    }

    public void addLine(Line line) {
        if (lines == null) {
            ArrayList<Line> lines = new ArrayList<>();
            lines.add(line);
            this.lines = lines;
        } else {
            lines.add(line);
        }
    }

    public ArrayList<Line> getLines() {
        if (lines == null) {
            return new ArrayList<>();
        }
        return lines;
    }
}
