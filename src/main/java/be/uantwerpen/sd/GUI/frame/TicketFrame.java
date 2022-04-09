package be.uantwerpen.sd.GUI.frame;

import be.uantwerpen.sd.controller.Controller;
import be.uantwerpen.sd.controller.TicketController;
import be.uantwerpen.sd.database.PersonDB;
import be.uantwerpen.sd.person.Person;
import be.uantwerpen.sd.ticket.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketFrame extends JFrame implements ActionListener, Controller {
    private JTextField ticketName;
    private JTextField ticketPrice;
    private JComboBox<Object> personComboBox;
    ArrayList<Person> personArrayList = new ArrayList<>(); // list with all people
    ArrayList<Person> personArrayList2 = new ArrayList<>(); // list will people that are not selected yet
    private ArrayList<JTextField> priceArray = new ArrayList<>(); // list with all the prices
    private ArrayList<JComboBox<Object>> comboBoxArray = new ArrayList<>(); //array of all the comboboxes
    GridBagConstraints c = new GridBagConstraints();
    //counters
    private int i = 0;
    private int j = 0;
    private int m = 0;
    boolean evenTicket = false;
    boolean unevenTicket = false;
    JButton addPerson = new JButton("Add Person");
    JButton removePerson = new JButton("Remove Person");
    JLabel label = new JLabel("Add Persons");
    private final TicketController ticketController;

    public TicketFrame(TicketController ticketController) {
        super("Money Tracker: Add Ticket");
        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocation(900, 200);
        this.ticketController = ticketController;
        draw();
        this.setVisible(true);
    }


    public void draw() {
        //https://www.javatpoint.com/java-gridbaglayout
        this.setLayout(new GridBagLayout());
        c.insets = new Insets(10, 10, 10, 10); //padding
        c.gridx = 1;
        c.gridy = 0;
        JLabel label = new JLabel("Ticket Name");
        this.add(label, c);

        this.ticketName = new JTextField(10);
        ticketName.addActionListener(this);
        c.gridx = 2;
        c.gridy = 0;
        this.add(ticketName, c);

        label = new JLabel("Person who paid");
        c.gridx = 1;
        c.gridy = 1;
        this.add(label, c);
        HashMap<UUID, Person> personDatabase = PersonDB.getPersonInstance().getDb();
        personDatabase.forEach((key, value) -> {
            personArrayList.add(value);
        });
        //https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
        //combobox lets the user choose between the persons
        this.personComboBox = new JComboBox<>(personArrayList.toArray());
        JScrollPane personPane = new JScrollPane(personComboBox);
        c.gridx = 2;
        c.gridy = 1;
        this.add(personPane, c);

        label = new JLabel("Amount");
        c.gridx = 1;
        c.gridy = 3;
        this.add(label, c);
        this.ticketPrice = new JTextField(10);
        ticketPrice.addActionListener(this);
        c.gridx = 2;
        c.gridy = 3;
        this.add(ticketPrice, c);
        // Make a buttonGroup where we add these two, because we may only choose one at a time
        JRadioButton evenBtn = new JRadioButton("Even Ticket");
        evenBtn.addActionListener(this);
        JRadioButton unevenBtn = new JRadioButton("Uneven Ticket");
        unevenBtn.addActionListener(this);
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(evenBtn);
        btnGroup.add(unevenBtn);
        c.gridx = 1;
        c.gridy = 4;
        this.add(evenBtn, c);
        c.gridx = 2;
        c.gridy = 4;
        this.add(unevenBtn, c);

        //cancel and confirm buttons
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        c.gridx = 1;
        c.gridy = 200;
        this.add(cancelBtn, c);
        JButton confirmBtn = new JButton("Add Ticket");
        confirmBtn.addActionListener(this);
        c.gridx = 2;
        c.gridy = 200;
        this.add(confirmBtn, c);

    }
    public void updatePerson() {
        personArrayList2.clear();
        PersonDB.getPersonInstance().getDb().forEach((key, value) -> { // voeg elke persoon toe
            personArrayList2.add(value);
        });
        for (JComboBox<Object> cb : comboBoxArray) { //
            personArrayList2.remove(cb.getSelectedItem()); //remove the selected item from the 2nd array
        }
        //check how many people are still in the combobox
        // add every person that is not been selected into the combobox

    }
    private void addPerson() {
        if (comboBoxArray.size() < personArrayList.size()) { // if the comboxarray is smaller then the personarray (so we can select more people)
            if (comboBoxArray.size() > 0) { //if there is one
                this.comboBoxArray.get(i - 1).setEnabled(false); // Disable the previous combo box
            }
            updatePerson();
            JComboBox<Object> comboBox2 = new JComboBox<>(personArrayList2.toArray()); //make new combobox from arraylist2
            comboBoxArray.add(comboBox2); // Add the new combobox2 to the array
            // personPane = new JScrollPane(comboBoxArray.get(i));
            c.gridy = 5 + i;
            if(unevenTicket){ // If its uneven
                JTextField price = new JTextField(5);
                priceArray.add(price);
                c.gridx = 3;
                this.add(priceArray.get(i),c); //get all the fields from i
            }
            c.gridx = 2;
            this.add(comboBoxArray.get(i), c);
            //delete the old buttons
            this.remove(addPerson);
            this.remove(removePerson);
            this.remove(label);
            //add the new button
            c.gridx = 1;
            c.gridy = 5;
            this.add(label, c);
            c.gridx = 1;
            c.gridy = 8 + i;

            this.add(addPerson, c);
            c.gridx = 2;
            c.gridy = 8 + i;
            this.add(removePerson, c);
            i++;

        }

    }

    private void removePerson() {
        if (i > 1) { // If there already is a person (i starts at 0 --> for first, is 2 if we pressed add once)
            i--; // I is 1 now
            this.comboBoxArray.get(i - 1).setEnabled(true); // Enable the previous box so if i = 2 enable 1 again
            this.remove(comboBoxArray.get(i)); //remove current combobox from screen
            comboBoxArray.remove(i); //remove current box from array [1] = 2nd box
            if(unevenTicket){ // If its uneven
                this.remove(priceArray.get(i));
                priceArray.remove(i); //
            }

        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Even Ticket":
                evenTicket = true;
                unevenTicket = false;
                if (j == 0) {
                    this.ticketPrice.setEnabled(true);
                    i = 0;
                    m = 0;
                    //we want to empty all the containers + variables
                    //empty out the spinnersArray

                    for(int n = 0; n < priceArray.size(); n++) { // For every element in the spinnersArray
                        this.remove(priceArray.get(n)); //remove this from the screen
                    }
                    priceArray.clear();
                    //empty out the combobox
                    for(int l = 0; l < comboBoxArray.size(); l++){
                        this.remove(comboBoxArray.get(l));
                    }
                    comboBoxArray.clear();
                    addPerson.removeActionListener(this);
                    removePerson.removeActionListener(this);
                    addPerson();
                    addPerson.addActionListener(this);
                    removePerson.addActionListener(this);
                    j++;
                }
                break;
            case "Add Person":
                addPerson();
                break;
            case "Remove Person":
                removePerson();
                break;
            case "Uneven Ticket":
                unevenTicket = true;
                evenTicket = false;
                if(m == 0){
                    this.ticketPrice.setEnabled(false);
                    i = 0;
                    j = 0;
                    for(int l = 0; l < comboBoxArray.size(); l++){
                        this.remove(comboBoxArray.get(l));
                    }
                    comboBoxArray.clear();
                    addPerson.removeActionListener(this);
                    removePerson.removeActionListener(this);
                    addPerson();
                    addPerson.addActionListener(this);
                    removePerson.addActionListener(this);
                m++;
            }
                break;
            case "Cancel":
                this.setVisible(false);
                this.dispose();
                break;
            case "Add Ticket":
                if (evenTicket) {
                    // Person, ticket name, ticket price
                    if(this.ticketPrice.getText().equals("")){ // Check if the textfield is empty
                        JOptionPane.showMessageDialog(null, "Please fill in a price","Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    else if(!ticketPrice.getText().chars().allMatch((Character::isDigit))) { // check that the string only contains numbers
                        JOptionPane.showMessageDialog(null, "Please only use numbers in the price field","Warning", JOptionPane.WARNING_MESSAGE);
                    } else if(this.ticketName.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "Please fill in a ticket name","Warning", JOptionPane.WARNING_MESSAGE);
                    }else{
                        createTicket((Person) this.personComboBox.getSelectedItem(), this.ticketName.getText(), Double.parseDouble(this.ticketPrice.getText()));
                        this.setVisible(false);
                        this.dispose();
                    }

                }
                if(unevenTicket) {
                    AtomicInteger p = new AtomicInteger(); // Integer used to display message only once
                    AtomicInteger t = new AtomicInteger();
                    AtomicBoolean error = new AtomicBoolean(false);
                    priceArray.forEach((key)->{
                       if(key.getText().equals("")){
                           if(p.get() ==0) {
                               JOptionPane.showMessageDialog(null, "Please fill in a price", "Warning", JOptionPane.WARNING_MESSAGE);
                               p.getAndIncrement();
                           }
                           error.set(true);
                       }
                       else if(!key.getText().chars().allMatch((Character::isDigit))){
                           if(t.get() == 0) {
                               JOptionPane.showMessageDialog(null, "Please only use numbers in the price field", "Warning", JOptionPane.WARNING_MESSAGE);
                               t.getAndIncrement();
                           }
                           error.set(true); // There is an error
                        }

                    });
                    if(this.ticketName.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill in a ticket name", "Warning", JOptionPane.WARNING_MESSAGE);
                    }else {
                        if (!error.get()) { // if there is no error
                            createTicket((Person) this.personComboBox.getSelectedItem(), this.ticketName.getText(), 0.0);
                            this.setVisible(false);
                            this.dispose();
                        }
                    }
                }
                break;


        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    @Override
    public Ticket createTicket(Person person, String type, double price) {
        if(evenTicket) {
            EvenTicketFact etf = new EvenTicketFact();
            EvenTicket et = (EvenTicket) ticketController.createTicket(etf, person, type);
            et.setPrice(price); // Set price of the ticket

            for (int k = 0; k < i; k++) { // For every person we selected as indebted (always atleast one)
                Person p = (Person) comboBoxArray.get(k).getSelectedItem();
                assert p != null;
                et.addPerson(p); // Add these people to the indebted list
            }
            return et;
        }
        else { // If the ticket is uneven
            UnevenTicketFact utf = new UnevenTicketFact();  // Create UTF
            UnevenTicket ut = (UnevenTicket) ticketController.createTicket(utf,person,type); // create UT
            for(int o = 0; o < i; o++){  // For all the people
                JTextField prices = this.priceArray.get(o); // Get their prices
                double pricePerPerson = Double.parseDouble(prices.getText());
                Person p = (Person) comboBoxArray.get(o).getSelectedItem();  // Get the persons
                assert p != null;  // Person p cant be empty
                ut.addPerson(p, pricePerPerson); // Add them to the ticket
            }
            return ut;
        }
    }
}

