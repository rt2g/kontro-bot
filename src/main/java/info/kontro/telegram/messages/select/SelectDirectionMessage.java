package info.kontro.telegram.messages.select;

import info.kontro.mongo.Line;
import info.kontro.telegram.KontroTelegramBot;
import info.kontro.telegram.messages.CustomSendMessage;

import java.util.ArrayList;

public class SelectDirectionMessage extends CustomSendMessage {
    public SelectDirectionMessage(long chatId, Line line, KontroTelegramBot.BEFEHL befehl) {
        setChatId(chatId);
        setBefehl(befehl, "direction#");
        setText("Wähle die richtung?");

        ArrayList<String> endStations = line.getEndStationsNames();

        String[] buttons = new String[2];
        buttons[0] = endStations.get(0);
        buttons[1] = endStations.get(1);

        String[] callback = new String[2];
        callback[0] = endStations.get(0);
        callback[1] = endStations.get(1);

        setButtons(buttons);
        setCallback(callback);
        setInlineButtonMarkup(1);
    }
}
