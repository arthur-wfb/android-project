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
    private Repository repository;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        repository = new Repository(context.getContentResolver());
    }

    public LiveData<Contact> getContact(String id){
        if (contact == null){
            contact = new MutableLiveData<>();
        }
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

    public void loadContactListByMatchOfName(final String charSequence) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactList.postValue(repository.getContactsByMatchOfName("%"+charSequence+"%"));
            }
        }).start();
    }

    private void loadContact(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contact.postValue(repository.getContact(id));
            }
        }).start();
    }

    private void loadContactList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactList.postValue(repository.getContacts());
            }
        }).start();
    }
}