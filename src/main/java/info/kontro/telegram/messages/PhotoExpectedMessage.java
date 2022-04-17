package info.kontro.telegram.messages;

public class PhotoExpectedMessage extends CustomSendMessage {
    public PhotoExpectedMessage(long chatId) {
        setChatId(chatId);
        setText("Das war kein Foto");
    }
}
