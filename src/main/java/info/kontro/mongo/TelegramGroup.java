package info.kontro.mongo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;

@Entity(value = "group", noClassnameStored = true)
public class TelegramGroup extends ClassWithoutObjectId {
    @Id
    private Long chatId;
    @Reference
    private City city;

    private TelegramGroup() {
    }

    private TelegramGroup(Long chatId) {
        this.chatId = chatId;
    }

    static public TelegramGroup getGroup(Long chatId, Datastore connection) {
        TelegramGroup group = connection.get(TelegramGroup.class, chatId);
        if (group == null) {
            group = new TelegramGroup(chatId);
            group.save(connection);
        }
        return group;
    }

    public static ArrayList<TelegramGroup> getGroups(City city, Datastore connection) {
        ArrayList<TelegramGroup> groups = new ArrayList<>();
        for (TelegramGroup group : getAllGroups(connection)) {
            if (group.getCity() != null) {
                if (group.getCity().getName().equals(city.getName())) {
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    static public ArrayList<TelegramGroup> getAllGroups(Datastore connection) {
        return (ArrayList<TelegramGroup>) connection.createQuery(TelegramGroup.class).asList();
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Long getChatId() {
        return chatId;
    }
}
