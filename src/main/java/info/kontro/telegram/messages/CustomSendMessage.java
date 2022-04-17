package info.kontro.telegram.messages;

import info.kontro.telegram.KontroTelegramBot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CustomSendMessage extends SendMessage implements BotMethode {

    private String[] buttons;
    private String[] callback;
    private KontroTelegramBot.BEFEHL befehl;
    private String befehlSuffix;
    private HashMap<String, String> callbackMap = new HashMap<>();

    @Override
    public Message send(TelegramLongPollingBot telegram) {
        try {
            return telegram.execute(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void setInlineButtonMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRow = new ArrayList<>();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        String[] row1 = buttons;

        List<String[]> setup = new ArrayList<>();
        setup.add(row1);

        for (String[] buttonArray : setup) {
            for (String button : buttonArray) {
                InlineKeyboardButton b = new InlineKeyboardButton();
                b.setText(button);
                if (callbackMap != null) {
                    b.setCallbackData(callbackMap.get(button));
                }
                buttonList.add(b);
            }
            keyboardRow.add(buttonList);
            buttonList = new ArrayList<>();
        }

        inlineKeyboardMarkup.setKeyboard(keyboardRow);
        setReplyMarkup(inlineKeyboardMarkup);
    }

    protected void setInlineButtonMarkup(int maxProZeile) {

        String[][] inlineButtons2d;
        if (buttons.length > maxProZeile) {
            inlineButtons2d = new String[buttons.length / maxProZeile + 1][maxProZeile];

            for (int i = 0; i < buttons.length / maxProZeile + 1; i++) {
                for (int j = 0; j < maxProZeile; j++) {
                    if (i * maxProZeile + j >= buttons.length) {
                        break;
                    }
                    inlineButtons2d[i][j] = buttons[i * maxProZeile + j];
                }
            }

        } else {
            setInlineButtonMarkup();
            return;
        }


        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();


        List<InlineKeyboardButton> row;
        for (String[] strings : inlineButtons2d) {
            row = new ArrayList<>();
            for (String string : strings) {
                if (string == null) {
                    break;
                }
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(string);
                if (callbackMap != null) {
                    button.setCallbackData(callbackMap.get(string));
                }
                row.add(button);
            }
            rows.add(row);
        }

        markup.setKeyboard(rows);
        setReplyMarkup(markup);
    }

    protected void setButton(String buttton) {
        setButtons(new String[]{buttton});
    }

    protected void setCallback(String callback) {
        setCallback(new String[]{callback});
    }

    protected void setButtons(String[] buttons) {
        this.buttons = buttons;
        setCallbackMap();
    }

    protected void setCallback(String[] callback) {
        this.callback = callback;
        setCallbackMap();
    }

    protected void setBefehl(KontroTelegramBot.BEFEHL befehl) {
        this.befehl = befehl;
        setCallbackMap();
    }

    protected void setBefehl(KontroTelegramBot.BEFEHL befehl, String suffix) {
        this.befehl = befehl;
        this.befehlSuffix = suffix;
    }

    private void setCallbackMap() {
        if (buttons != null && callback != null) {
            if (callback.length == buttons.length) {
                if (befehl != null) {
                    for (int i = 0; i < buttons.length; i++) {
                        if (befehlSuffix == null) {
                            callbackMap.put(buttons[i], befehl + "##" + callback[i]);
                        } else {
                            callbackMap.put(buttons[i], befehl + "##" + befehlSuffix + callback[i]);
                        }
                    }
                } else {
                    for (int i = 0; i < buttons.length; i++) {
                        callbackMap.put(buttons[i], callback[i]);
                    }
                }
            }
        }
    }

    public enum BEFEHLSID {
        S1, G1, C1, C2, C3, C4, C5, C6, C7, C8
    }

    public enum MESSAGE {
        selectLine, select, vehicle
    }
}
