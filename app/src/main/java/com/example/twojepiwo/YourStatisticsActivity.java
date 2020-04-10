package com.example.twojepiwo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.twojepiwo.LoginRegister.LoginActivity;

import org.w3c.dom.Text;

public class YourStatisticsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mLevel;
    private TextView mFavouriteBeer;
    private TextView mCaloriesToday;
    private TextView mCaloriesTotal;
    private TextView mDrankToday;
    private TextView mDrankWeek;
    private TextView mDrankTotal;
    private TextView mPureAlcohol;
    private TextView mDateOfCreation;
    private TextView mFavouriteType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_statistics);
        setUpToolbar();

        mLevel = findViewById(R.id.stat_level_value);
        mFavouriteBeer = findViewById(R.id.stat_favourite_beer_value);
        mCaloriesToday = findViewById(R.id.stat_calories_today_value);
        mCaloriesTotal = findViewById(R.id.stat_calories_total_value);
        mDrankToday = findViewById(R.id.stat_drank_today_value);
        mDrankWeek = findViewById(R.id.stat_drank_week_value);
        mDrankTotal = findViewById(R.id.stat_drank_total_value);
        mPureAlcohol = findViewById(R.id.stat_pure_alcohol_value);
        mDateOfCreation = findViewById(R.id.stat_date_of_creation_value);
        mFavouriteType = findViewById(R.id.stat_favourite_type_value);

        //TODO: Uzupelnic reszte
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES,MODE_PRIVATE);
        int level = sharedPreferences.getInt(LoginActivity.SHARED_LEVEL,0);
        String favouriteBeer = sharedPreferences.getString(LoginActivity.SHARED_FAVOURITE_BEER,"");
        int caloriesToday = 0;
        int caloriesTotal = 0;
        int drankToday = sharedPreferences.getInt(LoginActivity.SHARED_TODAY_BEERS,0);
        int drankWeek = sharedPreferences.getInt(LoginActivity.SHARED_WEEK_BEERS,0);
        int drankTotal = sharedPreferences.getInt(LoginActivity.SHARED_TOTAL_BEERS,0);
        int pureAlcohol = 0;
        String dateOfCreation = sharedPreferences.getString(LoginActivity.SHARED_DATE,"");
        String subDate = dateOfCreation.substring(4,10).trim();
        String subDateYear = dateOfCreation.substring(dateOfCreation.length()-4);
        String dateFinal = subDate + " " + subDateYear;
        String favouriteType = "";

        mLevel.setText(String.valueOf(level));
        mFavouriteBeer.setText(favouriteBeer);
        mCaloriesToday.setText(String.valueOf(caloriesToday));
        mCaloriesTotal.setText(String.valueOf(caloriesTotal));
        mDrankToday.setText(String.valueOf(drankToday));
        mDrankWeek.setText(String.valueOf(drankWeek));
        mDrankTotal.setText(String.valueOf(drankTotal));
        mPureAlcohol.setText(String.valueOf(pureAlcohol));
        mDateOfCreation.setText(dateFinal);
        mFavouriteType.setText(favouriteType);
    }

    private void setUpToolbar()
    {
        mToolbar = findViewById(R.id.stat_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
}
