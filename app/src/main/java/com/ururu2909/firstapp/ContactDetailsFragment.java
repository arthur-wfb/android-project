package com.ururu2909.firstapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutionException;

public class ContactDetailsFragment extends Fragment {
    private ContactsService mService;
    Contact contact;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ContactListFragment.CanGetService){
            this.mService = ((ContactListFragment.CanGetService) context).getService();
        }
    }

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

        try {
            contact = mService.getContact(index);
        } catch (ExecutionException | InterruptedException | NullPointerException e){
            Log.d("exc", e.getMessage());
        }

        TextView contactName = (TextView) view.findViewById(R.id.contactDetailsName);
        TextView contactPhoneNumber = (TextView) view.findViewById(R.id.contactDetailsPhoneNumber);
        contactName.setText(contact.getName());
        contactPhoneNumber.setText(contact.getPhoneNumber());
        return view;
    }
}