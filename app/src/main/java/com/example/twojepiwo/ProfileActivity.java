package com.example.twojepiwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twojepiwo.LoginRegister.LoginActivity;


public class ProfileActivity extends AppCompatActivity implements ProfileBeersAdapter.ItemClickListener {

    //Declaring variables
    private String mUserID;
    private Toolbar mToolbar;
    private int mFollowersNumber;
    private int mFollowingNumber;
    private TextView mName;
    private TextView mTitle;
    private TextView mTotal;
    private TextView mLevel;
    public final static String TAG = "TAG";
    private SharedPreferences sharedPreferences;

    private RecyclerView mRecyclerView;
    private ProfileBeersAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mRecyclerView = findViewById(R.id.profile_rv_beers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProfileBeersAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);
        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES,MODE_PRIVATE);
        //TODO: Remove this and replace it with getting numbers from database
        mFollowersNumber = 0;
        mFollowingNumber = 0;

        //Getting basic values from the database
        setUpToolbar();
        getFollowersAndFollowing();
        getNameAndTitle();
        getBaseStats();

    }




    public void seeAllStatistics(View view) {
        Intent statIntent = new Intent(this,YourStatisticsActivity.class);
        startActivity(statIntent);
    }


    //Setting up profile toolbar
    private void setUpToolbar()
    {
        mToolbar = findViewById(R.id.profile_toolbar);
        mToolbar.inflateMenu(R.menu.profile_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.action_settings:
                        Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(settingsIntent);
                        return true;
                }
                return false;
            }
        });
    }

    private void getFollowersAndFollowing()
    {
        TextView mFollowers = findViewById(R.id.profile_tv_followers_number);
        TextView mFollowing = findViewById(R.id.profile_tv_following_number);

        mFollowers.setText(String.valueOf(mFollowersNumber));
        mFollowing.setText(String.valueOf(mFollowingNumber));

    }

    private void getNameAndTitle()
    {
        String name = sharedPreferences.getString(LoginActivity.SHARED_USERNAME,"Could not load xd");
        String title = sharedPreferences.getString(LoginActivity.SHARED_TITLE,"");

        mName = findViewById(R.id.profile_tv_name);
        mTitle = findViewById(R.id.profile_tv_title);

        mName.setText(name);
        if(title.equals("New Comer"))
        {
            mTitle.setText(getResources().getString(R.string.profile_title_default));
        }else mTitle.setText(title);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getBaseStats();
    }

    public void getBaseStats()
    {
        int totalBeers = sharedPreferences.getInt(LoginActivity.SHARED_TOTAL_BEERS,0);
        int level = sharedPreferences.getInt(LoginActivity.SHARED_LEVEL,1);
        mTotal = findViewById(R.id.profile_tv_total_number_beers);
        mLevel = findViewById(R.id.profile_tv_level);

        mTotal.setText(String.valueOf(totalBeers));
        mLevel.setText(String.valueOf(level));

    }

    public void goToFollowing(View view) {
        Toast.makeText(this, "Following", Toast.LENGTH_SHORT).show();
    }

    public void goToFollowers(View view) {
        Toast.makeText(this, "Followers", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClickListener(int itemId) {
        Toast.makeText(this,"To umozliwi zobaczenie statystyk piwa",Toast.LENGTH_SHORT).show();
//        Intent seeBeerProgress = new Intent(this,);
//        startActivity(seeBeerProgress);
    }

    public void goToMain(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }
}
