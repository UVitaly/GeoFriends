package com.example.luf;

import java.util.ArrayList;

class UserParams
{
    static ArrayList<String> fullName = new ArrayList<String>();
    static ArrayList<String> age = new ArrayList<String>();
    static ArrayList<String> email = new ArrayList<String>();
    static ArrayList<Double> currentLocX = new ArrayList<Double>();
    static ArrayList<Double> currentLocY = new ArrayList<Double>();

    public UserParams(ArrayList<String> fullName, ArrayList<String> age, ArrayList<String> email)
    {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
    }

    public void SetParams(ArrayList<String> fullName, ArrayList<String> age, ArrayList<String> email)
    {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
    }
}