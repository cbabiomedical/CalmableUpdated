package com.example.calmable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class HappyHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_home);

    }

    public void btnGoHealingMusic(View view) {
        startActivity(new Intent(getApplicationContext(), HealingMusicActivity.class));
    }

    public void btnGoLoveMusic(View view) {
        startActivity(new Intent(getApplicationContext() , LoveMusicActivity.class));
    }

    public void btnGoInstrumentalMusic(View view) {
        startActivity(new Intent(getApplicationContext(), InstrumentalMusicActivity.class));
    }
}