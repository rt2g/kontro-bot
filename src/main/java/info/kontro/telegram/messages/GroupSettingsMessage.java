package info.kontro.telegram.messages;

import info.kontro.mongo.City;
import info.kontro.mongo.TelegramGroup;
import info.kontro.telegram.KontroTelegramBot;
import org.mongodb.morphia.Datastore;

import java.util.ArrayList;

public class GroupSettingsMessage extends CustomSendMessage {
    public GroupSettingsMessage(TelegramGroup group, Datastore connection, KontroTelegramBot.BEFEHL befehl) {

        setChatId(group.getChatId());
        setText("Für welche Stadt ist die Gruppe?");

        ArrayList<City> cities = City.getAllCities(connection);

        String[] buttons = new String[cities.size() + 2];
        String[] callback = new String[cities.size() + 2];

        for (int i = 0; i < cities.size(); i++) {
            buttons[i] = cities.get(i).getName();
            callback[i] = "city#" + cities.get(i).getName();
        }


        buttons[cities.size()] = "Fertig";
        callback[cities.size()] = "close";

        buttons[cities.size() + 1] = "Verlassen";
        callback[cities.size() + 1] = "leave";

        setButtons(buttons);
        setCallback(callback);
        setBefehl(befehl);
        setInlineButtonMarkup(1);
    }
}
