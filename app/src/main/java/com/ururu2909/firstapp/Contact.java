package com.ururu2909.firstapp;

import java.text.SimpleDateFormat;
import java.util.Date;

class Contact {
    private final String name;
    private final String phoneNumber;
    private final String birthdDate;

    static final Contact[] contacts = {
            new Contact("Мама", "89547965236", "14/05"),
            new Contact("Юлий Цезарь", "89874563698", "17/03")
    };

    private Contact(String name, String phoneNumber, String birthDate){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthdDate = birthDate;
    }

    String getName(){
        return name;
    }

    String getPhoneNumber(){
        return phoneNumber;
    }

    String getBirthDate(){
        return birthdDate;
    }
}