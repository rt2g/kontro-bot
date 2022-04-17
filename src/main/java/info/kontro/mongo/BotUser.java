package info.kontro.mongo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDateTime;

@Entity(value = "user", noClassnameStored = true)
public class BotUser extends ClassWithoutObjectId {
    @Id
    Integer userId;
    Long chatId;
    City city;
    Boolean awareness;
    LocalDateTime blockTextUntil;
    LocalDateTime blockPhotoUntil;
    LocalDateTime lastOnline;

    public BotUser() {
    }

    public BotUser(Integer userId, Datastore connection) {
        this.userId = userId;
        setLastOnline(connection);
    }

    public static BotUser get(Integer userId, Datastore connection) {
        BotUser botUser = connection.get(BotUser.class, userId);
        if (botUser == null) {
            botUser = new BotUser(userId, connection);
        } else {
            botUser.setLastOnline(connection);
        }
        return botUser;
    }

    private void setLastOnline(Datastore connection) {
        this.lastOnline = LocalDateTime.now();
        connection.merge(this);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city, Datastore connection) {
        this.city = city;
        save(connection);
    }

    public void setCity(String id, Datastore connection) {
        setCity(City.getCity(id, connection), connection);
    }

    public Integer getId() {
        return userId;
    }

    public void setAwareness(Boolean awareness) {
        if (awareness) {
            this.awareness = true;
        } else {
            this.awareness = null;
        }

    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
