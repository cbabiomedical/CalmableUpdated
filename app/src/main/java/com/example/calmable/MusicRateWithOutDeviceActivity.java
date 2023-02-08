package com.example.calmable;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MusicRateWithOutDeviceActivity extends AppCompatActivity {

    Button ratingBtn, backBtn2;
    RatingBar ratingStars;
    float myRating = 0;
    public static float totalRating;
    int a;

    FirebaseUser mUser;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_rate);

        ratingBtn = findViewById(R.id.ratingBtn);
        backBtn2 = findViewById(R.id.backBtn2);
        ratingStars = findViewById(R.id.ratingBar);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating = (int) v;
                String message = null;

                myRating = ratingBar.getRating();

                switch (rating) {
                    case 1:
                        message = "Sorry to hear that!";
                        break;
                    case 2:
                        message = "We always accept suggestions";
                        break;
                    case 3:
                        message = "Good";
                        break;
                    case 4:
                        message = "Great! Thank you";
                        break;
                    case 5:
                        message = "Awesome!";
                        break;
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("rating before click---------", String.valueOf(myRating));




        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK",String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH",String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR",String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY",String.valueOf(now.get(Calendar.DAY_OF_MONTH)));

        int month=now.get(Calendar.MONTH)+1;
        int day=now.get(Calendar.DAY_OF_MONTH)+1;
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
//prints day name
        System.out.println("Day Name: "+str);
        Log.d("Day Name",str);


        SharedPreferences sh = getSharedPreferences("prefsCount", MODE_APPEND);
        a = sh.getInt("firstStartCount", 0);
        Log.d("---------------a-------------", String.valueOf(a));

        int b = a + 1;

        SharedPreferences prefsCount1 = getSharedPreferences("prefsCount", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsCount1.edit();
        editor.putInt("firstStartCount", b);
        editor.apply();
        SharedPreferences sha = getSharedPreferences("prefsCount", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int a1 = sha.getInt("firstStartCount", 0);

        Log.d("A Count2", String.valueOf(a1));



        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),String.valueOf(myRating), Toast.LENGTH_SHORT).show();
                totalRating = totalRating + myRating;

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));

                reference.setValue(myRating);

                Intent intent = new Intent(getApplicationContext(), MusicReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });



        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalRating = totalRating + myRating;

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
                reference.setValue(myRating);


                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    // for go back
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}