package be.uantwerpen.sd.person;

import java.util.UUID;

public class Person {

    private String name;    // Name of the person
    private UUID ID;        // Identifier of the person

    public Person(String name) {   // Constructor used to create a Person
        this.ID = UUID.randomUUID(); // Gives every person a random UUID when creating them
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    } // Set name

    public UUID getID() {
        return ID;
    }

    public void setID(UUID id) {
        this.ID = ID;
    }  // We can also set our own ID

    @Override
    public String toString() { // We want to display our names
        return name;
    }
}
