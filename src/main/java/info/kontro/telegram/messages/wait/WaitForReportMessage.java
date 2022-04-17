package info.kontro.telegram.messages.wait;

import info.kontro.telegram.KontroTelegramBot;
import info.kontro.telegram.messages.CustomSendMessage;

public class WaitForReportMessage extends CustomSendMessage {
    public WaitForReportMessage(long chatId) {
        setBefehl(KontroTelegramBot.BEFEHL.REPORT);
        setChatId(chatId);
        setText("Leite die Kontrolle die du Melden möchtest aus dem (@KontroKanal) Kanal an mich weiter.");
        setButton("abrechen");
        setCallback("cancel");
        setInlineButtonMarkup();
    }
}
