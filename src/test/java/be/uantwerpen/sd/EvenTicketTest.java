package be.uantwerpen.sd;

import be.uantwerpen.sd.database.PersonDB;
import be.uantwerpen.sd.person.Person;
import be.uantwerpen.sd.ticket.AFact;
import be.uantwerpen.sd.ticket.EvenTicket;
import be.uantwerpen.sd.ticket.EvenTicketFact;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import java.util.UUID;

public class EvenTicketTest {
    // HERE WE WANT TO TEST ALL THE METHODS OF OUR EVENTICKET UNIT
    private EvenTicket et1;
    ArrayList<Person> persons;
    ArrayList<UUID> personIDs;

    @Before
    public void initialize() {
        // Create 3 persons
        Person p1 = new Person("Bavo");
        Person p2 = new Person("Oliver");
        Person p3 = new Person("Jefke");
        // Add these 3 persons into the PerosonDB + get their IDs in the personArrayList
        PersonDB.getPersonInstance().addEntry(p1.getID(), p1);
        PersonDB.getPersonInstance().addEntry(p2.getID(), p2);
        PersonDB.getPersonInstance().addEntry(p3.getID(), p3);
        persons = new ArrayList<>();
        personIDs = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        personIDs.add(p1.getID());
        personIDs.add(p2.getID());
        personIDs.add(p3.getID());

        AFact af = new EvenTicketFact(); // eventicketfactory
        this.et1 = (EvenTicket) af.getTicket(p1.getID(), "Cafe"); // Create an even ticket for cafe, paid by p1
        this.et1.createIndebted(personIDs); // Add these people tot the indebted
    }

    @Test //NOW WE ARE TESTING ALL OUR METHODS
    public void t_setPrice() //test if setPrice works correctly
    {
        this.et1.setPrice(600);
        Assert.assertEquals("We expect the price to be 600",600.0,this.et1.getPrice(), 0.01); // Total price of the ticket
        Assert.assertEquals("We expect the price to be 200 for p2",200.0,this.et1.getIndebted().get(this.persons.get(1).getID()),0.01);

    }
    @Test
    public void t_addPerson() //test if addPerson works correctly
    {
        // If we added a new person than the prices should be updated
        this.et1.setPrice(600);
        Person p4 = new Person("Joske");
        persons.add(p4);
        personIDs.add(p4.getID());
        this.et1.addPerson(p4);
        Assert.assertEquals("We now except the price to be 150 for p4",150.0,this.et1.getIndebted().get(this.persons.get(3).getID()),0.01);
        Assert.assertEquals("We now except the price to be 150 for p2",150.0,this.et1.getIndebted().get(this.persons.get(1).getID()),0.01);
    }
    @Test
    public void t_deletePerson(){ //test if removePerson works correctly
        this.et1.setPrice(600);
        this.et1.deletePerson(persons.get(0)); // We remove p1 from the ticket, so now only p2 and p3
        Assert.assertEquals("We now except the price to be 300 for p2",300.0,this.et1.getIndebted().get(this.persons.get(1).getID()),0.01);
        Assert.assertEquals("We now except the price to be 300 for p3",300.0,this.et1.getIndebted().get(this.persons.get(2).getID()),0.01);

    }
    @Test
    public void t_createIndebted(){ // test if createIndebted works correctly
    this.et1.setPrice(900);
    this.et1.createIndebted(personIDs);
    Assert.assertEquals("We now except the size of the indebted to be 3",3,this.et1.getIndebted().size());
    Assert.assertEquals("We now except the price to be 300 for p1",300.0,this.et1.getIndebted().get(this.persons.get(0).getID()),0.01);
    }
}





