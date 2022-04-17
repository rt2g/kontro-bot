package info.kontro.mongo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.io.IOException;
import java.util.Scanner;

@Entity(value = "settings", noClassnameStored = true)
public class Settings extends ClassWithoutObjectId {
    static int TCK_LENGHT = 25;
    static int TCS_LENGHT = 50;
    static int TAT_LENGHT = 50;
    static int TATS_LENGHT = 45;
    @Id
    private String _id = "settings";
    private Boolean KONTRO_TWITTER;
    private String TELEGRAM_API_TOKEN;
    //Twitter Consumer Key
    @Property(value = "TWITTER_CONSUMER_KEY")
    private String TCK;
    @Property(value = "TWITTER_CONSUMER_SECRET")
    private String TCS;
    @Property(value = "TWITTER_ACCESS_TOKEN")
    private String TAT;
    @Property(value = "TWITTER_ACCESS_TOKEN_SECRET")
    private String TATS;

    public boolean isTwitter() {
        if (KONTRO_TWITTER == null) {
            KONTRO_TWITTER = false;
        }
        return KONTRO_TWITTER;
    }

    public String getTelegramToken(Datastore connection) {
        if (TELEGRAM_API_TOKEN == null) {
            TELEGRAM_API_TOKEN = changeToken("Telegram Api Token", null, 45);
            connection.save(this);
        }
        return TELEGRAM_API_TOKEN;
    }

    public String getTwitterAccessToken(Datastore connection) {
        if (TAT == null) {
            TAT = changeToken("Twitter Access Token", null, 50);
            connection.save(this);
        }
        return TAT;
    }

    public String getTwitterAccessTokenSecret(Datastore connection) {
        if (TATS == null) {
            TATS = changeToken("Twitter Access Token Secret", null, 45);
            connection.save(this);
        }
        return TATS;
    }

    public String getTwitterConsumerKey(Datastore connection) {
        if (TCK == null) {
            TCK = changeToken("Twitter Consumer Key", null, 25);
            connection.save(this);
        }
        return TCK;
    }

    public String getTwitterConsumerSecret(Datastore connection) {
        if (TCS == null) {
            TCS = changeToken("Twitter Consumer Secret", null, 50);
            connection.save(this);
        }
        return TCS;
    }

    public void setTelegramToken(Datastore connection) {
        changeToken("Telegram Api Token", TELEGRAM_API_TOKEN, 5);
    }

    private String changeToken(String tokenName, String old, int lenght) {
        if (old == null) {
            System.out.println("Es wurde noch kein " + tokenName + " angegeben!");
        } else {
            System.out.println("Den alten " + tokenName + ": " + old + " ersetzen!");
        }
        System.out.println("\nBitte trage jetzt den " + tokenName.toUpperCase() + " ein:");
        Scanner userInput = new Scanner(System.in);
        String newtoken = userInput.nextLine();
        while (newtoken.length() != lenght) {
            try {
                System.in.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Den " + tokenName + " den du eingegeben hast hat nicht die richtige länge..." +
                    "\nVersuch es nochmal der token müsste " + lenght + " stellen lang sein.");
            if (newtoken.equals("exit") || newtoken.equals("leave") || newtoken.equals("break")) {
                System.exit(1);
            }
            newtoken = userInput.nextLine();
        }
        return newtoken;

    }

    public void changeTwitterKeys() {
        TCS = changeToken("Twitter Consumer Secret", TCS, TCS_LENGHT);
        TCK = changeToken("Twitter Consumer Key", TCK, TCK_LENGHT);
        TATS = changeToken("Twitter Access Token Secret", TATS, TATS_LENGHT);
        TAT = changeToken("Twitter Access Token", TAT, TATS_LENGHT);
    }
}
