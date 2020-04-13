package com.example.twojepiwo;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;

public class ChangePasswordPreference extends DialogPreference {

    private int mDialogLayoutResId = R.layout.change_password_preference;

    public ChangePasswordPreference(Context context) {
        super(context, null);
    }

    public ChangePasswordPreference(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public ChangePasswordPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, defStyleAttr);
    }

    public ChangePasswordPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }








    @Override
    public int getDialogLayoutResource() {
        return mDialogLayoutResId;
    }
}
