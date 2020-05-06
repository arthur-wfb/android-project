package com.ururu2909.firstapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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

    public Contact[] getContacts() throws ExecutionException, InterruptedException {
        FutureTask<Contact[]> future = new FutureTask<>(new Callable<Contact[]>() {
            @Override
            public Contact[] call() throws Exception {
                return Contact.contacts;
            }
        });
        new Thread(future).start();
        return future.get();
    }

    public Contact getContact(int id) throws ExecutionException, InterruptedException {
        final int index = id;
        FutureTask<Contact> future = new FutureTask<>(new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                return Contact.contacts[index];
            }
        });
        new Thread(future).start();
        return future.get();
    }
}