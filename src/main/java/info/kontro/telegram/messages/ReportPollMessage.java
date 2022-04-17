package info.kontro.telegram.messages;

import info.kontro.mongo.TicketControl;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class ReportPollMessage extends SendPoll implements BotMethode {

    public ReportPollMessage(String chatId, TicketControl ticketControl) {
        setChatId(chatId);
        setQuestion("Die Kontrolle wurde gemldet:/n/n" + ticketControl.getText());
        ArrayList<String> options = new ArrayList<>();
        options.add("Die Beschreibung ist diskrimminierend");
        options.add("Es handelt sich um Spam");
        options.add("Die Kontrolle ist okay");
        setOptions(options);
    }

    @Override
    public Message send(TelegramLongPollingBot telegram) {
        try {
            return telegram.execute(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
