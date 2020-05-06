package com.ururu2909.firstapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ContactsService extends Service {
    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        ContactsService getService() {
            return ContactsService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public Contact[] getContacts(){
        return Contact.contacts;
    }

    public Contact getContact(int id){
        return Contact.contacts[id];
    }
}