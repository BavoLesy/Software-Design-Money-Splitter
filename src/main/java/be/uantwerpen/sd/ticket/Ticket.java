package be.uantwerpen.sd.ticket;

import be.uantwerpen.sd.database.PersonDB;



import java.util.HashMap;
import java.util.UUID;

public abstract class Ticket {

    protected UUID ID;                          // ID of the ticket (used to identify it)
    protected UUID personID;                    // Name of the person who paid
    protected String type;                      // Type of ticket (airplane ticket, bus ticket, restaurant bill, ...)
    protected double price;                     // The total price of the ticket
    protected HashMap<UUID, Double> indebted;   // Link between who owes who money + how much

    public Ticket(UUID personID, String type) {
        this.ID = UUID.randomUUID();            // Every ticket gets a random ID so there are no doubles (pseudorandom)
        this.personID = personID;               // personID is also randomised (see Person class)
        this.type = type;                            // Type of ticket
        this.indebted = new HashMap<>();        // indebted HashMap
    }

    // Generated Setters and Getters
    public UUID getID() { return ID; }
    public void setID(UUID ID) { this.ID = ID; }
    public UUID getPersonID() { return personID; }
    public void setPersonID(UUID personID) { this.personID = personID; }
    public String getType() { return type; }
    public void setType(String type) { type = type; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public HashMap<UUID, Double> getIndebted() { return indebted; }
    public void setIndebted(HashMap<UUID, Double> indebted) { this.indebted = indebted; }

    @Override
    public String toString()
    {
        return String.format("%s: %.2f paid by %s",type,price,PersonDB.getPersonInstance().getEntry(personID).getName());

    }











}
