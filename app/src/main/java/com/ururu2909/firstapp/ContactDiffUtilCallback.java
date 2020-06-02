package com.ururu2909.firstapp;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class ContactDiffUtilCallback extends DiffUtil.Callback {
    private final ArrayList<Contact> oldList;
    private final ArrayList<Contact> newList;

    ContactDiffUtilCallback(ArrayList<Contact> oldList, ArrayList<Contact> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Contact oldContact = oldList.get(oldItemPosition);
        Contact newContact = newList.get(newItemPosition);
        return oldContact.getId().equals(newContact.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Contact oldContact = oldList.get(oldItemPosition);
        Contact newContact = newList.get(newItemPosition);
        return oldContact.getName().equals(newContact.getName())
                && oldContact.getPhoneNumber1().equals(newContact.getPhoneNumber1());
    }
}