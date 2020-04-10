package com.example.twojepiwo.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twojepiwo.Beers.Beers;
import com.example.twojepiwo.Beers.Harnold;
import com.example.twojepiwo.Beers.Perla;
import com.example.twojepiwo.Beers.Tatra;
import com.example.twojepiwo.Receivers.MidnightChangerReceiver;
import com.example.twojepiwo.Receivers.DailyBeerNotificationReceiver;
import com.example.twojepiwo.ProfileActivity;
import com.example.twojepiwo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private TextView mEmail;
    private TextView mPassword;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mReference;
    private User mUser;
    private String mUserID;
    private ProgressDialog mLoading;

    public static final String TAG = "TAG";
    public static final String SHARED_PREFERENCES = "preferences";
    public static final String SHARED_USERNAME = "username";
    public static final String SHARED_PASSWORD = "password";
    public static final String SHARED_EMAIL = "email";
    public static final String SHARED_WEIGHT = "weight";
    public static final String SHARED_HEIGHT = "height";
    public static final String SHARED_AGE = "age";
    public static final String SHARED_SEX = "sex";
    public static final String SHARED_FAVOURITE_BEER = "favourite";
    public static final String SHARED_LEVEL = "level";
    public static final String SHARED_PROGRESS = "lvl_progress";
    public static final String SHARED_TITLE = "title";
    public static final String SHARED_DATE = "date";
    public static final String SHARED_TOTAL_BEERS = "total_beers";
    public static final String SHARED_TODAY_BEERS = "today_beers";
    public static final String SHARED_WEEK_BEERS = "week_beers";
    public static final int PENDING_DAILY_INTENT = 4;
    public static final String SHARED_DAILY_BEER = "daily_beer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.log_email);
        mPassword = findViewById(R.id.log_password);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = new User();
    }

    public void LogIn(View view) {
        if(checkIfValid())
        {
            mAuth.signInWithEmailAndPassword(mEmail.getText().toString().trim(),
                    mPassword.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                mLoading = new ProgressDialog(LoginActivity.this);
                                mLoading.show();
                                mLoading.setContentView(R.layout.progress_loading);
                                mLoading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                getUserInfoFromServer();
                            } else Toast.makeText(getApplicationContext(),"Nie istnieje użytkownik z takim mailem i hasłem", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

   private void getUserInfoFromServer()
   {
       //TODO: Needs to be handled if any disconnection from the server occurs
       if(true) {
           mFirebaseUser = mAuth.getCurrentUser();
           mUserID = mFirebaseUser.getUid();
           mReference = mDatabase.getReference().child("Users").child(mUserID);

           mReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   mUser.setUsername(dataSnapshot.child("username").getValue().toString());
                   mUser.setPassword(dataSnapshot.child("password").getValue().toString());
                   mUser.setEmailAddress(dataSnapshot.child("emailAddress").getValue().toString());
                   mUser.setWeight(Integer.parseInt(dataSnapshot.child("weight").getValue().toString()));
                   mUser.setHeight(Integer.parseInt(dataSnapshot.child("height").getValue().toString()));
                   mUser.setAge(Integer.parseInt(dataSnapshot.child("age").getValue().toString()));
                   mUser.setSex(dataSnapshot.child("sex").getValue().toString());
                   mUser.setFavouriteBeer(dataSnapshot.child("favouriteBeer").getValue().toString());
                   mUser.setLevel(Integer.parseInt(dataSnapshot.child("level").getValue().toString()));
                   mUser.setLevelProgress(Float.parseFloat(dataSnapshot.child("levelProgress").getValue().toString()));
                   mUser.setTitle(dataSnapshot.child("title").getValue().toString());
                   mUser.setDateOfCreation(dataSnapshot.child("dateOfCreation").getValue().toString());
                   mUser.setTotalBeers(Integer.parseInt(dataSnapshot.child("totalBeers").getValue().toString()));
                   mUser.setTodayBeers(Integer.parseInt(dataSnapshot.child("todayBeers").getValue().toString()));
                   mUser.setBeers(new Beers(
                           Integer.parseInt(dataSnapshot.child("beers").child(Harnold.class.getSimpleName().toLowerCase()).getValue().toString()),
                           Integer.parseInt(dataSnapshot.child("beers").child(Tatra.class.getSimpleName().toLowerCase()).getValue().toString()),
                           Integer.parseInt(dataSnapshot.child("beers").child(Perla.class.getSimpleName().toLowerCase()).getValue().toString())
                   ));


                   //Adding all account's data to SharedPreferences stored locally
                   SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
                   SharedPreferences.Editor editor = sharedPreferences.edit();

                   editor.putString(SHARED_USERNAME,mUser.getUsername());
                   editor.putString(SHARED_PASSWORD,mUser.getPassword());
                   editor.putString(SHARED_EMAIL,mUser.getEmailAddress());
                   editor.putInt(SHARED_WEIGHT,mUser.getWeight());
                   editor.putInt(SHARED_HEIGHT,mUser.getHeight());
                   editor.putInt(SHARED_AGE,mUser.getAge());
                   editor.putString(SHARED_SEX,mUser.getSex());
                   editor.putString(SHARED_FAVOURITE_BEER,mUser.getFavouriteBeer());
                   editor.putInt(SHARED_LEVEL,mUser.getLevel());
                   editor.putFloat(SHARED_PROGRESS,mUser.getLevelProgress());
                   editor.putString(SHARED_TITLE,mUser.getTitle());
                   editor.putString(SHARED_DATE,mUser.getDateOfCreation());
                   editor.putInt(SHARED_TOTAL_BEERS,mUser.getTotalBeers());
                   editor.putInt(SHARED_TODAY_BEERS,mUser.getTodayBeers());
                   editor.putString(SHARED_DAILY_BEER, dataSnapshot.child("dailyBeer").getValue().toString());
                   editor.putInt(Harnold.class.getSimpleName().toLowerCase(),mUser.getBeers().getHarnold());
                   editor.putInt(Tatra.class.getSimpleName().toLowerCase(),mUser.getBeers().getTatra());
                   editor.putInt(Perla.class.getSimpleName().toLowerCase(),mUser.getBeers().getPerla());
                   editor.apply();
                   Log.d(TAG,"All data stored locally in SharedPreferences");


                   sendDailyNotification();
                   changeDailyBeerEveryday();

                   PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.settings_screen,false);
                   mLoading.dismiss();
                   Intent logInIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                   startActivity(logInIntent);
                   finish();
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }
   }

    public void goToRegister(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);

    }

    //TODO: Need to do the EditTexts check
    private boolean checkIfValid()
    {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        //TODO: To do later, Email Regex
        String emailRegex = "[.*][@][.*][.][.*]";

        if(email.equals("") || password.equals(""))
        {
            Toast.makeText(this,"Nie możesz zostawić pola emailu lub hasła pustego!",Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }


    private void sendDailyNotification() {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE, 10);

        Intent dailyNotificationIntent = new Intent(this, DailyBeerNotificationReceiver.class);

        dailyNotificationIntent.putExtra(DailyBeerNotificationReceiver.DAILY_NOTIFICATION_ID, 1);

        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(
                this,0,dailyNotificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);



        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,notificationPendingIntent);
    }

    private void changeDailyBeerEveryday()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);

        Intent changeDailyBeer = new Intent(this, MidnightChangerReceiver.class);
        PendingIntent changeDailyPending = PendingIntent.getBroadcast(
                this,1,changeDailyBeer,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,changeDailyPending);
    }
}
