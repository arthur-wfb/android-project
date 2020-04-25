package com.ururu2909.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactDetailsFragment extends Fragment {

    static ContactDetailsFragment newInstance(int index) {
        ContactDetailsFragment f = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Детали контакта");
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        int index = this.getArguments().getInt("index");
        TextView contactName = (TextView) view.findViewById(R.id.contactDetailsName);
        TextView contactPhoneNumber = (TextView) view.findViewById(R.id.contactDetailsPhoneNumber);
        contactName.setText(ContactListFragment.contacts[index].getName());
        contactPhoneNumber.setText(ContactListFragment.contacts[index].getPhoneNumber());
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setTitle("Список контактов");
    }
}
