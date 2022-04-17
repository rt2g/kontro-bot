package info.kontro.mongo;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class TelegramMessage {
    @Property(value = "chat")
    private Long chatId;
    @Property(value = "message")
    private Integer messageId;

    public TelegramMessage() {
    }

    public TelegramMessage(Long chatId, Integer messageId) {
        this.chatId = chatId;
        this.messageId = messageId;
    }
}
