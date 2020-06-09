package com.example.b10309005hw2;

public class Guest {
    public int id;
    public String number;
    public String name;

    public Guest(String number, String name)
    {
        this.id = -1;
        this.number = number;
        this.name = name;
    }

    public Guest(int id, String number, String name)
    {
        this.id = id;
        this.number = number;
        this.name = name;
    }
}
