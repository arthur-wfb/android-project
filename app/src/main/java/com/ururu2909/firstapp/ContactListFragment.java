package com.ururu2909.firstapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import java.util.concurrent.ExecutionException;

public class ContactListFragment extends ListFragment {
    private ContactsService mService;
    Contact[] contacts;

    public interface CanGetService {
        ContactsService getService();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CanGetService){
            this.mService = ((CanGetService) context).getService();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Список контактов");

        try {
            contacts = mService.getContacts();
        } catch (ExecutionException | InterruptedException | NullPointerException e){
            Log.d("exc", e.getMessage());
        }

        ArrayAdapter<Contact> contactAdapter = new ArrayAdapter<Contact>(getActivity(), 0, contacts){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.fragment_contact, null, false);
                }
                TextView nameView = (TextView) convertView.findViewById(R.id.contactName);
                TextView phoneNumberView = (TextView) convertView.findViewById(R.id.contactPhoneNumber);

                Contact currentContact = contacts[position];

                nameView.setText(currentContact.getName());
                phoneNumberView.setText(currentContact.getPhoneNumber());

                return convertView;
            }
        };
        setListAdapter(contactAdapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        ContactDetailsFragment detailsFragment = ContactDetailsFragment.newInstance((int) id);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, detailsFragment).addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Список контактов");
    }
}