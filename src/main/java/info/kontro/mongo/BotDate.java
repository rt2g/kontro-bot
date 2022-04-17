package info.kontro.mongo;

import org.mongodb.morphia.annotations.Entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(value = "date", noClassnameStored = true)
public class BotDate extends ClassWithoutObjectId {

    private LocalDateTime dateTime;
    private int year;
    private int month;
    private int day;
    private int dayOfWeek;
    private int hour;
    private int minute;

    public BotDate() {
    }

    public BotDate(LocalDateTime localDateTime) {
        this.dateTime = localDateTime;
        this.year = dateTime.getYear();
        this.month = dateTime.getMonthValue();
        this.day = dateTime.getDayOfMonth();
        this.dayOfWeek = dateTime.getDayOfWeek().getValue();
        this.hour = dateTime.getHour();
        this.minute = dateTime.getMinute();
    }

    public static DateTimeFormatter hourMinute() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTimeFormatter;
    }

    public String getHourMinute() {
        return hourMinute().format(dateTime);
    }
}
