package info.kontro.telegram.messages;

import info.kontro.mongo.TicketControl;
import info.kontro.telegram.KontroTelegramBot;

public class ComfimMessag extends CustomSendMessage {
    public ComfimMessag(long chatId, TicketControl ticketControl) {
        setChatId(chatId);
        String text = ticketControl.getText();
        text = text + "\n\nSind alle Angaben richtig";
        setText(text);
        setButtons(new String[]{"Ja", "Nein"});
        setCallback(new String[]{"save", "cancel"});
        setBefehl(KontroTelegramBot.BEFEHL.COMFORMATION);
        setInlineButtonMarkup();
    }
}
