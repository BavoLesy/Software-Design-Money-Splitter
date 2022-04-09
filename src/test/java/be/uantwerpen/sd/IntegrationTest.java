package be.uantwerpen.sd;

import be.uantwerpen.sd.controller.CalculateBill;
import be.uantwerpen.sd.controller.TicketController;
import be.uantwerpen.sd.database.PersonDB;
import be.uantwerpen.sd.database.TicketDB;
import be.uantwerpen.sd.person.Person;
import be.uantwerpen.sd.ticket.EvenTicket;
import be.uantwerpen.sd.ticket.EvenTicketFact;
import be.uantwerpen.sd.ticket.UnevenTicket;
import be.uantwerpen.sd.ticket.UnevenTicketFact;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class IntegrationTest {
    TicketController ticketController;
    Person p1;
    Person p2;
    Person p3;
    EvenTicket et1;
    UnevenTicket ut1;

    @Before
    public void initialize(){
        this.ticketController = new TicketController();
        this.p1 = new Person("Bavo");
        this.p2 = new Person("Oliver");
        this.p3 = new Person("Joske");
        // Add these 3 persons into the PerosonDB + get their IDs in the personArrayList
        PersonDB.getPersonInstance().addEntry(p1.getID(), p1);
        PersonDB.getPersonInstance().addEntry(p2.getID(), p2);
        PersonDB.getPersonInstance().addEntry(p3.getID(), p3);
        this.et1 = (EvenTicket) ticketController.createTicket(new EvenTicketFact(), p1, "EvenTicketRestaurant");
        this.ut1 = (UnevenTicket) ticketController.createTicket(new UnevenTicketFact(), p2, "UnevenTicketRestaurant");
    }
    @Test
    //test if we can add people to the database, add tickets to the database and test if we can calculate our final bill correctly
    public void t_createPerson(){
        Person p4 = new Person("Jefke");
        PersonDB.getPersonInstance().addEntry(p4.getID(),p4); // add new person into the database, should be 4 now
        Assert.assertEquals("We expect the size to be 4 now",4, PersonDB.getPersonInstance().getDb().size());
        Assert.assertEquals("We expect his name to be the same in the database", p4.getName(),PersonDB.getPersonInstance().getDb().get(p4.getID()).getName());
    }
    @Test
    public void t_createEvenTicket(){
        Person p5 = new Person("Jeffy");
        PersonDB.getPersonInstance().addEntry(p5.getID(), p5);
        EvenTicket et = (EvenTicket) ticketController.createTicket(new EvenTicketFact(), p5, "EvenTicketRestaurant" ); // create new ticket
        et.setPrice(500);
        Assert.assertEquals("We expect p5 his paid ID to be the same in the database", p5.getID(),TicketDB.getTicketInstance().getDb().get(et.getID()).getPersonID());
        Assert.assertEquals("We expect the price to be the same in the database", 500.0,TicketDB.getTicketInstance().getDb().get(et.getID()).getPrice(),0.01);
        TicketDB.getTicketInstance().removeEntry(et.getID());
    }
    @Test
    public void t_createUnevenTicket(){
        Person p6 = new Person("Jonas");
        PersonDB.getPersonInstance().addEntry(p6.getID(), p6);
        UnevenTicket ut = (UnevenTicket) ticketController.createTicket(new UnevenTicketFact(), p6,"UnevenTicketRestaurant");
        ut.setPrice(600);
        Assert.assertEquals("We expect p5 his paid ID to be the same in the database", p6.getID(),TicketDB.getTicketInstance().getDb().get(ut.getID()).getPersonID());
        Assert.assertEquals("We expect the price to be the same in the database", 600.0,TicketDB.getTicketInstance().getDb().get(ut.getID()).getPrice(),0.01);
        TicketDB.getTicketInstance().removeEntry(ut.getID());
    }
    @Test
    public void t_calculateBill(){
       // Add these 3 persons as indebted on bill et1
        this.et1.setPrice(800);
        this.et1.addPerson(p1);
        this.et1.addPerson(p2);
        this.et1.addPerson(p3);
        //Uneven ticket
        this.ut1.setPrice(200);
        this.ut1.addPerson(p1,50);
        this.ut1.addPerson(p2,150);

        CalculateBill bill = new CalculateBill();
        HashMap<UUID,Double> totalBill = bill.getTotalBill();
        //P1 paid 800 and costs 266.67 + 50, he needs 483,33 back
        //P2 paid 200 and costs 266.67 + 150, he needs to pay 216.67
        //P3 paid 0 and costs 266.67, he needs to pay 266.67
        Assert.assertEquals("We expect this to be 483.33 (p1)", 483.33, totalBill.get(p1.getID()), 0.01);
        Assert.assertEquals("We expect this to be -216.67 p2)", -216.67, totalBill.get(p2.getID()), 0.01);
        Assert.assertEquals("We expect this to be -266.67 p2)", -266.67, totalBill.get(p3.getID()), 0.01);
    }
}
