package info.kontro.mongo;

import org.mongodb.morphia.Datastore;

public abstract class ClassWithoutObjectId {
    public void save(Datastore connection) {
        connection.save(this);
    }

    public void delete(Datastore connection) {
        connection.delete(this);
    }

    public void merge(Datastore connection) {
        connection.merge(this);
    }
}
