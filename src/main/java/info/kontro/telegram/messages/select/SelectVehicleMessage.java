package info.kontro.telegram.messages.select;

import info.kontro.mongo.Vehicle;
import info.kontro.telegram.KontroTelegramBot;
import info.kontro.telegram.messages.CustomSendMessage;
import org.mongodb.morphia.Datastore;

import java.util.ArrayList;

public class SelectVehicleMessage extends CustomSendMessage {
    public SelectVehicleMessage(Long chatId, KontroTelegramBot.BEFEHL befehl, Datastore cityConnection) {
        ArrayList<Vehicle> vehicles = Vehicle.getAllVehicles(cityConnection);
        setChatId(chatId);
        setBefehl(befehl, "vehicle#");
        setText("Wähle eine Vehrkehrsmittel aus");

        String[] buttons = new String[vehicles.size()];
        String[] callback = new String[vehicles.size()];
        for (int i = 0; i < vehicles.size(); i++) {
            buttons[i] = vehicles.get(i).getName();
            callback[i] = vehicles.get(i).getName();
        }
        setButtons(buttons);
        setCallback(callback);
        setInlineButtonMarkup(2);
    }
}
