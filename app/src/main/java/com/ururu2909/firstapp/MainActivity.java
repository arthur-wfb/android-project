package com.ururu2909.firstapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.ururu2909.firstapp.ContactsService.MyBinder;

public class MainActivity extends AppCompatActivity implements ServiceProvider, ActivityCompat.OnRequestPermissionsResultCallback {
    ContactsService mService;
    boolean mBound = false;
    boolean createdFirstTime;
    public static final int PERMISSION_REQUEST_CODE = 10;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_main);
        createdFirstTime = savedInstanceState == null;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MainActivity.this, ContactsService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder binder = (MyBinder) service;
            mService = binder.getService();
            mBound = true;
            if (createdFirstTime){
                addFragment();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    void addFragment(){
        int index = getIntent().getIntExtra("contactIndex", -1);
        if (index != -1){
            String id = getIntent().getStringExtra("contactId");
            ContactDetailsFragment detailsFragment = ContactDetailsFragment.newInstance(index, id);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, detailsFragment);
            ft.commit();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ContactListFragment fragment = new ContactListFragment();
            ft.add(R.id.container, fragment);
            ft.commit();
        }
    }

    @Override
    public ContactsService getService() {
        return mService;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainActivity.this, ContactsService.class);
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                } else {
                    Toast.makeText(this, "Приложению требуется разршенеие на чтение контактов.", Toast.LENGTH_LONG).show();
                    finish();
                }

        }
    }
}
