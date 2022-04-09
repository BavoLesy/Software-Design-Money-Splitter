package be.uantwerpen.sd.controller;

import be.uantwerpen.sd.person.Person;
import be.uantwerpen.sd.ticket.Ticket;


public interface Controller {
    Ticket createTicket(Person person , String type, double price);

}
