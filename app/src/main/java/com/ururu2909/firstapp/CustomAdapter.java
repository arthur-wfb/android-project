package com.ururu2909.firstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ContactViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Contact> contacts;
    private ArrayList<Contact> contactsAll;
    private CustomAdapter adapter;

    CustomAdapter(Context context, ArrayList<Contact> contacts){
        this.context = context;
        this.contacts = contacts;
        this.contactsAll = new ArrayList<>(contacts);
        this.adapter = this;
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView contactName;
        TextView contactNumber;

        ContactViewHolder(View v) {
            super(v);
            contactName = v.findViewById(R.id.contactName);
            contactNumber = v.findViewById(R.id.contactPhoneNumber);
        }

        void bind(Contact contact){
            contactName.setText(contact.getName());
            contactNumber.setText(contact.getPhoneNumber1());
        }
    }

    @NonNull
    @Override
    public CustomAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ContactViewHolder holder, final int position) {
        holder.bind(contacts.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactDetailsFragment detailsFragment = ContactDetailsFragment.newInstance(position, contacts.get(position).getId());
                FragmentTransaction ft = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, detailsFragment).addToBackStack(null);
                ft.commit();
            }
        });
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
