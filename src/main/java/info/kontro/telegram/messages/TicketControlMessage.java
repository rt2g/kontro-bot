package info.kontro.telegram.messages;

import info.kontro.mongo.TicketControl;

public class TicketControlMessage extends CustomSendMessage {
    public TicketControlMessage(long chatId, TicketControl ticketControl) {
        setChatId(chatId);
        setText(ticketControl.getHashtag() + ticketControl.getText());
    }

    public TicketControlMessage(String chatId, TicketControl ticketControl) {
        setChatId(chatId);
        setText(ticketControl.getText());
    }
}
