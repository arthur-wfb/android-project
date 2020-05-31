package com.ururu2909.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ururu2909.firstapp.viewmodel.ContactsViewModel;

import java.util.ArrayList;

public class ContactListFragment extends ListFragment {
    private View view;
    private ArrayList<Contact> contactsList;

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        ContactDetailsFragment detailsFragment = ContactDetailsFragment.newInstance((int) id, contactsList.get((int) id).getId());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, detailsFragment).addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Список контактов");
        view = getView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ContactsViewModel model = new ViewModelProvider(requireActivity()).get(ContactsViewModel.class);
        model.getContactList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(final ArrayList<Contact> contacts) {
                contactsList = contacts;
                if (view != null){
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            final ArrayAdapter<Contact> contactAdapter = new ArrayAdapter<Contact>(getActivity(), 0, contacts){
                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                    if (convertView == null){
                                        convertView = getLayoutInflater().inflate(R.layout.fragment_contact, null, false);
                                    }
                                    TextView nameView = convertView.findViewById(R.id.contactName);
                                    TextView phoneNumberView = convertView.findViewById(R.id.contactPhoneNumber);
                                    Contact currentContact = contacts.get(position);
                                    if (nameView != null && phoneNumberView != null){
                                        nameView.setText(currentContact.getName());
                                        phoneNumberView.setText(currentContact.getPhoneNumber1());
                                    }
                                    return convertView;
                                }
                            };
                            setListAdapter(contactAdapter);
                        }
                    });
                }
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}