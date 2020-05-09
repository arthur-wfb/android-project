package com.ururu2909.firstapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

public class ContactListFragment extends ListFragment {
    private ContactsService mService;
    private View view;

    public interface ResultListener {
        void onComplete(Contact[] result);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ServiceProvider){
            this.mService = ((ServiceProvider) context).getService();
        }
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
        view = getView();
        mService.getContacts(callback);
    }

    private ResultListener callback = new ResultListener() {
        @Override
        public void onComplete(Contact[] result) {
            final Contact[] contacts = result;
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
                });
            }
        }
    };
}