package be.uantwerpen.sd.database;


import java.util.*;

public abstract class Database extends Observable{
    public Database() {
    }
    public abstract void removeEntry(UUID id);
}
