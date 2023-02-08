package com.example.calmable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SleepyHome extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepy_home);

    }

    public void btnGoSleepyStories (View view) {
        startActivity(new Intent(getApplicationContext(), SleepStoryAudioActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnGoMeditation (View view) {
        startActivity(new Intent(getApplicationContext(), MeditateMusicActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnGoSoundScape (View view) {

        startActivity(new Intent(getApplicationContext(), SoundScapeMusicActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}