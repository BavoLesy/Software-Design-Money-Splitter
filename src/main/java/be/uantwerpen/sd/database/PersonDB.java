package be.uantwerpen.sd.database;

import be.uantwerpen.sd.person.Person;

import java.util.HashMap;


import java.util.UUID;

public class PersonDB extends Database {
    private static PersonDB personInstance; //database for tickets
    private final HashMap<UUID,Person> db; // Immutable universally unique identifier (ID to navigate through database)
    private PersonDB(){
        this.db = new HashMap<UUID,Person>();
    }

    // Singleton implementation

    public static PersonDB getPersonInstance(){ // Get person database
        if (personInstance == null){ // If there is no database
            personInstance = new PersonDB(); // Make a database
        }
        return personInstance;
    }
    public void addEntry(UUID id,Person p){ // Add entry to the database (T = either Ticket  or Person)
        this.db.put(id,p);          // Add entry t in the database with UUID id
        this.setChanged();          // Set changed to true
        this.notifyObservers();     // Notify the Observers that something changed
    }
    public void removeEntry(UUID id){ // Remove entry from the database
        if(db.containsKey(id)) {    // If the given ID has a DB entry
            this.db.remove(id);     // Remove this entry
        }
        this.setChanged();          // Set changed to true
        this.notifyObservers();     // Notify the Observers that something changed
    }
    public Person getEntry(UUID id){
        return this.db.get(id);
    }

    public HashMap<UUID, Person> getDb() {
        return db;
    }

}
