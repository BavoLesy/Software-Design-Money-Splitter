package be.uantwerpen.sd.controller;


import be.uantwerpen.sd.database.TicketDB;
import be.uantwerpen.sd.person.Person;
import be.uantwerpen.sd.ticket.AFact;
import be.uantwerpen.sd.ticket.Ticket;

public class TicketController {


    public Ticket createTicket(AFact af, Person person, String type) {
        Ticket ticket = af.getTicket(person.getID(), type); // still an abstract ticket, so only personID and ticket type
        TicketDB.getTicketInstance().addEntry(ticket.getID(), ticket); // add this ticket to the ticket DB
        return ticket; //return this ticket
    }

    // If i put a ticket into the database, i need to add the type of ticket, myself as payer, the total price if split evenly
    // Otherwise, the price of everything everyone bought!




}



