package be.uantwerpen.sd.ticket;

import java.util.UUID;

public class UnevenTicketFact extends AFact{

    @Override
    public Ticket getTicket(UUID personID, String type) {
        return new UnevenTicket(personID, type);
    }
}
