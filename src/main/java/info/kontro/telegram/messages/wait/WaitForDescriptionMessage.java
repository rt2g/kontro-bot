package info.kontro.telegram.messages.wait;

import info.kontro.telegram.messages.CustomSendMessage;

public class WaitForDescriptionMessage extends CustomSendMessage {
    public WaitForDescriptionMessage(long chatId) {
        setChatId(chatId);
        setText("Sende eine Beschreibung der Kontrolleure");
    }
}
