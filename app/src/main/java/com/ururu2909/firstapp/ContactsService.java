package com.ururu2909.firstapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.lang.ref.WeakReference;

public class ContactsService extends Service {
    private final IBinder mBinder = new MyBinder();

    class MyBinder extends Binder {
        ContactsService getService() {
            return ContactsService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void getContacts(ContactListFragment.ResultListener callback){
        final WeakReference<ContactListFragment.ResultListener> ref = new WeakReference<>(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Contact[] result = Contact.contacts;
                ContactListFragment.ResultListener local = ref.get();
                if (local != null){
                    local.onComplete(result);
                }
            }
        }).start();
    }

    public void getContact(ContactDetailsFragment.ResultListener callback, int id){
        final WeakReference<ContactDetailsFragment.ResultListener> ref = new WeakReference<>(callback);
        final int index = id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Contact result = Contact.contacts[index];
                ContactDetailsFragment.ResultListener local = ref.get();
                if (local != null){
                    local.onComplete(result);
                }
            }
        }).start();
    }
}