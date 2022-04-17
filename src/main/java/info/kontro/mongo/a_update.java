package info.kontro.mongo;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;

public class a_update {
    public static void main(String[] args) {
        Morphia morphia = new Morphia();
        MongoClient mongoClient = new MongoClient();
        morphia.mapPackage("dev.morphia.example");
        Datastore connection = morphia.createDatastore(mongoClient, "Berlin");


        ArrayList<Station> allStations = Station.getAllStations(connection);

        for (Station station : allStations) {
            System.out.println(station);
            station.update(connection);
        }
    }
}
