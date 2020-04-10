package com.example.twojepiwo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.twojepiwo.LoginRegister.LoginActivity;
import com.example.twojepiwo.Receivers.DailyBeerNotificationReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class PreferenceFragment extends PreferenceFragmentCompat{

    private  Preference mLogOut;
    private EditTextPreference mPassword;
    private EditTextPreference mWeight;
    private EditTextPreference mHeight;
    private SwitchPreference mDailyNotification;

    SharedPreferences mainSharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    private String mUserID;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_screen,rootKey);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserID = mUser.getUid();

        mainSharedPreferences = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        mHeight = findPreference("height");
        mLogOut = findPreference("log_out");
        mPassword = findPreference("password");
        mWeight = findPreference("weight");
        mDailyNotification = findPreference("daily_notifications");


        mHeight.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        mWeight.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        mWeight.setSummary(getResources().getString(R.string.settings_wage_summary) +
                " " + mainSharedPreferences.getInt(LoginActivity.SHARED_WEIGHT,0));
        mHeight.setSummary(getResources().getString(R.string.settings_height_summary) +
                " " + mainSharedPreferences.getInt(LoginActivity.SHARED_HEIGHT,0));



        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key){
                    case "height":
                        setHeight(sharedPreferences,key);
                        break;
                    case "weight":
                        setWeight(sharedPreferences,key);
                    default:
                        break;
                    case "daily_notifications":
                        if(mDailyNotification.isChecked())
                        {
                            sendDailyNotification(true);
                        }else {
                            sendDailyNotification(false);
                        }
                }
            }
        };


        setLogout();

    }

    private void setWeight(SharedPreferences sharedPreferences, String key) {
        String weightSummary = getResources().getString(R.string.settings_wage_summary);
        int newWeight = Integer.parseInt(sharedPreferences.getString(key,""));
        mainSharedPreferences.edit().putInt(LoginActivity.SHARED_WEIGHT,newWeight).apply();
        mWeight.setSummary(weightSummary + " " + mainSharedPreferences.getInt(key,0));
        mReference = mDatabase.getReference().child("Users").child(mUserID);
        mReference.child("weight").setValue(newWeight);

    }

    private void setLogout() {
        assert mLogOut != null;
        mLogOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(mUser != null) {
                    mAuth.signOut();
                    getPreferenceManager().getSharedPreferences().edit().clear().apply();
                    mainSharedPreferences.edit().clear().apply();
                    Intent signOutIntent = new Intent(getActivity(), LoginActivity.class);
                    signOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    signOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(signOutIntent);

                }
                return true;
            }
        });
    }

    private void setHeight(SharedPreferences sharedPreferences, String key)
    {
        String heightSummary = getResources().getString(R.string.settings_height_summary);
        mHeight = findPreference(key);
        int newHeight = Integer.parseInt(sharedPreferences.getString(key,""));
        mainSharedPreferences.edit().putInt(LoginActivity.SHARED_HEIGHT,newHeight).apply();
        mHeight.setSummary(heightSummary + " " + mainSharedPreferences.getInt(key,0));
        mReference = mDatabase.getReference().child("Users").child(mUserID);
        mReference.child("weight").setValue(newHeight);
    }

    private void sendDailyNotification(boolean setUp) {
        //TODO: fix bug that first run is not on 15 if after
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,15);
        calendar.set(Calendar.MINUTE, 10);

        Intent dailyNotificationIntent = new Intent(getContext(), DailyBeerNotificationReceiver.class);

        dailyNotificationIntent.putExtra(DailyBeerNotificationReceiver.DAILY_NOTIFICATION_ID, 1);

        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(
                getActivity().getApplicationContext(),0,dailyNotificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        if (setUp)
        {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,notificationPendingIntent);
            Toast.makeText(getContext(),getResources().getString(R.string.daily_notification_reminder),Toast.LENGTH_SHORT).show();
        }else alarmManager.cancel(notificationPendingIntent);

    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
