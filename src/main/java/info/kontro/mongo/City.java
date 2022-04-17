package info.kontro.mongo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;

@Entity(value = "city", noClassnameStored = true)
public class City extends ClassWithoutObjectId {
    @Id
    private String name;

    public City() {
    }

    public static City getCity(String id, Datastore connection) {
        return connection.get(City.class, id);
    }

    public static ArrayList<City> getAllCities(Datastore connection) {
        return (ArrayList<City>) connection.createQuery(City.class).asList();
    }

    public String getName() {
        return name;
    }
}
