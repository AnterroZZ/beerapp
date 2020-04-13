package com.example.twojepiwo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    Context context;

    EditText oldPassword;
    EditText newPassword;
    EditText newPasswordConfirm;

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        oldPassword = view.findViewById(R.id.change_password_old);
        newPassword = view.findViewById(R.id.change_password_new);
        newPasswordConfirm = view.findViewById(R.id.change_password_new_confirm);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if(positiveResult)
        {

            mDatabase = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            context = getContext();

            //TODO: Change password
            final String email = mUser.getEmail();
            final String oldPass = oldPassword.getText().toString();
            final String newPass = newPassword.getText().toString();
            final String newPassConfirm = newPasswordConfirm.getText().toString();
            AuthCredential credential = EmailAuthProvider.getCredential(email,oldPass);

            if(newPasswordValid(newPass, newPassConfirm, context)){
                try {
                    mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                        }else{
                                            mReference = mDatabase.getReference().child("Users").child(mUser.getUid());
                                            mReference.child("password").setValue(newPass);
                                            Toast.makeText(context,"Hasło zostało zmienione", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }else{
                                Toast.makeText(context,"Złe stare hasło!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getContext(),"Coś poszło nie tak", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    private boolean newPasswordValid(String newPass, String newPassConfirm, Context context) {
        if(newPass.length()>=8)
        {
            if(containsNumbers(newPass))
            {
                if(newPass.equals(newPassConfirm))
                {
                    return true;
                }else {
                    Toast.makeText(context,getResources().getString(R.string.reg_toast_matches),Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(context,getResources().getString(R.string.reg_toast_number),Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(context,getResources().getString(R.string.reg_toast_length),Toast.LENGTH_SHORT).show();
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

    public static ChangePasswordDialogFragmentCompat newInstance(String key)
    {
        final ChangePasswordDialogFragmentCompat fragment
                = new ChangePasswordDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY,key);
        fragment.setArguments(b);

        return fragment;
    }
}
