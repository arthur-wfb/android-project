package com.ururu2909.firstapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ururu2909.firstapp.Contact;
import com.ururu2909.firstapp.repository.Repository;

import java.util.ArrayList;

public class ContactsViewModel extends AndroidViewModel {
    private MutableLiveData<Contact> contact;
    private MutableLiveData<ArrayList<Contact>> contactList;
    private Context context;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Contact> getContact(String id){
        contact = new MutableLiveData<>();
        loadContact(id);
        return contact;
    }

    public LiveData<ArrayList<Contact>> getContactList(){
        if (contactList == null){
            contactList = new MutableLiveData<>();
            loadContactList();
        }
        return contactList;
    }

    private void loadContact(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Repository repository = new Repository(context.getContentResolver());
                contact.postValue(repository.getContact(id));
            }
        }).start();
    }

    private void loadContactList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Repository repository = new Repository(context.getContentResolver());
                contactList.postValue(repository.getContacts());
            }
        }).start();
    }
}
