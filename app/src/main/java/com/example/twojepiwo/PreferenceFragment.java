package com.example.twojepiwo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.twojepiwo.LoginRegister.LoginActivity;
import com.example.twojepiwo.Receivers.DailyBeerNotificationReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.ClipboardManager;

import java.util.Calendar;

public class PreferenceFragment extends PreferenceFragmentCompat{

    private  Preference mLogOut;
    private EditTextPreference mWeight;
    private EditTextPreference mHeight;
    private SwitchPreference mDailyNotification;

    private Preference mHelp;
    private Preference mAbout;
    private Preference mPhoto;
    private Preference mTitle;

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
        mWeight = findPreference("weight");
        mDailyNotification = findPreference("daily_notifications");

        mHelp = findPreference("help");
        mAbout = findPreference("about");

        mPhoto = findPreference("photo");
        mTitle = findPreference("title");


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


        //TODO: Delete after implementing
        setDummy(mPhoto, "Implemented in future");
        setDummy(mTitle, "Implemented in future");

        setHelpAbout();
        setLogout();
    }

    private void setDummy(Preference preference, final String message) {
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(),message, Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void setHelpAbout() {
        final String link = "https://github.com/AnterroZZ/beerapp";
        mHelp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(),"Aby dowiedzieć się więcej zajrzyj do dokumentacji," +
                        " pozwoliłem sobie skopiować link do schowka :)", Toast.LENGTH_LONG).show();
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Github", link);
                clipboard.setPrimaryClip(clip);
                return true;
            }


        });

        mAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(),"Odwiedź mojego GitHuba aby dowiedzieć się więcej!", Toast.LENGTH_LONG).show();
                return true;
            }
        });
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

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment =null;
        if (preference instanceof ChangePasswordPreference) {
            dialogFragment = ChangePasswordDialogFragmentCompat.newInstance(preference.getKey());
        }
        if(dialogFragment != null)
        {
            dialogFragment.setTargetFragment(this,0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference" +
                    ".PreferenceFragment.DIALOG");
        }

        else super.onDisplayPreferenceDialog(preference);
    }
}
