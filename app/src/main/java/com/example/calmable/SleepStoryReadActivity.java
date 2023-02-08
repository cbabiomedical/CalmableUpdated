package com.example.calmable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SleepStoryReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_story_read);
    }

    public void btnGoStoryAudio (View view){
        startActivity(new Intent(getApplicationContext(), SleepStoryAudioActivity.class));
    }

    //Sleep story categories
    public void SleepStory1btn (View view){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(getApplicationContext(), SleepStory1.class));
    }
    public void SleepStory2btn (View view){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(getApplicationContext(), SleepStory2.class));
    }
    public void SleepStory3btn (View view){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(getApplicationContext(), SleepStory3.class));
    }
    public void SleepStory4btn (View view){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(getApplicationContext(), SleepStory4.class));
    }
    public void SleepStory5btn (View view){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(getApplicationContext(), SleepStory5.class));
    }
    public void SleepStory6btn (View view){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(getApplicationContext(), SleepStory6.class));
    }
    public void SleepStory7btn (View view){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(getApplicationContext(), SleepStory7.class));
    }
    public void SleepStory8btn (View view){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(getApplicationContext(), SleepStory8.class));
    }
    public void SleepStory9btn (View view){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(getApplicationContext(), SleepStory9.class));
    }

    public void SleepStory10btn(View view) {
        startActivity(new Intent(getApplicationContext(), SleepStory10.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void SleepStory11btn(View view) {
        startActivity(new Intent(getApplicationContext(), SleepStory11.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }


    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), SleepyHome.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}