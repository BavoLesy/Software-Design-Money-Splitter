package be.uantwerpen.sd.GUI.panel;

import be.uantwerpen.sd.GUI.frame.PersonFrame;
import be.uantwerpen.sd.database.PersonDB;
import be.uantwerpen.sd.database.TicketDB;
import be.uantwerpen.sd.person.Person;
import be.uantwerpen.sd.ticket.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PersonPanel extends JPanel implements Observer,ActionListener {
    private HashMap<UUID, Person> PersonDatabase; // PersonDatabase
    private HashMap<UUID, Ticket> TicketDatabase; // TicketDatabase
    private ArrayList<Person> personArrayList = new ArrayList<>(); // ArrayList with all the persons
    private DefaultListModel<Person> listModelPerson = new DefaultListModel<>(); // ListModel used for JList
    private JList<Person> listPerson; // Jlist
    GridBagConstraints c = new GridBagConstraints();
    public PersonPanel(){
        PersonDB.getPersonInstance().addObserver(this); // Add observer to personDB
        draw();
        this.setVisible(true);
    }
    public void draw(){
        this.setLayout(new GridBagLayout()); // We use the gridbaglayout
        //https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5,5,5,5);
        JLabel label = new JLabel("Persons");
        this.add(label,c);
        refresh(); // update the current screen
        listPerson = new JList<>(listModelPerson);
        listPerson.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // so we can only select one at a time
        listPerson.setLayoutOrientation(JList.VERTICAL); // vertical list
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 40; // set extra width
        this.add(listPerson,c); // Use toString in Person class to show name,
        c.ipadx = 0;
        JButton addPersonBtn = new JButton("Add Person");
        JButton removePersonBtn = new JButton("Remove Person");
        addPersonBtn.addActionListener(this);
        removePersonBtn.addActionListener(this);
        c.gridy = 2;
        c.gridx = 2;
        c.ipadx = 30;
        this.add(addPersonBtn,c);
        c.ipadx = 0;
        c.gridy = 2;
        c.gridx = 0;
        this.add(removePersonBtn,c);
    }
    public void refresh(){
        //https://www.programiz.com/java-programming/library/hashmap/foreach
        listModelPerson.clear();
        personArrayList.clear();
        PersonDatabase = PersonDB.getPersonInstance().getDb();
        PersonDatabase.forEach((key, value) -> {
            personArrayList.add(value); // for each person in the database, add them to the personarraylists
        });
        for (Person person : personArrayList) {
            listModelPerson.addElement(person); // add every person to the listmodelperson
        }
        SwingUtilities.updateComponentTreeUI(this); //update screen
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Add Person":
                PersonFrame pf = new PersonFrame(); // Opens a new frame where we can add persons
                break;
            case "Remove Person":
                AtomicBoolean existingTicket = new AtomicBoolean(false);
                if (listPerson.getSelectedValue() != null) { // If we have selected a value
                    UUID selectedPerson = listPerson.getSelectedValue().getID();
                    TicketDatabase = TicketDB.getTicketInstance().getDb();  // Get the ticket Database
                    TicketDatabase.forEach((key, value) -> { // Check if there is a ticket in the database
                        if ((value.getPersonID() == selectedPerson) | (value.getIndebted().containsKey(selectedPerson))){ // If there is a ticket that was paid by this person
                            existingTicket.set(true);
                        }
                    });
                    if (existingTicket.get()) { // If there is a ticket with the selected person
                        int option = JOptionPane.showConfirmDialog(null, "Are you Sure? You will delete all tickets this person is a part of");
                        if(option == 0) {
                            ArrayList<UUID> entries = new ArrayList<>();
                            AtomicInteger i = new AtomicInteger();
                            TicketDatabase.forEach((key, value) -> { // Remove tickets
                                if ((value.getPersonID() == selectedPerson) | (value.getIndebted().containsKey(selectedPerson))) { // we have a ticket paid by selectedPerson
                                    entries.add(i.get(), key); // add all these tickets to the entries arraylist
                                    i.getAndIncrement();
                                }
                            });
                            entries.forEach((key)->{
                                TicketDB.getTicketInstance().removeEntry(key);  //remove all these tickets
                            });
                            PersonDB.getPersonInstance().removeEntry(selectedPerson); // Delete this person out of the database
                        }
                    } else // If there is no ticket with the selected person
                    {
                        PersonDB.getPersonInstance().removeEntry(selectedPerson); // Delete this person out of the database
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "There is no person selected to delete. Please select a person and try again.","Warning", JOptionPane.WARNING_MESSAGE);
                }
                break;
        }
    }
    @Override
    public void update(Observable o, Object arg) { // If the personDB is updated, refresh the list with people
        this.refresh();
    } // Update screen when we observed a change in the personDB
}
