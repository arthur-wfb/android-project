package com.ururu2909.firstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ContactViewHolder> implements Filterable {
    private ArrayList<Contact> contacts;
    private ArrayList<Contact> contactsAll;
    private CustomAdapter adapter;
    private OnContactListener mOnContactListener;

    CustomAdapter(ArrayList<Contact> contacts, OnContactListener mOnContactListener){
        this.contacts = contacts;
        this.contactsAll = new ArrayList<>(contacts);
        this.adapter = this;
        this.mOnContactListener = mOnContactListener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contactName;
        TextView contactNumber;
        OnContactListener onContactListener;

        ContactViewHolder(View v, OnContactListener onContactListener) {
            super(v);
            contactName = v.findViewById(R.id.contactName);
            contactNumber = v.findViewById(R.id.contactPhoneNumber);
            this.onContactListener = onContactListener;

            itemView.setOnClickListener(this);
        }

        void bind(Contact contact){
            contactName.setText(contact.getName());
            contactNumber.setText(contact.getPhoneNumber1());
        }

        @Override
        public void onClick(View v) {
            onContactListener.onContactClick(contacts.get(getAdapterPosition()).getId());
        }
    }

    public interface OnContactListener{
        void onContactClick(String id);
    }

    @NonNull
    @Override
    public CustomAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_contact, parent, false);
        return new ContactViewHolder(view, mOnContactListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ContactViewHolder holder, final int position) {
        holder.bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Contact> filteredContacts = new ArrayList<>();

            if (constraint.toString().isEmpty()){
                filteredContacts.addAll(contactsAll);
            } else {
                for (Contact contact : contactsAll) {
                    if (contact.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredContacts.add(contact);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredContacts;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contacts.clear();
            contacts.addAll((Collection<? extends Contact>) results.values);

            ContactDiffUtilCallback contactDiffUtilCallback = new ContactDiffUtilCallback(contactsAll, contacts);
            DiffUtil.DiffResult contactDiffResult = DiffUtil.calculateDiff(contactDiffUtilCallback);
            contactDiffResult.dispatchUpdatesTo(adapter);
        }
    };
}