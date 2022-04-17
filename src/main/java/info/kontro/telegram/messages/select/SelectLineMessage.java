package info.kontro.telegram.messages.select;

import info.kontro.mongo.Line;
import info.kontro.mongo.Vehicle;
import info.kontro.telegram.KontroTelegramBot;
import info.kontro.telegram.messages.CustomSendMessage;

import java.util.ArrayList;

public class SelectLineMessage extends CustomSendMessage {
    public SelectLineMessage(Long chatId, KontroTelegramBot.BEFEHL befehl, Vehicle vehicle) {
        setChatId(chatId);
        setBefehl(befehl, "line#");
        setText("Wähle eine Linie aus");

        ArrayList<Line> lines = vehicle.getLines();
        String[] buttons = new String[lines.size()];
        String[] callback = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            buttons[i] = lines.get(i).getName();
            callback[i] = lines.get(i).getName();
        }
        setButtons(buttons);
        setCallback(callback);
        setInlineButtonMarkup(4);
    }
}
