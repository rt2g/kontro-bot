package info.kontro.telegram.messages;

public class StartMessage extends CustomSendMessage {
    public StartMessage(long chatID) {
        setChatId(chatID);
        setText("Dieser Bot soll Menschen helfen ticketlos im BVG Netz zu fahren" +
                "\n\n/Kontrolle\nMelde eine Kontrolle" +
                "\n\n/Linie\nZeigt dir alle Kontrollen auf einer Liene an" +
                //"\n\n/Statistiken\nZeigt dir Statistiken an" +
                //"\n\n/Melden\nFalls dir eine diskriminirende Beschreibug aufgefallen ist" +
                //"\n\n/Einstellungen\nHier kannst du einstellen wie sich der Bot dir gegenüber verhält" +
                "\n\n/FAQ\nFragen und Antworten");
    }
}
