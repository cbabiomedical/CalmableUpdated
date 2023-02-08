package com.example.calmable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.crrepa.ble.CRPBleClient;
import com.example.calmable.device.DeviceActivity;
import com.example.calmable.sample.JsonPlaceHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RateUsActivity extends AppCompatActivity {

    Button ratingBtn;
    RatingBar ratingStars;
    float myRating = 0;
    Double sum;
    Double average;
    CRPBleClient mBleClient;
    FirebaseUser mUser;
    int a;
    JsonPlaceHolder jsonPlaceHolder;
    private static final String TAG = "MusicPlayer";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        ratingBtn = findViewById(R.id.ratingBtn);
        ratingStars = findViewById(R.id.ratingBar);
        DeviceActivity deviceActivity=new DeviceActivity();
//        Log.d("ChckResponses", String.valueOf(deviceActivity.getMusicRelaxation_index()));
//        for(int i=0; i<deviceActivity.getMusicRelaxation_index().size();i++){
//            sum+=(Double)deviceActivity.getMusicRelaxation_index().get(i);
////        }
//        average=sum/deviceActivity.getMusicRelaxation_index().size();
//        Log.d("SUM", String.valueOf(sum));

        Log.d("Average", String.valueOf(average));
        // Getting count of no.of plays
        SharedPreferences sh = getSharedPreferences("prefsMusic", MODE_APPEND);
        a = sh.getInt("firstStartMusic", 0);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Gson gson = new GsonBuilder().setLenient().create();
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
        Log.d("Month", String.valueOf(now.get(Calendar.MONTH)));

        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH) + 1;
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        mUser = FirebaseAuth.getInstance().getCurrentUser();

//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//        reference.setValue(average);


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
                Toast.makeText(RateUsActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("rating before click---------", String.valueOf(myRating));

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RateUsActivity.this, String.valueOf(myRating), Toast.LENGTH_SHORT).show();
            }
        });

    }
}