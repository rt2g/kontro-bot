package info.kontro.telegram.messages;

import info.kontro.mongo.BotUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotForwardMessage extends ForwardMessage implements BotMethode {
    public BotForwardMessage(BotUser user, Message message) {
        setChatId(user.getChatId());
        setMessageId(message.getMessageId());
        setFromChatId(message.getChatId());
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
