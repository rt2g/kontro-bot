package info.kontro.mongo;

import info.kontro.telegram.messages.BotMethode;
import info.kontro.telegram.messages.ComfimMessag;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(value = "ticket_control", noClassnameStored = true)
public class TicketControl extends ClassWithObjectId {

    @Reference(lazy = true)
    private Station station;
    @Property(value = "desc")
    private String description;
    @Property(value = "photo")
    private String photoId;
    @Embedded
    private BotDate date;
    @Property(value = "messages")
    private List<TelegramMessage> messages;
    @Reference(lazy = true)
    private Line line;
    @Reference(lazy = true)
    private BotUser creator;
    private ArrayList<Integer> reports;
    @Reference(lazy = true)
    private Station direction;
    @Reference(lazy = true)
    private Vehicle vehicle;
    private City city;
    @Property(value = "messageId")
    private Integer telegramMessageId;
    private Long tweetId;
    private String pollId;


    public TicketControl() {
    }

    public TicketControl(LocalDateTime creationDateTime) {
        this.date = new BotDate(creationDateTime);
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getHourMinute() {
        return date.getHourMinute();
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        text.append("Stadt: ").append(getCity().getName());
        text.append("\nVehrkehrsmittel: ").append(getVehicle().getName());
        text.append("\nLinie: ").append(line.getName());
        text.append("\nStation: ").append(station.getName());
        text.append("\nRichtung: ").append(direction.getName());
        text.append("\nUhrzeit: ").append(getHourMinute());
        text.append("\nBeschreibung: ").append(description);
        return text.toString();
    }

    public void setDirection(Station direction) {
        this.direction = direction;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public BotMethode getComformationMessage(long chatId) {
        return new ComfimMessag(chatId, this);
    }

    public void setCreator(BotUser creator) {
        this.creator = creator;
    }

    public ArrayList<Line> getLines() {
        return station.getLines();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return null;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setMessages(ArrayList<TelegramMessage> messages) {
        this.messages = messages;
    }

    public String getHashtag() {
        StringBuilder hashtag = new StringBuilder();
        hashtag.append("#")
                .append(city.getName())
                .append('_')
                .append(vehicle.getName())
                .append('_')
                .append(line.getName())
                .append("\n#")
                .append(station.getName());
        return hashtag.toString().replace("-", "").replace(' ', '_');
    }

    public void setMessageId(Integer telegramChannelUrl) {
        this.telegramMessageId = telegramChannelUrl;
    }

    public String getTelegramChannelUrl() {
        return "https://t.me/KontroKanal/" + telegramMessageId;
    }

    public String getTweetUrl() {
        return "https://twitter.com/BotKontro/status/" + tweetId;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public int addReport(Integer userId, Datastore connection) {
        if (reports == null) {
            reports = new ArrayList<>();
        } else if (reports.contains(userId)) {
            return reports.size();
        }
        reports.add(userId);
        save(connection);
        return reports.size();
    }

    public void setPollId(String id) {
        this.pollId = id;
    }

    public int getTelegramMessageId() {
        return telegramMessageId;
    }
}

