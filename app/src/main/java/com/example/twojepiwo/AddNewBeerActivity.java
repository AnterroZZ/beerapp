package com.example.twojepiwo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.example.twojepiwo.LoginRegister.LoginActivity;
import com.example.twojepiwo.LoginRegister.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewBeerActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Spinner mName;
    private Spinner mAmount;


    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private User mUser;
    private String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_beer);
        setUpToolbar();

        mName = findViewById(R.id.add_beers_names);
        mAmount = findViewById(R.id.amount_drank);

    }

    private void setUpToolbar()
    {
        mToolbar = findViewById(R.id.add_new_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void addNewBeer(View view) {

        String className = mName.getSelectedItem().toString().toLowerCase();
        int amount = Integer.parseInt(mAmount.getSelectedItem().toString());
        boolean daily;

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES,MODE_PRIVATE);
        int totalBeers = sharedPreferences.getInt(LoginActivity.SHARED_TOTAL_BEERS,0) + 1;
        float progress = sharedPreferences.getFloat(LoginActivity.SHARED_PROGRESS,0);
        int todayBeers = sharedPreferences.getInt(LoginActivity.SHARED_TODAY_BEERS,0) + 1;
        int level = sharedPreferences.getInt(LoginActivity.SHARED_LEVEL,1);
        int choosenBeerAmount = sharedPreferences.getInt(className,0) + 1;
        String dailyBeer = sharedPreferences.getString(LoginActivity.SHARED_DAILY_BEER,"").toLowerCase();


        if(dailyBeer.equals(className))
        {
            daily = true;
            Log.d("SIEMA", "Jest to piwo twojego dnia!");
        }else daily = false;


        //TODO: Need to implement percantage of each individual beer
        progress = AddNewBeerMath.AccountLevelProgress(progress,amount,6,level,daily);
        if(progress>=1)
        {
            Log.d(ProfileActivity.TAG,"Progress is at about: " + progress);
            progress = progress - 1;
            level = level + 1;
        }
        Log.d(LoginActivity.TAG,"Progress is at about: " + progress);

        sharedPreferences.edit()
                .putInt(LoginActivity.SHARED_TOTAL_BEERS,totalBeers)
                .putFloat(LoginActivity.SHARED_PROGRESS,progress)
                .putInt(LoginActivity.SHARED_LEVEL,level)
                .putInt(LoginActivity.SHARED_TODAY_BEERS,todayBeers)
                .putInt(className,choosenBeerAmount)
                .apply();

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mUserID = mFirebaseUser.getUid();
        mReference = mDatabase.getReference().child("Users").child(mUserID);
        Log.d(LoginActivity.TAG,""+mReference);
        mReference.child("level").setValue(level);
        mReference.child("levelProgress").setValue(progress);
        mReference.child("totalBeers").setValue(totalBeers);
        mReference.child("todayBeers").setValue(todayBeers);
        mReference.child("beers").child(className).setValue(choosenBeerAmount);



        Intent intent = new Intent();
        intent.putExtra("message",1);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
