package com.example.twojepiwo.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twojepiwo.Beers.BeersUtils;
import com.example.twojepiwo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView mEmail;
    private TextView mPassword;
    private TextView mUsername;
    private TextView mAge;
    private TextView mHeight;
    private TextView mWeight;
    private Spinner mSex;
    private boolean InformationMatch;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mDatabaseUser;
    private DatabaseReference mReference;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        setUpTextViews();

    }

    public void goToLogin(View view) {
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
    }

    private void setUpTextViews()
    {
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mUsername = findViewById(R.id.reg_username);
        mAge = findViewById(R.id.reg_age);
        mHeight = findViewById(R.id.reg_height);
        mWeight = findViewById(R.id.reg_weight);
        mSex = findViewById(R.id.reg_sex);
    }

    public void Register(View view) {

        if(areRequirementsMatched())
        {
            createUser();
            mAuth.createUserWithEmailAndPassword(mUser.getEmailAddress(),mUser.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                mDatabaseUser = mAuth.getCurrentUser();
                                mReference.child("Users").child(mDatabaseUser.getUid()).setValue(mUser)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mReference.child("Users").child(mDatabaseUser.getUid()).child("dailyBeer").setValue(BeersUtils.chooseNextDailyBeer());
                                                if(task.isSuccessful())
                                                {
                                                    //TODO: To remove later on
                                                    Toast.makeText(getApplicationContext(),"Zarejestrowany!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        });
                            }else Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_toast_wrong_email), Toast.LENGTH_SHORT).show();;
                        }
                    });

        }
    }

    private void createUser() {
        if(mSex.getSelectedItemPosition()==0) {
            mUser = new User(mUsername.getText().toString().trim(),mPassword.getText().toString().trim(),
                    mEmail.getText().toString().trim(),Integer.parseInt(mWeight.getText().toString().trim()),
                    Integer.parseInt(mHeight.getText().toString().trim()),Integer.parseInt(mAge.getText().toString().trim()), "M");
        }else{
            mUser = new User(mUsername.getText().toString().trim(),mPassword.getText().toString().trim(),
                    mEmail.getText().toString().trim(),Integer.parseInt(mWeight.getText().toString().trim()),
                    Integer.parseInt(mHeight.getText().toString().trim()),Integer.parseInt(mAge.getText().toString().trim()), "F");

        }
    }

    //    Checks if the password is matching the required needs
    private boolean passwords()
    {
        TextView password2 = findViewById(R.id.reg_password_confirm);
        String passwordConfirm = password2.getText().toString();
        String password = mPassword.getText().toString();

        if(password.length()>=8)
        {
            if(containsNumbers(password))
            {
                if(password.equals(passwordConfirm))
                {
                    return true;
                }else {
                    Toast.makeText(this,getResources().getString(R.string.reg_toast_matches),Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(this,getResources().getString(R.string.reg_toast_number),Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(this,getResources().getString(R.string.reg_toast_length),Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    private boolean containsNumbers(String s)
    {
        if(s != null && !s.isEmpty())
        {
            for(char c : s.toCharArray())
            {
                if(Character.isDigit(c))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean areRequirementsMatched()
    {
        if(passwords())
        {
            
            return true;
        }
        return false;
    }
}
