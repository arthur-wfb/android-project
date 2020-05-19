package com.ururu2909.firstapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ContactDetailsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private ContactsService mService;
    private TextView contactName;
    private TextView contactPhoneNumber;
    private TextView contactBirthDate;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

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
        contactBirthDate = (TextView) view.findViewById(R.id.contactBirthDate);
        Switch birthdayNotifySwitch = (Switch) view.findViewById(R.id.birthday_notify_switch);
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
        mService.getContact(callback, index);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactName = null;
        contactPhoneNumber = null;
        contactBirthDate = null;
    }

    private ContactDetailsFragment.ResultListener callback = new ContactDetailsFragment.ResultListener() {
        @Override
        public void onComplete(Contact result) {
            final Contact contact = result;
            if (contactName != null && contactPhoneNumber != null && contactBirthDate !=null){
                contactName.post(new Runnable() {
                    @Override
                    public void run() {
                        if (contactName != null && contactPhoneNumber != null && contactBirthDate !=null) {
                            contactName.setText(contact.getName());
                            contactPhoneNumber.setText(contact.getPhoneNumber());
                            contactBirthDate.setText(contact.getBirthDate());
                        }
                    }
                });
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        final int id = this.getArguments().getInt("index");
        Activity context = getActivity();
        Intent intent = new Intent(context, AlarmReceiver.class);
        if (isChecked){
            if (contactBirthDate != null){
                String date = contactBirthDate.getText().toString();
                intent.putExtra("contactId", id);
                intent.putExtra("birthDate", date);
                intent.putExtra("text", getString(R.string.today_birthday_notification_text) + contactName.getText().toString());
                alarmIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                try {
                    calendar.setTime(new SimpleDateFormat("dd/MM", Locale.ENGLISH).parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        alarmIntent);
            }
        } else {
            if (alarmManager != null){
                alarmIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(alarmIntent);
                alarmIntent.cancel();
            }
        }
    }
}