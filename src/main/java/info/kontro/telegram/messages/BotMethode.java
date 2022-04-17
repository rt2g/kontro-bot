package info.kontro.telegram.messages;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotMethode {
    Message send(TelegramLongPollingBot telegram);
}
