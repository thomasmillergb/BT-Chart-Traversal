package com.bt;

/**
 * Created by Thomas on 27/06/2015.
 */
public class Employee {

    private final int id;
    private final int manager;
    private final String name;

    Employee(int id, String name, int manager) {
        this.id = id;
        this.manager = manager;
        this.name = name;
    }

    public int getManager(){return manager;}

    public int getId() {return id;}

    public String getName() {
        return name;
    }
    public String toString(){
        return name+ " ("+id+")";
    }


}
