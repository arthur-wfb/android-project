package com.ururu2909.firstapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

public class ContactListFragment extends ListFragment {
    static final Contact[] contacts = {
            new Contact("Мама", "10987654321"),
            new Contact("Юлий Цезарь", "12345678910")
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Список контактов");
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

        FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() == 0){
                    getActivity().setTitle("Список контактов");
                }
            }
        });
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        ContactDetailsFragment detailsFragment = ContactDetailsFragment.newInstance((int) id);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_list, detailsFragment).addToBackStack(null);
        ft.commit();
    }
}