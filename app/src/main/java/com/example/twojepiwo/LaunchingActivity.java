package com.example.twojepiwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.twojepiwo.LoginRegister.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LaunchingActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        Intent launchingIntent;

        if(isLoggedIn())
        {
            launchingIntent = new Intent(LaunchingActivity.this, ProfileActivity.class);
            startActivity(launchingIntent);
            finish();
        }else
        {
            launchingIntent = new Intent(LaunchingActivity.this, LoginActivity.class);
            startActivity(launchingIntent);
            finish();
        }
    }

    private boolean isLoggedIn()
    {
        if(mAuth.getCurrentUser() != null)
        {
            return true;
        } else return false;
    }
}
