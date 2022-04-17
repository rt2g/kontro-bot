package info.kontro;

import com.mongodb.MongoClient;
import info.kontro.mongo.City;
import info.kontro.mongo.Settings;
import info.kontro.mongo.TicketControl;
import info.kontro.telegram.KontroTelegramBot;
import info.kontro.twitter.KontroTwitterBot;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public Settings settings;
    private KontroTelegramBot telegramBot;
    private ArrayList<KontroBotPlatform> kontroBotPlatformList = new ArrayList<>();
    private Morphia morphia;
    private MongoClient mongoClient;
    private Datastore connection;
    private HashMap<String, Datastore> connectionHashMap;


    public Main() {

        morphia = new Morphia();
        mongoClient = new MongoClient();
        morphia.mapPackage("dev.morphia.example");
        connection = morphia.createDatastore(mongoClient, "kontroBot");
        connectionHashMap = new HashMap<>();
        getSettings();

        kontroBotPlatformList.add(createTwitterBot());
        kontroBotPlatformList.add(createTelegramBot());


        for (City city : connection.createQuery(City.class).asList()) {
            connectionHashMap.put(city.getName(), morphia.createDatastore(mongoClient, city.getName()));
        }
    }

    public static void main(String[] args) {
        new Main();
    }

    private void getSettings() {
        try {
            settings = connection.get(Settings.class, "settings");
        } catch (Exception e) {
            System.out.println("problems with reaching the settings");
        }
        if (settings == null) {
            this.settings = new Settings();
        }
    }

    private KontroTwitterBot createTwitterBot() {
        final String TWITTER_CONSUMER_KEY = settings.getTwitterConsumerKey(connection);
        final String TWITTER_CONSUMER_SECRET = settings.getTwitterConsumerSecret(connection);
        final String TWITTER_ACCESS_TOKEN = settings.getTwitterAccessToken(connection);
        final String TWITTER_ACSESS_TOKEN_SECRET = settings.getTwitterAccessTokenSecret(connection);
        return new KontroTwitterBot(this, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, TWITTER_ACCESS_TOKEN, TWITTER_ACSESS_TOKEN_SECRET);
    }

    private KontroTelegramBot createTelegramBot() {
        final String TELEGRAM_API_TOKEN = settings.getTelegramToken(connection);
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        KontroTelegramBot telegramBot = new KontroTelegramBot(this, TELEGRAM_API_TOKEN);
        try {
            telegramBotsApi.registerBot(telegramBot);
            System.out.println("Connected to Telegram API");

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        return telegramBot;
    }

    public void newTicketControl(TicketControl ticketControl) {
        ticketControl.save(getConnection(ticketControl));
        kontroBotPlatformList.forEach(botMedium -> botMedium.newTicketControl(ticketControl));
        ticketControl.save(getConnection(ticketControl));
    }

    public void deleteticketControl(TicketControl ticketControl) {
        ticketControl.delete(getConnection(ticketControl));
        kontroBotPlatformList.forEach(botMedium -> botMedium.deleteTicketControl(ticketControl));
    }

    public Datastore getConnection() {
        return connection;
    }

    public Datastore getConnection(City city) {
        return connectionHashMap.get(city.getName());
    }

    public Datastore getConnection(TicketControl ticketControl) {
        return getConnection(ticketControl.getCity());
    }
}