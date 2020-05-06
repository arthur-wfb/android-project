package com.ururu2909.firstapp;

class Contact {
    private final String name;
    private final String phoneNumber;

    static final Contact[] contacts = {
            new Contact("Мама", "10987654321"),
            new Contact("Юлий Цезарь", "12345678910")
    };

    private Contact(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    String getName(){
        return name;
    }

    String getPhoneNumber(){
        return phoneNumber;
    }
}