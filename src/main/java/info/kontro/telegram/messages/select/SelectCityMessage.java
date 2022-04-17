package info.kontro.telegram.messages.select;

import info.kontro.mongo.City;
import info.kontro.telegram.KontroTelegramBot.BEFEHL;
import info.kontro.telegram.messages.CustomSendMessage;
import org.mongodb.morphia.Datastore;

import java.util.ArrayList;

public class SelectCityMessage extends CustomSendMessage {

    public SelectCityMessage(long chatId, Datastore connection) {
        setChatId(chatId);
        setBefehl(BEFEHL.CITY, "city#");

        setText("Wähle eine Stadt");

        ArrayList<City> cities = City.getAllCities(connection);
        String[] buttons = new String[cities.size()];
        String[] callback = new String[cities.size()];

        for (int i = 0; i < cities.size(); i++) {
            buttons[i] = cities.get(i).getName();
            callback[i] = cities.get(i).getName();
        }


        setButtons(buttons);
        setCallback(callback);
        setInlineButtonMarkup(1);
    }
}
