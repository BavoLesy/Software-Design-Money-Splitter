package be.uantwerpen.sd.ticket;

import be.uantwerpen.sd.person.Person;

import java.util.ArrayList;
import java.util.UUID;

public class EvenTicket extends Ticket {

    public EvenTicket(UUID personID, String type) {
        super(personID, type);
    } //constructor of even ticket

    @Override
    public void setPrice(double price){ //set price of the ticket
        this.price = price;     // set the price of the ticket
        ArrayList<UUID> personsIndebt = new ArrayList<>(this.indebted.keySet()); // Make an ArrayList of All UUID of people who are involved
        this.createIndebted(personsIndebt);  // Put these people in the HashMap
    }

    public void addPerson(Person person){                // Add a person and update existing prices
        if(!this.indebted.containsKey(person.getID())) { // Check if the person already exists
            this.indebted.put(person.getID(), 0.0);      // if not, put the person in the HashMap
        }
        double meanPrice = this.price / this.indebted.size();   // Calculate the new price
        this.indebted.forEach((id,price) -> this.indebted.put(id, meanPrice));  //update the price for ever person because of the new person
    }
    public void deletePerson(Person person) { // Delete person from a bill
        if (this.indebted.containsKey(person.getID())) { //Check if the person exists
            this.indebted.remove(person.getID());
        }
        double meanPrice = this.price / this.indebted.size();   // Calculate the new price
        this.indebted.forEach((id,price) -> this.indebted.put(id, meanPrice));  //update the price for ever person
    }

    public void createIndebted(ArrayList<UUID> persons){     // Add a list of persons to a ticket
        this.indebted.clear();                               // clear old indebted
            for (UUID personID: persons){                    //
                this.indebted.put(personID, this.price / persons.size());
            }
        }
    }






