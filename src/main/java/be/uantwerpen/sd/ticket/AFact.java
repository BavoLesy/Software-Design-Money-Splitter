package be.uantwerpen.sd.ticket;

import java.util.UUID;

public abstract class AFact {

    public abstract Ticket getTicket(UUID personID, String type); // ID of person who paid + string for type of ticket





}
