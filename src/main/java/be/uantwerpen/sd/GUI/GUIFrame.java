package be.uantwerpen.sd.GUI;

import be.uantwerpen.sd.GUI.panel.PersonPanel;
import be.uantwerpen.sd.GUI.panel.TicketPanel;
import be.uantwerpen.sd.GUI.frame.CalculateFrame;
import be.uantwerpen.sd.controller.TicketController;
import be.uantwerpen.sd.database.PersonDB;
import be.uantwerpen.sd.database.TicketDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
//I used code from https://www.javatpoint.com/java-layout-manager to get a nice structure

public class GUIFrame extends JFrame implements Observer,ActionListener {
    private static final TicketController ticketController = new TicketController();
    GridBagConstraints c = new GridBagConstraints();
    public GUIFrame(){
        super("Money Tracker Bavo Lesy & Oliver Rommens");
        this.setSize(1000,600);
        this.setLocation(600,200);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PersonDB.getPersonInstance().addObserver(this);

        draw();
        this.setVisible(true);
    }
    public void draw(){
        this.setLayout(new GridBagLayout());
        JSplitPane panels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new PersonPanel(), new TicketPanel(ticketController));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        this.add(panels, c);
        panels.setResizeWeight(0.5); // 0.5 means they both get 50%
        JButton calculate = new JButton("Calculate for whole trip"); // Add button to calculate
        calculate.addActionListener(this);
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 1;
        this.add(calculate,c);
    }
    @Override
    public void update(Observable o, Object arg)
    {
        SwingUtilities.updateComponentTreeUI(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) { // If the calculate button is pressed
        if(e.getActionCommand().equals("Calculate for whole trip"))
            if(TicketDB.getTicketInstance().getDb().size() == 0){
                JOptionPane.showMessageDialog(null, "There are no tickets yet", "Warning", JOptionPane.WARNING_MESSAGE);
            }else {
                CalculateFrame cf = new CalculateFrame();   // Open the new CalculateFrame
            }
    }

}
