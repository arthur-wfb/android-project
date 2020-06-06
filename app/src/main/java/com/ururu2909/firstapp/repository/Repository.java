package com.ururu2909.firstapp.repository;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.ururu2909.firstapp.Contact;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Repository {
    private WeakReference<ContentResolver> weakContentResolver;
    public Repository(ContentResolver contentResolver) {
        weakContentResolver = new WeakReference(contentResolver);
    }

    public ArrayList<Contact> getContacts(){
        return getContactsByMatchOfName("");
    }

    public ArrayList<Contact> getContactsByMatchOfName(String charSequence){
        String id;
        String contactName;
        boolean hasNumber;
        String selection;
        String[] args;
        String phoneNumber = null;
        if (charSequence.equals("")){
            selection = null;
            args = null;
        } else {
            selection = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
            args = new String[] { "%"+charSequence+"%" };
        }
        ArrayList<Contact> result = new ArrayList<>();
        ContentResolver contentResolver = weakContentResolver.get();
        Cursor contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER},
                        selection, args, null);
        try {
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
                        try {
                            phoneCursor.moveToFirst();
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                        } catch (Exception e) {
                            Log.d("xxx", e.getMessage());
                        } finally {
                            phoneCursor.close();
                        }
                    }
                    result.add(new Contact(id, contactName, phoneNumber != null ? phoneNumber : "None"));
                }
            }
        } catch (Exception e){
            Log.d("xxx", e.getMessage());
        } finally {
            if (contactsCursor != null){
                contactsCursor.close();
            }
        }
        return result;
    }

    public Contact getContact(final String id){
        String contactName = null;
        ArrayList<String> phoneNumbers = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();
        boolean hasNumber;
        String date = null;
        Contact result;
        ContentResolver contentResolver = weakContentResolver.get();
        Cursor contactCursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER
                },
                ContactsContract.Contacts._ID + " = ?",
                new String[] { id }, null);
        try {
            if (contactCursor.getCount() > 0){
                contactCursor.moveToFirst();
                contactName = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                hasNumber = contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0;
                if (hasNumber){
                    Cursor phonesCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER },
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{ id }, null);
                    try {
                        while (phonesCursor.moveToNext()){
                            phoneNumbers.add(phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        }
                    } catch (Exception e) {
                        Log.d("xxx", e.getMessage());
                    } finally {
                        phonesCursor.close();
                    }

                }
                Cursor emailsCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        new String[]{ ContactsContract.CommonDataKinds.Email.ADDRESS },
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{ id }, null);
                try {
                    while (emailsCursor.moveToNext()){
                        emails.add(emailsCursor.getString(emailsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
                    }
                } catch (Exception e) {
                    Log.d("xxx", e.getMessage());
                } finally {
                    emailsCursor.close();
                }
                Cursor birthdayCursor = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Event.START_DATE},
                        ContactsContract.Data.CONTACT_ID + " = ? AND " +
                                ContactsContract.CommonDataKinds.Event.TYPE + " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                        new String[]{String.valueOf(id)},
                        null
                );
                try {
                    while (birthdayCursor.moveToNext()){
                        date = birthdayCursor.getString(birthdayCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                    }
                } catch (Exception e) {
                    Log.d("xxx", e.getMessage());
                } finally {
                    birthdayCursor.close();
                }
            }
        } catch (Exception e){
            Log.d("xxx", e.getMessage());
        } finally {
            contactCursor.close();
        }

        result = new Contact(id, contactName,
                phoneNumbers.size() > 0 ? phoneNumbers.get(0) : "None",
                phoneNumbers.size() > 1 ? phoneNumbers.get(1) : "None",
                emails.size() > 0 ? emails.get(0) : "None",
                emails.size() > 1 ? emails.get(1) : "None",
                date != null ? date : "None");
        return result;
    }
}
