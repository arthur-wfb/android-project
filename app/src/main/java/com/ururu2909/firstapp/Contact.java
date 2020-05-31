package com.ururu2909.firstapp;

public class Contact {
    private String id;
    private String name;
    private String phoneNumber1;
    private String phoneNumber2;
    private String email1;
    private String email2;
    private String birthDate;

    public Contact(String id, String name, String phoneNumber1, String phoneNumber2, String email1, String email2, String birthDate){
        this.id = id;
        this.name = name;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.email1 = email1;
        this.email2 = email2;
        this.birthDate = birthDate;
    }

    public Contact(String id, String name, String phoneNumber1){
        this(id, name, phoneNumber1, "None", "None", "None", "None");
    }

    String getId(){
        return id;
    }

    String getName(){
        return name;
    }

    String getPhoneNumber1(){
        return phoneNumber1;
    }

    String getPhoneNumber2(){
        return phoneNumber2;
    }

    String getEmail1(){
        return email1;
    }

    String getEmail2(){
        return email2;
    }

    String getBirthDate(){
        return birthDate;
    }
}