package info.kontro.mongo;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.annotations.Id;

public abstract class ClassWithObjectId extends ClassWithoutObjectId {
    @Id
    private
    ObjectId id;

    @Override
    public void save(Datastore connection) {
        Key<ClassWithObjectId> key = connection.save(this);
        setId((ObjectId) key.getId());

        System.out.println("\nsave: " + this.toString() + "\n");
    }

    public ObjectId getId() {
        return id;
    }

    private void setId(ObjectId id) {
        this.id = id;
    }
}

