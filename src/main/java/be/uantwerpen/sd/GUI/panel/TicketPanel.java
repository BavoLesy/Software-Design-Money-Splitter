package be.uantwerpen.sd.GUI.panel;

import be.uantwerpen.sd.GUI.frame.TicketFrame;
import be.uantwerpen.sd.controller.TicketController;
import be.uantwerpen.sd.database.PersonDB;
import be.uantwerpen.sd.database.TicketDB;
import be.uantwerpen.sd.ticket.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class TicketPanel extends JPanel implements ActionListener, Observer {
    private HashMap<UUID, Ticket> ticketDatabase;
    private ArrayList<Ticket> ticketArrayList = new ArrayList<>();
    private DefaultListModel<Ticket> listModelTicket = new DefaultListModel<>();
    private JList<Ticket> listTicket;
    private TicketController ticketController;

    public TicketPanel(TicketController ticketController){
        TicketDB.getTicketInstance().addObserver(this);
        this.ticketController = ticketController;
        draw();
        this.setVisible(true);
        }

    public void draw(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5,5,5,5);
        JLabel label = new JLabel("Tickets");
        this.add(label,c);
        refresh();
        listTicket = new JList<>(listModelTicket);
        listTicket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listTicket.setLayoutOrientation(JList.VERTICAL);
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 20;
        this.add(listTicket,c); // Use toString in Person class to show name,

        JButton addTicketBtn = new JButton("Add Ticket");
        JButton removeTicketBtn = new JButton("Remove Ticket");
        addTicketBtn.addActionListener(this);
        removeTicketBtn.addActionListener(this);
        c.gridy = 2;
        c.gridx = 2;
        this.add(addTicketBtn,c);
        c.ipadx = 0;
        c.gridy = 2;
        c.gridx = 0;
        this.add(removeTicketBtn,c);
    }
    public void refresh(){
        // https://www.programiz.com/java-programming/library/hashmap/foreach
        listModelTicket.clear();
        ticketArrayList.clear();
        ticketDatabase = TicketDB.getTicketInstance().getDb();

        ticketDatabase.forEach((key, value) -> {
            ticketArrayList.add(value); // Add the tickets to an arraylist
        });
        for (Ticket ticket : ticketArrayList) { // for all the tickets
            listModelTicket.addElement(ticket); //add them to the listModel
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Add Ticket":
                if(PersonDB.getPersonInstance().getDb().size() == 0){
                    JOptionPane.showMessageDialog(null, "Please add a person before making a ticket.","Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    TicketFrame tf = new TicketFrame(ticketController); // Opens a new frame where we can add our ticket
                }
                break;
            case "Remove Ticket":
                if (listTicket.getSelectedValue() != null) {
                    TicketDB.getTicketInstance().removeEntry(listTicket.getSelectedValue().getID());
                }else{
                    JOptionPane.showMessageDialog(null, "There is no ticket selected to delete. Please select a ticket and try again.","Warning", JOptionPane.WARNING_MESSAGE);
                }
                break;

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.refresh();
    }
}
