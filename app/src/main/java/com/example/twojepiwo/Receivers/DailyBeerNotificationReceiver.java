package com.example.twojepiwo.Receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.twojepiwo.Beers.BeersUtils;
import com.example.twojepiwo.LoginRegister.LoginActivity;
import com.example.twojepiwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DailyBeerNotificationReceiver extends BroadcastReceiver {

    public static String DAILY_NOTIFICATION_ID = "dailyBeers";
    public static String DEFAULT_DAILY_NOTIFICATION_CHANNEL = "daily";
    public static String DAILY_NOTIFICATION_CHANNEL_ID = "idDaily";
    public static String NOTIFICATION = "notificationDaily";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    private String mUserID;

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        String dailyBeer = sharedPreferences.getString(LoginActivity.SHARED_DAILY_BEER,"");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DailyBeerNotificationReceiver.DEFAULT_DAILY_NOTIFICATION_CHANNEL)
                .setContentTitle(context.getString(R.string.daily_notification_title) + " " + dailyBeer + "!")
                .setContentText(context.getString(R.string.daily_notification_body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setChannelId(DailyBeerNotificationReceiver.DAILY_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_beer_daily);
        Notification notification = builder.build();

        NotificationManager notificationManger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(DAILY_NOTIFICATION_CHANNEL_ID,"DAILY_BEER",importance);
            notificationManger.createNotificationChannel(notificationChannel);
        }

        int id = intent.getIntExtra(DAILY_NOTIFICATION_ID,0);
        notificationManger.notify(id,notification);

    }
}
