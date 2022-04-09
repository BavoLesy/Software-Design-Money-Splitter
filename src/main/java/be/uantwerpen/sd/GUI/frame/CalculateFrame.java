package be.uantwerpen.sd.GUI.frame;

import be.uantwerpen.sd.controller.CalculateBill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculateFrame extends JFrame implements ActionListener {
    GridBagConstraints c = new GridBagConstraints();
    private CalculateBill calculateBill;
    public CalculateFrame(){
        super("Money Tracker: Calculated values");
        this.setSize(600,500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocation(750,250);
        draw();
        this.setVisible(true);

    }
    public void draw(){
        this.setLayout(new GridBagLayout());
        c.insets = new Insets(10, 10, 10, 10); //padding
        c.gridx = 1;
        c.gridy = 0;
        JLabel label = new JLabel("Final Bill");
        this.add(label,c);
        calculateBill = new CalculateBill(); // Create new CalculateBill Object.
        JTextArea bill = new JTextArea(calculateBill.finalBill(),20,20);
        c.gridx = 1;
        c.gridy = 1;
        this.add(bill,c);
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        c.gridx = 1;
        c.gridy = 2;
        this.add(cancelBtn,c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Cancel".equals(e.getActionCommand())) {
            this.setVisible(false);
            this.dispose();
        }
    }
}
