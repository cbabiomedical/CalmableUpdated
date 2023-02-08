package com.example.calmable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SleepStory9 extends AppCompatActivity {
    Button ratingbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_story9);

        ratingbtn = findViewById(R.id.ratingBtn);


        // On click listener of rate button
        ratingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SleepRate.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    //to change the light theme to dark theme
    public void btnTheme (View view){
        startActivity(new Intent(getApplicationContext(), Sleep9DarkTheme.class));
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), SleepStoryReadActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
