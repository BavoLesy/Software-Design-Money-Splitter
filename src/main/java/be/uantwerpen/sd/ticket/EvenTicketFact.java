package be.uantwerpen.sd.ticket;

import java.util.UUID;

public class EvenTicketFact extends AFact {

    @Override
    public Ticket getTicket(UUID personID, String type) {
        return new EvenTicket(personID, type);
    }
}
