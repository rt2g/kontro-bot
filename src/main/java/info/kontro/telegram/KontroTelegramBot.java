package info.kontro.telegram;

import com.mongodb.MongoClient;
import info.kontro.KontroBotPlatform;
import info.kontro.Main;
import info.kontro.mongo.*;
import info.kontro.telegram.messages.*;
import info.kontro.telegram.messages.select.*;
import info.kontro.telegram.messages.wait.WaitForDescriptionMessage;
import info.kontro.telegram.messages.wait.WaitForPictureMessage;
import info.kontro.telegram.messages.wait.WaitForReportMessage;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KontroTelegramBot extends TelegramLongPollingBot implements KontroBotPlatform {

    public static final int reportsUntilRep = 1;
    private static long botId = 798020455;
    private final String API_TOKEN;
    private final Main main;
    private HashMap<Long, TicketControl> ticketControlHashMap = new HashMap<>();
    private HashMap<Long, Message> waitForDescriptionHashMap = new HashMap<>();
    private HashMap<Long, Message> waitForPictureHashMap = new HashMap<>();
    private HashMap<Long, Message> waitForDirectionHashMap = new HashMap<>();
    private HashMap<Long, Message> waitForComformationHashMap = new HashMap<>();
    private HashMap<Long, Message> photoExpectedHashMap = new HashMap<>();
    private HashMap<Long, Message> descriptionExpectedHashMap = new HashMap<>();
    private HashMap<Long, Update> noCityHashMap = new HashMap<>();
    private HashMap<Long, Message> waitForReportHashMap = new HashMap<>();
    private String awarnessId = "@KontroBotAwareness";
    private String channelId = "@KontroKanal";
    private ArrayList<City> cities;
    private ArrayList<TelegramGroup> groups;
    private ArrayList<Line> linesWithControls;


    public KontroTelegramBot(Main main, String API_TOKEN) {
        this.main = main;
        this.API_TOKEN = API_TOKEN;
        final Morphia morphia = new Morphia();
        final MongoClient mongoClient = new MongoClient();
        morphia.mapPackage("dev.morphia.example");
        linesWithControls = new ArrayList<>();
    }

    @Override
    public void newTicketControl(TicketControl ticketControl) {
        ArrayList<TelegramMessage> messages = new ArrayList<>();
        for (TelegramGroup group : TelegramGroup.getGroups(ticketControl.getCity(), main.getConnection())) {
            Message message = send(new TicketControlMessage(group.getChatId(), ticketControl));
            messages.add(new TelegramMessage(message.getChatId(), message.getMessageId()));
        }
        ticketControl.setMessages(messages);
    }

    @Override
    public void deleteTicketControl(TicketControl ticketControl) {
        DeleteBotMessage deleteBotMessage = new DeleteBotMessage(channelId, ticketControl.getTelegramMessageId());
    }

    @Override
    public void deleteDescription(TicketControl ticketControl) {

    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        for (Update update : updates) {
            onUpdateReceived(update);
        }
    }

    @Override
    public String getBotUsername() {
        try {
            return getMe().getUserName();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBotToken() {
        return API_TOKEN;
    }

    @Override
    public void onClosing() {
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId;
        if (update.hasMessage()) {
            Message recivedMessage = update.getMessage();
            Chat chat = recivedMessage.getChat();
            chatId = recivedMessage.getChatId();
            String recivedText = recivedMessage.getText();
            User from = recivedMessage.getFrom();
            BotUser botUser = BotUser.get(from.getId(), main.getConnection());
            if (botUser.getCity() == null) {
                send(new SelectCityMessage(chatId, main.getConnection()));
                noCityHashMap.put(chatId, update);
                return;
            }
            if (waitForDescriptionHashMap.get(chatId) != null) {
                waitForDescription(chatId, recivedMessage, recivedText);
                return;
            } else if (waitForPictureHashMap.get(chatId) != null && !update.hasCallbackQuery()) {
                waitForPicture(chatId, recivedMessage);
                return;
            } else if (waitForReportHashMap.get(chatId) != null) {
                deleteMessage(waitForReportHashMap.get(chatId));
                deleteMessage(recivedMessage);
                waitForReportHashMap.remove(chatId);
                if (recivedMessage.getForwardFromChat().getUserName().equals(channelId.substring(1))) {
                    Query<TicketControl> controllQuery = main.getConnection(botUser.getCity()).createQuery(TicketControl.class);
                    controllQuery.criteria("messageId").equals(recivedMessage.getForwardFromMessageId());
                    TicketControl ticketControl = controllQuery.get();
                    int reportsConut = ticketControl.addReport(botUser.getId(), main.getConnection());

                    if (reportsConut > reportsUntilRep) {
                        Message message = send(new ReportPollMessage(awarnessId, ticketControl));
                        ticketControl.setPollId(message.getPoll().getId());
                        Query<BotUser> userQuery = main.getConnection().createQuery(BotUser.class);
                        controllQuery.criteria("awareness").equals(true);
                        for (BotUser user : userQuery.asList()) {
                            send(new BotForwardMessage(user, message));
                        }
                    }
                }
                return;
            } else if (chat.isGroupChat() || chat.isSuperGroupChat() || chat.isChannelChat()) {
                TelegramGroup group = TelegramGroup.getGroup(chatId, main.getConnection());
                if (!groups.contains(group))
                    groups.add(group);
                //Bot wird einer neuen Gruppe/Channel hinzugefügt
                if (recivedMessage.getNewChatMembers().size() > 0) {
                    for (int i = 0; i < recivedMessage.getNewChatMembers().size(); i++) {
                        int newUserId = recivedMessage.getNewChatMembers().get(i).getId();
                        if (newUserId == botId) {
                            send(new GroupSettingsMessage(group, main.getConnection(), BEFEHL.GROUPSETTINGS));
                        }
                    }
                }
                //Bot wird aus einer Gruppe entfernt
                else if (recivedMessage.getLeftChatMember() != null) {
                    if (recivedMessage.getLeftChatMember().getId() == botId) {
                        group.delete(main.getConnection());
                    }
                } else if (recivedMessage.hasText()) {
                    if (recivedText.contains("@" + getBotUsername())) {
                        recivedText = recivedText.replace("@" + getBotUsername(), "");
                        recivedText = recivedText.toLowerCase();
                        recivedText = recivedText.trim();
                        if (recivedText.equals("/einstellungen") && isChatAdmin(chat, from)) {
                            deleteMessage(recivedMessage);
                            send(new GroupSettingsMessage(group, main.getConnection(), BEFEHL.GROUPSETTINGS));
                        }
                        deleteMessage(recivedMessage);
                    }
                }
            } else if (chat.isUserChat()) {
                deleteMessage(recivedMessage);
                recivedText = recivedText.toLowerCase();
                switch (recivedText) {
                    case "/start":
                        send(new StartMessage(chatId));
                        botUser.setChatId(recivedMessage.getChatId());
                        break;
                    case "/kontrolle":
                        send(new SelectVehicleMessage(chatId, BEFEHL.KONTROLLE, main.getConnection(botUser.getCity())));
                        break;
                    case "/linie":
                        //send(new SelectVehicleMessage(chatId,BEFEHL.LINE,vehicles));
                        break;
                    case "/faq":
                        break;
                    case "/awareness":
                        botUser.setAwareness(true);
                        break;
                    case "/report":
                    case "/melden":
                        Message message = send(new WaitForReportMessage(chatId));
                        waitForReportHashMap.put(chatId, message);
                    default:
                        break;
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery recivedCallback = update.getCallbackQuery();
            if (recivedCallback.getData() != null) {
                User from = recivedCallback.getFrom();
                BotUser botUser = BotUser.get(from.getId(), main.getConnection());
                Message recivedMessage = recivedCallback.getMessage();
                chatId = recivedCallback.getMessage().getChatId();
                BEFEHL befehl = BEFEHL.valueOf(recivedCallback.getData().split("##")[0]);
                String[] recivedData = recivedCallback.getData().split("##")[1].split("#");
                deleteMessage(recivedMessage);
                switch (befehl) {
                    case LINE:
                    case KONTROLLE:
                        select(chatId, botUser, recivedData, befehl);
                        break;
                    case PICTURE:
                        waitForPicture(chatId);
                        break;
                    case CITY:
                        botUser.setCity(recivedData[1], main.getConnection());
                        if (noCityHashMap.get(chatId) != null) {
                            onUpdateReceived(noCityHashMap.get(chatId));
                            noCityHashMap.remove(chatId);
                        }
                        break;
                    case GROUPSETTINGS:
                        groupSettings(recivedData, recivedMessage.getChat(), from);
                        break;
                    case COMFORMATION:
                        waitForComformation(chatId, recivedData[0], from);
                        break;
                    case REPORT:
                        if (waitForReportHashMap.get(chatId) != null) {
                            waitForReportHashMap.remove(chatId);
                        }
                        break;
                    case HILFE:
                    case SETTINGS:
                    case STATS:
                    default:
                        break;

                }
            }
        }
    }

    private void waitForDescription(Long chatId, Message recivedMessage, String recivedText) {
        if (descriptionExpectedHashMap.get(chatId) != null) {
            deleteMessage(descriptionExpectedHashMap.get(chatId));
            descriptionExpectedHashMap.remove(chatId);
        }
        if (recivedMessage.hasText()) {
            deleteMessage(recivedMessage);
            deleteMessage(waitForDescriptionHashMap.get(chatId));
            waitForDescriptionHashMap.remove(chatId);
            ticketControlHashMap.get(chatId).setDescription(recivedText);
            Message message = send(new WaitForPictureMessage(chatId));
            waitForPictureHashMap.put(chatId, message);
        } else {
            CustomSendMessage sendMessage = new CustomSendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Deine Nachricht beinhaltet kein Text");
            Message message = send(sendMessage);
            descriptionExpectedHashMap.put(chatId, message);
        }
    }

    private void waitForPicture(long chatId) {
        waitForPictureHashMap.remove(chatId);
        Message message = send(ticketControlHashMap.get(chatId).getComformationMessage(chatId));
        waitForComformationHashMap.put(chatId, message);
    }

    private void waitForPicture(Long chatId, Message recivedMessage) {
        if (photoExpectedHashMap.get(chatId) != null) {
            deleteMessage(photoExpectedHashMap.get(chatId));
            photoExpectedHashMap.remove(chatId);
        }
        if (recivedMessage.hasPhoto()) {
            waitForPictureHashMap.remove(chatId);
            String photoId = recivedMessage.getPhoto().get(0).getFileId();
            ticketControlHashMap.get(chatId).setPhotoId(photoId);
            deleteMessage(waitForPictureHashMap.get(chatId));
            Message message = send(ticketControlHashMap.get(chatId).getComformationMessage(chatId));
            waitForComformationHashMap.put(chatId, message);
        } else {
            Message photoExpectedMesage = send(new PhotoExpectedMessage(chatId));
            deleteMessage(recivedMessage);
            photoExpectedHashMap.put(chatId, photoExpectedMesage);
        }
    }

    private void waitForComformation(long chatId, String callback, User user) {
        switch (callback) {
            case ("save"):
                TicketControl ticketControl = ticketControlHashMap.get(chatId);
                Message message = send(new TicketControlMessage(channelId, ticketControl));
                ticketControl.setMessageId(message.getMessageId());
                BotUser botUser = BotUser.get(user.getId(), main.getConnection());
                ticketControl.setCreator(botUser);
                main.newTicketControl(ticketControl);
                ticketControl.save(main.getConnection(botUser.getCity()));
            case ("cancel"):
            default:
                break;
        }
        waitForComformationHashMap.remove(chatId);
        ticketControlHashMap.remove(chatId);
    }

    private void select(Long chatId, BotUser user, String[] recivedData, BEFEHL befehl) {

        Datastore cityConnection = main.getConnection(user.getCity());
        String type = recivedData[0];
        String id = recivedData[1];

        try {
            switch (type) {
                case "vehicle":
                    setVehicle(chatId, Vehicle.getVehicle(id, cityConnection), user.getCity(), befehl);
                    break;
                case "line":
                    setLine(chatId, Line.getLine(id, cityConnection), befehl);
                    break;
                case "station":
                    setStation(chatId, Station.getStation(id, cityConnection), befehl);
                    break;
                case "direction":
                    setDirection(chatId, Station.getStation(id, cityConnection), befehl);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setVehicle(Long chatId, Vehicle vehicle, City city, BEFEHL befehl) {
        TicketControl ticketControl = new TicketControl(LocalDateTime.now());
        ticketControl.setVehicle(vehicle);
        ticketControl.setCity(city);
        ticketControlHashMap.put(chatId, ticketControl);

        send(new SelectLineMessage(chatId, befehl, vehicle));
    }

    private void setLine(Long chatId, Line line, BEFEHL befehl) {
        ticketControlHashMap.get(chatId).setLine(line);
        send(new SelectStationMessage(chatId, befehl, line));
    }

    private void setStation(Long chatId, Station station, BEFEHL befehl) {
        ticketControlHashMap.get(chatId).setStation(station);
        Line line = ticketControlHashMap.get(chatId).getLine();
        Message message = send(new SelectDirectionMessage(chatId, line, befehl));
    }

    private void setDirection(Long chatId, Station station, BEFEHL befehl) {
        ticketControlHashMap.get(chatId).setDirection(station);
        Message message = send(new WaitForDescriptionMessage(chatId));
        waitForDescriptionHashMap.put(chatId, message);

    }

    private void groupSettings(String[] recivedData, Chat chat, User user) {
        long chatId = chat.getId();
        if (isChatAdmin(chat, user)) {
            TelegramGroup group = TelegramGroup.getGroup(chatId, main.getConnection());

            switch (recivedData[0]) {
                case "city":
                    group.setCity(City.getCity(recivedData[1], main.getConnection()));
                    break;
                case "leave":
                    try {
                        execute(new LeaveChat().setChatId(chatId));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    group.delete(main.getConnection());
                    return;
                case "close":
                default:
                    return;

            }
            send(new GroupSettingsMessage(group, main.getConnection(), BEFEHL.GROUPSETTINGS));
        }
    }

    //<editor-fold desc = "Telegram Bot Methodes">
    private boolean isChatAdmin(Chat chat, User user) {
        boolean isAdmin = false;
        try {
            GetChatMember getChatMember = new GetChatMember();
            getChatMember.setChatId(chat.getId());
            getChatMember.setUserId(user.getId());
            ChatMember chatMember = execute(getChatMember);
            String status = chatMember.getStatus();
            isAdmin = status.equals("administrator") || status.equals("creator");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAdmin;
    }

    private void deleteMessage(Message message) {
        send(new DeleteBotMessage(message));
    }

    private Message send(BotMethode sendBotMessages) {
        return sendBotMessages.send(this);
    }

    public enum BEFEHL {
        START, KONTROLLE, LINE, HILFE, SETTINGS, GROUPSETTINGS, REPORT, STATS, COMFORMATION, PICTURE, CITY
    }
}
