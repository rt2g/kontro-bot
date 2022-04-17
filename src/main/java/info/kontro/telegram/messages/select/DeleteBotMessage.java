package info.kontro.telegram.messages.select;

import info.kontro.telegram.messages.BotMethode;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DeleteBotMessage extends DeleteMessage implements BotMethode {

    public DeleteBotMessage(Message message) {
        this.setChatId(message.getChatId());
        this.setMessageId(message.getMessageId());
    }

    public DeleteBotMessage(String chatId, int messageId) {
        setChatId(chatId);
        setMessageId(messageId);
    }

    @Override
    public Message send(TelegramLongPollingBot telegram) {
        try {
            telegram.execute(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
