package be.uantwerpen.sd.database;

import be.uantwerpen.sd.ticket.Ticket;

import java.util.HashMap;
import java.util.UUID;

public class TicketDB extends Database {
    private static TicketDB ticketInstance; //database for persons
    private final HashMap<UUID,Ticket> db; // Immutable universally unique identifier (ID to navigate through database)
    private TicketDB(){
        this.db = new HashMap<UUID,Ticket>();
    }
    //singleton implementation
    public static TicketDB getTicketInstance(){ // Get ticket database
        if (ticketInstance == null){ // If there is no database
            ticketInstance = new TicketDB(); // Make a database
        }
        return ticketInstance;
    }
    public void addEntry(UUID id,Ticket t){ // Add ticket entry to the database
        this.db.put(id,t);          // Add entry t in the database with UUID id
        this.setChanged();
        this.notifyObservers();     // Notify the Observers that something changed
    }

    public void removeEntry(UUID id){ // Remove entry from the database
        if(db.containsKey(id)) {    // If the given ID has a DB entry
            this.db.remove(id);     // Remove this entry
        }
        this.setChanged();
        this.notifyObservers();     // Notify the Observers that something changed
    }

    public HashMap<UUID, Ticket> getDb() {
        return db;
    }
}
