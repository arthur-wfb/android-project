package com.ururu2909.firstapp;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
                String id;
                String contactName;
                boolean hasNumber;
                String phoneNumber = null;
                ArrayList<Contact> result = new ArrayList<>();
                ContentResolver contentResolver = getContentResolver();
                Cursor contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                        new String[]{ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.Contacts.HAS_PHONE_NUMBER},
                        null, null, null);
                if ((contactsCursor != null ? contactsCursor.getCount() : 0) > 0){
                    while (contactsCursor.moveToNext()){
                        id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                        contactName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        hasNumber = contactsCursor.getInt(contactsCursor.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0;
                        if (hasNumber) {
                            Cursor phoneCursor = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            phoneCursor.moveToFirst();
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneCursor.close();
                        }
                        result.add(new Contact(id, contactName, phoneNumber != null ? phoneNumber : "None"));
                    }
                }
                contactsCursor.close();
                ContactListFragment.ResultListener local = ref.get();
                if (local != null){
                    local.onComplete(result);
                }
            }
        }).start();
    }

    public void getContact(ContactDetailsFragment.ResultListener callback, final String id){
        final WeakReference<ContactDetailsFragment.ResultListener> ref = new WeakReference<>(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String contactName = null;
                ArrayList<String> phoneNumbers = new ArrayList<>();
                ArrayList<String> emails = new ArrayList<>();
                boolean hasNumber;
                Contact result;
                ContentResolver contentResolver = getContentResolver();
                Cursor contactCursor = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{
                                ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                        },
                        ContactsContract.Contacts._ID + " = ?",
                        new String[] { id }, null);
                if (contactCursor.getCount() > 0){
                    contactCursor.moveToFirst();
                    contactName = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    hasNumber = contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0;
                    if (hasNumber){
                        Cursor phonesCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER },
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{ id }, null);
                        while (phonesCursor.moveToNext()){
                            phoneNumbers.add(phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        }
                        phonesCursor.close();
                    }
                    Cursor emailsCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            new String[]{ ContactsContract.CommonDataKinds.Email.ADDRESS },
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{ id }, null);
                    while (emailsCursor.moveToNext()){
                        emails.add(emailsCursor.getString(emailsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
                    }
                    emailsCursor.close();
                }
                contactCursor.close();
                result = new Contact(id, contactName,
                        phoneNumbers.size() > 0 ? phoneNumbers.get(0) : "None",
                        phoneNumbers.size() > 1 ? phoneNumbers.get(1) : "None",
                        emails.size() > 0 ? emails.get(0) : "None",
                        emails.size() > 1 ? emails.get(1) : "None",
                        "None");

                ContactDetailsFragment.ResultListener local = ref.get();
                if (local != null){
                    local.onComplete(result);
                }
            }
        }).start();
    }
}