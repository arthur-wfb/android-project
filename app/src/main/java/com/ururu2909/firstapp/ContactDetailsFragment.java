package com.ururu2909.firstapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactDetailsFragment extends Fragment {
    private ContactsService mService;
    private TextView contactName;
    private TextView contactPhoneNumber;

    interface ResultListener {
        void onComplete(Contact result);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ServiceProvider){
            this.mService = ((ServiceProvider) context).getService();
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
        contactName = (TextView) view.findViewById(R.id.contactDetailsName);
        contactPhoneNumber = (TextView) view.findViewById(R.id.contactDetailsPhoneNumber);
        mService.getContact(callback, index);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactName = null;
        contactPhoneNumber = null;
    }

    private ContactDetailsFragment.ResultListener callback = new ContactDetailsFragment.ResultListener() {
        @Override
        public void onComplete(Contact result) {
            final Contact contact = result;
            if (contactName != null && contactPhoneNumber != null){
                contactName.post(new Runnable() {
                    @Override
                    public void run() {
                        contactName.setText(contact.getName());
                        contactPhoneNumber.setText(contact.getPhoneNumber());
                    }
                });
            }
        }
    };
}