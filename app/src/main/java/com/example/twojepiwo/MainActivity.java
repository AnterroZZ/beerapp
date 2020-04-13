package com.example.twojepiwo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twojepiwo.LoginRegister.LoginActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //Declarations
    TextView mAmountDrankToday;
    TextView mAmountDrankNow;
    TextView mBeerOfDay;
    SharedPreferences sharedPreferences;

    public static final int ADD_BEER_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Initializations
        mAmountDrankToday = findViewById(R.id.main_tv_amount_drank_today);
        mAmountDrankNow = findViewById(R.id.main_tv_amount_drank_now);
        mBeerOfDay = findViewById(R.id.main_tv_beer_day_value);
        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES,MODE_PRIVATE);
        int drankToday = sharedPreferences.getInt(LoginActivity.SHARED_TODAY_BEERS,0);
//        Mock data
        String dailyBeer = sharedPreferences.getString(LoginActivity.SHARED_DAILY_BEER,"");
        mBeerOfDay.setText(dailyBeer);
        mAmountDrankNow.setText("0");
        mAmountDrankToday.setText(String.valueOf(drankToday));


    }



    public void addNewDrankBeer(View view) {
        Intent intent = new Intent(this,AddNewBeerActivity.class);
        startActivityForResult(intent,ADD_BEER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_BEER_REQUEST) {
            sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES,MODE_PRIVATE);
                int drankToday = sharedPreferences.getInt(LoginActivity.SHARED_TODAY_BEERS,MODE_PRIVATE);
                Log.d("MOTHERFUCKER",""+drankToday);
                mAmountDrankToday.setText(String.valueOf(drankToday));
            }
    }

    public void goToProfile(View view) {
        Intent profleIntent = new Intent(this, ProfileActivity.class);
        startActivity(profleIntent);
    }
}
