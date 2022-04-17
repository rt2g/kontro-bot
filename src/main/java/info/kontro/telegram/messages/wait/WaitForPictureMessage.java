package info.kontro.telegram.messages.wait;

import info.kontro.telegram.KontroTelegramBot;
import info.kontro.telegram.messages.CustomSendMessage;

public class WaitForPictureMessage extends CustomSendMessage {
    public WaitForPictureMessage(long chatId) {
        setChatId(chatId);
        setBefehl(KontroTelegramBot.BEFEHL.PICTURE);
        setText("Möchtest du noch ein Bild hinzufügen? \nFalls ja kannst du mir es jetzt schicken");
        setButtons(new String[]{"ohne Bild fortfahren"});
        setCallback(new String[]{"no Photo"});
        setInlineButtonMarkup();
    }
}
