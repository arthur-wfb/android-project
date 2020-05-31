package com.ururu2909.firstapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ururu2909.firstapp.viewmodel.ContactsViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ContactDetailsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private TextView contactName;
    private TextView contactPhoneNumber1;
    private TextView contactPhoneNumber2;
    private TextView contactEmail1;
    private TextView contactEmail2;
    private TextView contactBirthDate;
    private AlarmManager alarmManager;
    ContactsViewModel model;

    static ContactDetailsFragment newInstance(int index, String id) {
        ContactDetailsFragment f = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("id", id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(ContactsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Детали контакта");
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        int index = this.getArguments().getInt("index");
        String id = this.getArguments().getString("id");

        contactName = view.findViewById(R.id.contactDetailsName);
        contactPhoneNumber1 = view.findViewById(R.id.contactDetailsPhoneNumber);
        contactPhoneNumber2 = view.findViewById(R.id.contactDetailsPhoneNumber2);
        contactEmail1 = view.findViewById(R.id.contactDetailsEmail);
        contactEmail2 = view.findViewById(R.id.contactDetailsEmail2);
        contactBirthDate = view.findViewById(R.id.contactBirthDate);
        Switch birthdayNotifySwitch = view.findViewById(R.id.birthday_notify_switch);

        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        if (birthdayNotifySwitch != null) {
            if (alarmManager != null){
                boolean alarmUp = (PendingIntent.getBroadcast(getActivity(), index,
                        new Intent(getActivity(), AlarmReceiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null);
                if (alarmUp){
                    birthdayNotifySwitch.setChecked(true);
                } else {
                    birthdayNotifySwitch.setChecked(false);
                }
            }
            birthdayNotifySwitch.setOnCheckedChangeListener(this);
        }

        model.getContact(id).observe(getViewLifecycleOwner(), new Observer<Contact>() {
            @Override
            public void onChanged(final Contact contact) {
                if (contactName != null && contactPhoneNumber1 != null && contactPhoneNumber2 !=null
                        && contactEmail1 !=null && contactEmail2 !=null && contactBirthDate !=null) {
                    contactName.setText(contact.getName());
                    contactPhoneNumber1.setText(contact.getPhoneNumber1());
                    contactPhoneNumber2.setText(contact.getPhoneNumber2());
                    contactEmail1.setText(contact.getEmail1());
                    contactEmail2.setText(contact.getEmail2());
                    contactBirthDate.setText(contact.getBirthDate());
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactName = null;
        contactPhoneNumber1 = null;
        contactPhoneNumber2 = null;
        contactEmail1 = null;
        contactEmail2 = null;
        contactBirthDate = null;
    }

    @Override
    public void onDestroy() {
        model = null;
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        final int index = this.getArguments().getInt("index");
        Activity context = getActivity();
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent;
        if (isChecked){
            if (contactBirthDate != null){
                String date = contactBirthDate.getText().toString();
                intent.putExtra("contactIndex", index);
                intent.putExtra("birthDate", date);
                intent.putExtra("text", getString(R.string.today_birthday_notification_text) + contactName.getText().toString());
                alarmIntent = PendingIntent.getBroadcast(context, index, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                try {
                    calendar.setTime(new SimpleDateFormat("dd/MM", Locale.ENGLISH).parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                if(System.currentTimeMillis() > calendar.getTimeInMillis()) {
                    calendar.add(Calendar.YEAR, 1);
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        alarmIntent);
            }
        } else {
            if (alarmManager != null){
                alarmIntent = PendingIntent.getBroadcast(context, index, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(alarmIntent);
                alarmIntent.cancel();
            }
        }
    }
}