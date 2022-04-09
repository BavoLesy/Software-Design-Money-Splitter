package be.uantwerpen.sd.ticket;

import be.uantwerpen.sd.person.Person;

import java.util.UUID;

public class UnevenTicket extends Ticket {
    public UnevenTicket(UUID personID, String type) {
        super(personID, type);
    }

    public void computePrice(){
        double price = 0.0; // Set price to 0
        for(UUID id: this.indebted.keySet()){ // Get the indebted values for every ID
            price += this.indebted.get(id); // Get prices
        }
        this.price = price; // Add all of these together
    }

    public void addPerson(Person person, double price){    // Add a person and update existing prices
        if(!this.indebted.containsKey(person.getID())) { // Check if the person already exists
            this.indebted.put(person.getID(), 0.0);      // if not, put the person in the HashMap
        }
        this.indebted.put(person.getID(), price);  //update the price for this person
        computePrice();     // Get the total price by adding everything together
    }
    public void deletePerson(Person person) {
        if (this.indebted.containsKey(person.getID())) { // Check if the person exists
            this.indebted.remove(person.getID());   // Remove the person
        }
        computePrice();

    }
}
