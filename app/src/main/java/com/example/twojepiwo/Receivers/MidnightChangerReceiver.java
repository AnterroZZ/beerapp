package com.example.twojepiwo.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.twojepiwo.Beers.BeersUtils;
import com.example.twojepiwo.LoginRegister.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MidnightChangerReceiver extends BroadcastReceiver {


    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    private String mUserID;

    private boolean sameBeer;
    private String newDailyBeer;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserID = mUser.getUid();

        do {
            newDailyBeer = BeersUtils.chooseNextDailyBeer();
            if(newDailyBeer.equals(sharedPreferences.getString(LoginActivity.SHARED_DAILY_BEER,"")))
            {
                sameBeer = true;
            }else sameBeer = false;

        }while (sameBeer);

        Toast.makeText(context,newDailyBeer,Toast.LENGTH_SHORT).show();

        sharedPreferences.edit()
                .putString(LoginActivity.SHARED_DAILY_BEER,newDailyBeer)
                .putString(LoginActivity.SHARED_TODAY_BEERS,newDailyBeer)
                .apply();
        mReference = mDatabase.getReference().child("Users").child(mUserID);
        mReference.child("dailyBeer").setValue(newDailyBeer);
        mReference.child("todayBeers").setValue(0);
    }
}
