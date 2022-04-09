package be.uantwerpen.sd.GUI.frame;



import be.uantwerpen.sd.database.PersonDB;
import be.uantwerpen.sd.person.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonFrame extends JFrame implements ActionListener {
    private JTextField personName;
    GridBagConstraints c = new GridBagConstraints();
    public PersonFrame(){
        super("Money Tracker: Add Person");
        this.setSize(400,300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocation(600,200);
        draw();
        this.setVisible(true);
    }

    public void draw(){
        this.setLayout(new GridBagLayout());
        //add label
        c.insets = new Insets(10,10,10,10);
        c.gridx = 1;
        c.gridy = 0;
        JLabel label = new JLabel("Person Name");
        this.add(label,c);
        // add personname textfield
        this.personName = new JTextField(10);
        personName.addActionListener(this);
        c.gridx = 1;
        c.gridy = 1;
        this.add(personName,c);
        // add cancelbtn
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        c.gridx = 0;
        c.gridy = 2;
        this.add(cancelBtn,c);
        // add confirmbtn
        JButton confirmBtn = new JButton("Add Person");
        confirmBtn.addActionListener(this);
        c.gridx = 2;
        c.gridy = 2;
        this.add(confirmBtn,c);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "Add Person":
                if(personName.getText().equals("")){ // when there is no name
                    JOptionPane.showMessageDialog(null, "Please fill in a name", "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    Person p = new Person(personName.getText());
                    PersonDB.getPersonInstance().addEntry(p.getID(),p); // Observers get notified that the DB changed (PersonPanel)
                    this.setVisible(false);
                    this.dispose();
                }
                break;
            case "Cancel":
                this.setVisible(false);
                this.dispose();
        }
    }
}
