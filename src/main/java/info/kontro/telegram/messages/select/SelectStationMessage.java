package info.kontro.telegram.messages.select;

import info.kontro.mongo.Line;
import info.kontro.mongo.StationOrder;
import info.kontro.telegram.KontroTelegramBot;
import info.kontro.telegram.messages.CustomSendMessage;

import java.util.ArrayList;

public class SelectStationMessage extends CustomSendMessage {
    public SelectStationMessage(Long chatId, KontroTelegramBot.BEFEHL befehl, Line line) {
        setChatId(chatId);
        setBefehl(befehl, "station#");
        setText("Wähle eine Station aus");

        ArrayList<StationOrder> stationOrders = line.getStationOrders();
        String[] buttons = new String[stationOrders.size()];
        String[] callback = new String[stationOrders.size()];
        for (int i = 0; i < stationOrders.size(); i++) {
            buttons[i] = stationOrders.get(i).getStationName();
            callback[i] = stationOrders.get(i).getStationName();
        }
        setButtons(buttons);
        setCallback(callback);
        setInlineButtonMarkup(1);
    }
}
