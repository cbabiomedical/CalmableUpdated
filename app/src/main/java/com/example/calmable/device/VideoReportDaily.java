package com.example.calmable.device;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.calmable.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import a.b.a.C;

public class VideoReportDaily extends AppCompatActivity {

    Double sum;
    Double average;
    int x;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_report_daily);
        mUser= FirebaseAuth.getInstance().getCurrentUser();

        DeviceActivity deviceActivity = new DeviceActivity();

//        Log.d("CHECKRESPO", String.valueOf(DeviceActivity.videoRelaxation));
//        Log.d("VideoRelaxation", String.valueOf(deviceActivity.getMusicRelaxation_index()));

//        for (int i = 0; i < DeviceActivity.videoRelaxation.size(); i++) {
//            sum += (Double) DeviceActivity.videoRelaxation.get(i);
//        }
//        Log.d("VideoSum", String.valueOf(sum));
//        average = sum / DeviceActivity.videoRelaxation.size();

        SharedPreferences prefsTimeMem = getSharedPreferences("prefsVideo", MODE_PRIVATE);
        int firstStartTimeMem = prefsTimeMem.getInt("firstStartVideo", 0);

        SharedPreferences sh = getSharedPreferences("prefsVideo", MODE_APPEND);
        x = sh.getInt("firstStartVideo", 0);
        Log.d("A Count", String.valueOf(x));
        int y = x + 1;

        SharedPreferences prefsCount1 = getSharedPreferences("prefsVideo", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsCount1.edit();
        editor.putInt("firstStartVideo", y);
        editor.apply();
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

//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("ReportWW").child("VideoIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
//        reference.setValue(average);


    }
}