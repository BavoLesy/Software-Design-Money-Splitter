package be.uantwerpen.sd.controller;


import be.uantwerpen.sd.database.PersonDB;
import be.uantwerpen.sd.database.TicketDB;
import be.uantwerpen.sd.person.Person;
import be.uantwerpen.sd.ticket.Ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CalculateBill {
    private HashMap<UUID, Person> personDB;
    private HashMap<UUID, Ticket> ticketDB;
    private HashMap<UUID, Double> totalBill;
    private ArrayList<String> printBill = new ArrayList<>(); // Make arrayList to add all the bills

    public CalculateBill(){
        this.personDB = PersonDB.getPersonInstance().getDb();
        this.ticketDB = TicketDB.getTicketInstance().getDb();
        this.totalBill = new HashMap<>();
    }

    public HashMap<UUID, Double> getTotalBill() { // Add all the tickets into one HashMap
        personDB.forEach((key,value) -> { // For every entry in the personDB
            this.totalBill.put(key, 0.0);// Add these people ID's  in the totalBill HashMap (NO PRICE YET)
        });
        ticketDB.forEach((key,value) -> { // For every ticket in the ticketDB
            if(totalBill.containsKey(value.getPersonID())){ // If the person that paid is in the totalbill
                // Put the price of this bill (with previous bills) at this person's ID
                this.totalBill.put(value.getPersonID(), this.totalBill.get(value.getPersonID())+value.getPrice());
            }
            for(UUID id: value.getIndebted().keySet()){  // check for every person ID if they are indebted indebted people
                if(totalBill.containsKey(id)){ // If the totalbill contains this person
                    this.totalBill.put(id,this.totalBill.get(id)- value.getIndebted().get(id)); // Lower the money they are owed
                }
            }
                });
        return totalBill; // Return a HashMap will all the tickets added up
    }
    public String finalBill(){  // Print out a String with the values of who needs to pay who
        printBill.clear(); // Clear existing bill
        this.totalBill = getTotalBill(); // Get new bill
        findPath(this.totalBill); // Find who needs to pay who
        StringBuilder bill = new StringBuilder(); // New string to write to
        System.out.println(printBill); // Also print in the console
        printBill.forEach(bill::append); // Add every person to this stringBuilder
        return bill.toString(); // return this as string
    }
    /*
    I USED  @soumyasethy's algorithm to determine who needs to pay who: you can find the code here:
    //https://medium.com/@soumyasethy/shortest-path-or-minimum-cash-flow-algorithm-using-java-5848d4148a76
    https://github.com/soumyasethy/ShortestPath-CashFlow-Algorithm-Splitwise
     */
    public void findPath(HashMap<UUID, Double> totalBill) {
     Double Max_Value = Collections.max(totalBill.values()); // Get max of the totalBill
        Double Min_Value = Collections.min(totalBill.values()); // Get min of the totalBill
        if (!Max_Value.equals(Min_Value)) { // If these are not equal
            UUID Max_Key = (UUID) getKeyFromValue(totalBill, Max_Value); // Get max ID of the totalBill
            UUID Min_Key = (UUID) getKeyFromValue(totalBill, Min_Value); // Get min ID of the totalBill
            double result = Max_Value + Min_Value; // result
            result = round(result, 1); // Round the result
            if ((result >= 0.0)) { // If the result is bigger than 0
                printBill.add(personDB.get(Min_Key).getName() + " needs to pay " + personDB.get(Max_Key).getName() + ": " + round(Math.abs(Min_Value), 2) + "\n");
                //System.out.println(personDB.get(Min_Key).getName() + " needs to pay " + personDB.get(Max_Key).getName() + ": " + round(Math.abs(Min_Value), 2));
                totalBill.remove(Max_Key); // Remove these
                totalBill.remove(Min_Key); // Remove these
                totalBill.put(Max_Key, result); // Put new maxKey in
                totalBill.put(Min_Key, 0.0); // Put min key back to 0.0 because he paid
            } else { // if the result is < 0
                printBill.add(personDB.get(Min_Key).getName() + " needs to pay " + personDB.get(Max_Key).getName() + ": " + round(Math.abs(Max_Value) , 2) + "\n");
                //System.out.println(personDB.get(Min_Key).getName() + " needs to pay " + personDB.get(Max_Key).getName() + ":" + round(Math.abs(Max_Value), 2));
                totalBill.remove(Max_Key);
                totalBill.remove(Min_Key);
                totalBill.put(Max_Key, 0.0);
                totalBill.put(Min_Key, result);
            }
            findPath(totalBill);
        }

    }

    public static Object getKeyFromValue(HashMap hm, Double value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
