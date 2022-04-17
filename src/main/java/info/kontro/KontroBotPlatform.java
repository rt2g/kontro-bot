package info.kontro;

import info.kontro.mongo.TicketControl;

public interface KontroBotPlatform {
    void newTicketControl(TicketControl ticketControl);

    void deleteTicketControl(TicketControl ticketControl);

    void deleteDescription(TicketControl ticketControl);
}
