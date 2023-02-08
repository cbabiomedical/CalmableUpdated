package com.example.calmable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calmable.device.DeviceActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class MusicReportMonthly extends AppCompatActivity {


    private Context context;
    String text;
    GifImageView c1gif, c2gif;
    int color;
    View c1, c2;
    AppCompatButton daily, weekly, yearly;
    FirebaseUser mUser;
    ImageView concentrationBtn, relaxationBtn;
    TextView tvDate;

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Long average1, average2, average3, average4, average5, average6, average7, average8, average9, average10, average11, average12;
    Double average111, average112, average13, average14, average15, average16, average17, average18, average19, average20, average21, average22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_report_monthly);

        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        yearly = findViewById(R.id.yearly);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        lineChart = findViewById(R.id.lineChartMonthly);

        tvDate = (TextView) findViewById(R.id.tvDate);

        Date realDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = sdf.format(realDate);
        tvDate.setText("Until " + date);

        mUser = FirebaseAuth.getInstance().getCurrentUser();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        NavigationBar();
        ////////////////////////////////////////////////////////////

        getEntries();


        //Initializing arraylist and storing input data to arraylist
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage


        final Handler handler = new Handler();
        final int delay = 7000;

        //Initializing arraylist and storing input data to arraylist
//
        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MusicReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MusicReportWeekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of yearly button
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MusicReportYearly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


    }

    private void getEntries() {
        Handler handler = new Handler();
        final int delay = 5000;


        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        int month = now.get(Calendar.MONTH) + 1;


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                lineEntries = new ArrayList();
                if (DeviceActivity.connected) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                            .child(String.valueOf(1));

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList sumElement = new ArrayList();
                            Double sum = (0.0);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                        Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                        sumElement.add(snapshot2.getValue());
                                        sum += av1;
                                    }
                                }
                            }
                            Log.d("Monthly Array", String.valueOf(sumElement));
                            Log.d("SUM", String.valueOf(sum));
                            if (sum != 0) {
                                average111 = sum / sumElement.size();
                            } else {
                                average111 = 0.0;
                            }

                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                    .child(String.valueOf(2));

                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList sumElement = new ArrayList();
                                    Double sum = (0.0);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                sumElement.add(snapshot2.getValue());
                                                sum += av1;
                                            }
                                        }
                                    }
                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                    Log.d("SUM", String.valueOf(sum));
                                    if (sum != 0) {
                                        average112 = sum / sumElement.size();
                                    } else {
                                        average112 = 0.0;
                                    }

                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                            .child(String.valueOf(3));
                                    reference2.addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ArrayList sumElement = new ArrayList();
                                            Double sum = (0.0);
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                        Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                        sumElement.add(snapshot2.getValue());
                                                        sum += av1;
                                                    }
                                                }
                                            }
                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                            Log.d("SUM", String.valueOf(sum));
                                            if (sum != 0) {
                                                average13 = sum / sumElement.size();
                                            } else {
                                                average13 = 0.0;
                                            }

                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                    .child(String.valueOf(4));

                                            reference2.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    ArrayList sumElement = new ArrayList();
                                                    Double sum = (0.0);
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                                sumElement.add(snapshot2.getValue());
                                                                sum += av1;
                                                            }
                                                        }
                                                    }
                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                    Log.d("SUM", String.valueOf(sum));
                                                    if (sum != 0) {
                                                        average14 = sum / sumElement.size();
                                                    } else {
                                                        average14 = 0.0;
                                                    }
                                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                            .child(String.valueOf(5));

                                                    reference2.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            ArrayList sumElement = new ArrayList();
                                                            Double sum = (0.0);
                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                        Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                                        sumElement.add(snapshot2.getValue());
                                                                        sum += av1;
                                                                    }
                                                                }
                                                            }
                                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                                            Log.d("SUM", String.valueOf(sum));
                                                            if (sum != 0) {
                                                                average15 = sum / sumElement.size();
                                                            } else {
                                                                average15 = 0.0;
                                                            }

                                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                    .child(String.valueOf(6));

                                                            reference2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    ArrayList sumElement = new ArrayList();
                                                                    Double sum = (0.0);
                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                                                sumElement.add(snapshot2.getValue());
                                                                                sum += av1;
                                                                            }
                                                                        }
                                                                    }
                                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                                    Log.d("SUM", String.valueOf(sum));
                                                                    if (sum != 0) {
                                                                        average16 = sum / sumElement.size();
                                                                    } else {
                                                                        average16 = 0.0;
                                                                    }
                                                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                            .child(String.valueOf(7));

                                                                    reference2.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            ArrayList sumElement = new ArrayList();
                                                                            Double sum = (0.0);
                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                        Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                                                        sumElement.add(snapshot2.getValue());
                                                                                        sum += av1;
                                                                                    }
                                                                                }
                                                                            }
                                                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                                                            Log.d("SUM", String.valueOf(sum));
                                                                            if (sum != 0) {
                                                                                average17 = sum / sumElement.size();
                                                                            } else {
                                                                                average17 = 0.0;
                                                                            }
                                                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                    .child(String.valueOf(8));

                                                                            reference2.addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    ArrayList sumElement = new ArrayList();
                                                                                    Double sum = (0.0);
                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                                                                sumElement.add(snapshot2.getValue());
                                                                                                sum += av1;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                    Log.d("SUM", String.valueOf(sum));
                                                                                    if (sum != 0) {
                                                                                        average18 = sum / sumElement.size();
                                                                                    } else {
                                                                                        average18 = 0.0;
                                                                                    }
                                                                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                            .child(String.valueOf(9));

                                                                                    reference2.addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            ArrayList sumElement = new ArrayList();
                                                                                            Double sum = (0.0);
                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                        Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                                                                        sumElement.add(snapshot2.getValue());
                                                                                                        sum += av1;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                            Log.d("SUM", String.valueOf(sum));
                                                                                            if (sum != 0) {
                                                                                                average19 = sum / sumElement.size();
                                                                                            } else {
                                                                                                average19 = 0.0;
                                                                                            }
                                                                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                    .child(String.valueOf(10));

                                                                                            reference2.addValueEventListener(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                    Double sum = (0.0);
                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                                Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                                                                                sumElement.add(snapshot2.getValue());
                                                                                                                sum += av1;
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                    Log.d("SUM", String.valueOf(sum));
                                                                                                    if (sum != 0) {
                                                                                                        average20 = sum / sumElement.size();
                                                                                                    } else {
                                                                                                        average20 = 0.0;
                                                                                                    }
                                                                                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                            .child(String.valueOf(11));

                                                                                                    reference2.addValueEventListener(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                            Double sum = (0.0);
                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                                        Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                                                                                        sumElement.add(snapshot2.getValue());
                                                                                                                        sum += av1;
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                            Log.d("SUM", String.valueOf(sum));
                                                                                                            if (sum != 0) {
                                                                                                                average21 = sum / sumElement.size();
                                                                                                            } else {
                                                                                                                average21 = 0.0;
                                                                                                            }
                                                                                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                                    .child(String.valueOf(12));

                                                                                                            reference2.addValueEventListener(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                    Double sum = (0.0);
                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                                                Double av1 = Double.parseDouble(String.valueOf(snapshot2.getValue()));
                                                                                                                                sumElement.add(snapshot2.getValue());
                                                                                                                                sum += av1;
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                                    Log.d("SUM", String.valueOf(sum));
                                                                                                                    if (sum != 0) {
                                                                                                                        average22 = sum / sumElement.size();
                                                                                                                    } else {
                                                                                                                        average22 = 0.0;
                                                                                                                    }
                                                                                                                    Log.d("Average Jan", String.valueOf(average1));
                                                                                                                    Log.d("Average Feb", String.valueOf(average2));
                                                                                                                    Log.d("Average Mar", String.valueOf(average3));
                                                                                                                    Log.d("Average Apr", String.valueOf(average4));
                                                                                                                    Log.d("Average May", String.valueOf(average5));
                                                                                                                    Log.d("Average Jun", String.valueOf(average6));
                                                                                                                    Log.d("Average Jul", String.valueOf(average7));
                                                                                                                    Log.d("Average Aug", String.valueOf(average8));
                                                                                                                    Log.d("Average Sep", String.valueOf(average9));
                                                                                                                    Log.d("Average Oct", String.valueOf(average10));
                                                                                                                    Log.d("Average Nov", String.valueOf(average11));
                                                                                                                    Log.d("Average Dec", String.valueOf(average12));

                                                                                                                    lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(average111))));
                                                                                                                    lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(average112))));
                                                                                                                    lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(average13))));
                                                                                                                    lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(average14))));
                                                                                                                    lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(average15))));
                                                                                                                    lineEntries.add(new Entry(6, Float.parseFloat(String.valueOf(average16))));
                                                                                                                    lineEntries.add(new Entry(7, Float.parseFloat(String.valueOf(average17))));
                                                                                                                    lineEntries.add(new Entry(8, Float.parseFloat(String.valueOf(average18))));
                                                                                                                    lineEntries.add(new Entry(9, Float.parseFloat(String.valueOf(average19))));
                                                                                                                    lineEntries.add(new Entry(10, Float.parseFloat(String.valueOf(average20))));
                                                                                                                    lineEntries.add(new Entry(11, Float.parseFloat(String.valueOf(average21))));
                                                                                                                    lineEntries.add(new Entry(12, Float.parseFloat(String.valueOf(average22))));

                                                                                                                    lineDataSet = new LineDataSet(lineEntries, "Relax Progress");
                                                                                                                    lineData = new LineData(lineDataSet);
                                                                                                                    lineChart.setData(lineData);

                                                                                                                    lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                                                                                                    lineDataSet.setValueTextColor(Color.WHITE);
                                                                                                                    lineDataSet.setValueTextSize(10f);

                                                                                                                    lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                                                                                    lineChart.setBorderColor(Color.TRANSPARENT);
                                                                                                                    lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                                                                                    lineChart.getAxisLeft().setDrawGridLines(false);
                                                                                                                    lineChart.getXAxis().setDrawGridLines(false);
                                                                                                                    lineChart.getAxisRight().setDrawGridLines(false);
                                                                                                                    lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
                                                                                                                    lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                                                                                    lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
                                                                                                                    lineChart.getDescription().setTextColor(R.color.white);
                                                                                                                    lineChart.invalidate();
                                                                                                                    lineChart.refreshDrawableState();


                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                }
                                                                                                            });
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                        }
                                                                                                    });

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                }
                                                                                            });
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                            .child(String.valueOf(1));

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList sumElement = new ArrayList();
                            int sum = (0);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                        Long av1 = (Long) snapshot2.getValue();
                                        sumElement.add(snapshot2.getValue());
                                        sum += av1;
                                    }
                                }
                            }
                            Log.d("Monthly Array", String.valueOf(sumElement));
                            Log.d("SUM", String.valueOf(sum));
                            if (sum != 0) {
                                average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                            } else {
                                average1 = 0L;
                            }

                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                    .child(String.valueOf(2));

                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList sumElement = new ArrayList();
                                    int sum = (0);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                Long av1 = (Long) snapshot2.getValue();
                                                sumElement.add(snapshot2.getValue());
                                                sum += av1;
                                            }
                                        }
                                    }
                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                    Log.d("SUM", String.valueOf(sum));
                                    if (sum != 0) {
                                        average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                    } else {
                                        average2 = 0L;
                                    }

                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                            .child(String.valueOf(3));

                                    reference2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ArrayList sumElement = new ArrayList();
                                            int sum = (0);
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                        Long av1 = (Long) snapshot2.getValue();
                                                        sumElement.add(snapshot2.getValue());
                                                        sum += av1;
                                                    }
                                                }
                                            }
                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                            Log.d("SUM", String.valueOf(sum));
                                            if (sum != 0) {
                                                average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                            } else {
                                                average3 = 0L;
                                            }

                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                    .child(String.valueOf(4));

                                            reference2.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    ArrayList sumElement = new ArrayList();
                                                    int sum = (0);
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                Long av1 = (Long) snapshot2.getValue();
                                                                sumElement.add(snapshot2.getValue());
                                                                sum += av1;
                                                            }
                                                        }
                                                    }
                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                    Log.d("SUM", String.valueOf(sum));
                                                    if (sum != 0) {
                                                        average4 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                    } else {
                                                        average4 = 0L;
                                                    }
                                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                            .child(String.valueOf(5));

                                                    reference2.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            ArrayList sumElement = new ArrayList();
                                                            int sum = (0);
                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                        Long av1 = (Long) snapshot2.getValue();
                                                                        sumElement.add(snapshot2.getValue());
                                                                        sum += av1;
                                                                    }
                                                                }
                                                            }
                                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                                            Log.d("SUM", String.valueOf(sum));
                                                            if (sum != 0) {
                                                                average5 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                            } else {
                                                                average5 = 0L;
                                                            }

                                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                    .child(String.valueOf(6));

                                                            reference2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    ArrayList sumElement = new ArrayList();
                                                                    int sum = (0);
                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                Long av1 = (Long) snapshot2.getValue();
                                                                                sumElement.add(snapshot2.getValue());
                                                                                sum += av1;
                                                                            }
                                                                        }
                                                                    }
                                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                                    Log.d("SUM", String.valueOf(sum));
                                                                    if (sum != 0) {
                                                                        average6 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                    } else {
                                                                        average6 = 0L;
                                                                    }
                                                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                            .child(String.valueOf(7));

                                                                    reference2.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            ArrayList sumElement = new ArrayList();
                                                                            int sum = (0);
                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                        Long av1 = (Long) snapshot2.getValue();
                                                                                        sumElement.add(snapshot2.getValue());
                                                                                        sum += av1;
                                                                                    }
                                                                                }
                                                                            }
                                                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                                                            Log.d("SUM", String.valueOf(sum));
                                                                            if (sum != 0) {
                                                                                average7 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                            } else {
                                                                                average7 = 0L;
                                                                            }
                                                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                    .child(String.valueOf(8));

                                                                            reference2.addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    ArrayList sumElement = new ArrayList();
                                                                                    int sum = (0);
                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                Long av1 = (Long) snapshot2.getValue();
                                                                                                sumElement.add(snapshot2.getValue());
                                                                                                sum += av1;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                    Log.d("SUM", String.valueOf(sum));
                                                                                    if (sum != 0) {
                                                                                        average8 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                    } else {
                                                                                        average8 = 0L;
                                                                                    }
                                                                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                            .child(String.valueOf(9));

                                                                                    reference2.addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            ArrayList sumElement = new ArrayList();
                                                                                            int sum = (0);
                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                        Long av1 = (Long) snapshot2.getValue();
                                                                                                        sumElement.add(snapshot2.getValue());
                                                                                                        sum += av1;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                            Log.d("SUM", String.valueOf(sum));
                                                                                            if (sum != 0) {
                                                                                                average9 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                            } else {
                                                                                                average9 = 0L;
                                                                                            }
                                                                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                    .child(String.valueOf(10));

                                                                                            reference2.addValueEventListener(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                    int sum = (0);
                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                                Long av1 = (Long) snapshot2.getValue();
                                                                                                                sumElement.add(snapshot2.getValue());
                                                                                                                sum += av1;
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                    Log.d("SUM", String.valueOf(sum));
                                                                                                    if (sum != 0) {
                                                                                                        average10 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                    } else {
                                                                                                        average10 = 0L;
                                                                                                    }
                                                                                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                            .child(String.valueOf(11));

                                                                                                    reference2.addValueEventListener(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                            ArrayList sumElement = new ArrayList();
                                                                                                            int sum = (0);
                                                                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                                    Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                                        Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                                        Long av1 = (Long) snapshot2.getValue();
                                                                                                                        sumElement.add(snapshot2.getValue());
                                                                                                                        sum += av1;
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                            Log.d("SUM", String.valueOf(sum));
                                                                                                            if (sum != 0) {
                                                                                                                average11 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                            } else {
                                                                                                                average11 = 0L;
                                                                                                            }
                                                                                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                                                                    .child(String.valueOf(12));

                                                                                                            reference2.addValueEventListener(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                    ArrayList sumElement = new ArrayList();
                                                                                                                    int sum = (0);
                                                                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                                        Log.d("Month Val", String.valueOf(dataSnapshot.getValue()));

                                                                                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                                                                            Log.d("Month Val2", String.valueOf(snapshot1.getValue()));
                                                                                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                                                                                Log.d("MonthVal3", String.valueOf(snapshot2.getValue()));
                                                                                                                                Long av1 = (Long) snapshot2.getValue();
                                                                                                                                sumElement.add(snapshot2.getValue());
                                                                                                                                sum += av1;
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                    Log.d("Monthly Array", String.valueOf(sumElement));
                                                                                                                    Log.d("SUM", String.valueOf(sum));
                                                                                                                    if (sum != 0) {
                                                                                                                        average12 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                                                    } else {
                                                                                                                        average12 = 0L;
                                                                                                                    }
                                                                                                                    Log.d("Average Jan", String.valueOf(average1));
                                                                                                                    Log.d("Average Feb", String.valueOf(average2));
                                                                                                                    Log.d("Average Mar", String.valueOf(average3));
                                                                                                                    Log.d("Average Apr", String.valueOf(average4));
                                                                                                                    Log.d("Average May", String.valueOf(average5));
                                                                                                                    Log.d("Average Jun", String.valueOf(average6));
                                                                                                                    Log.d("Average Jul", String.valueOf(average7));
                                                                                                                    Log.d("Average Aug", String.valueOf(average8));
                                                                                                                    Log.d("Average Sep", String.valueOf(average9));
                                                                                                                    Log.d("Average Oct", String.valueOf(average10));
                                                                                                                    Log.d("Average Nov", String.valueOf(average11));
                                                                                                                    Log.d("Average Dec", String.valueOf(average12));

                                                                                                                    lineEntries.add(new Entry(1, average1));
                                                                                                                    lineEntries.add(new Entry(2, average2));
                                                                                                                    lineEntries.add(new Entry(3, average3));
                                                                                                                    lineEntries.add(new Entry(4, average4));
                                                                                                                    lineEntries.add(new Entry(5, average5));
                                                                                                                    lineEntries.add(new Entry(6, average6));
                                                                                                                    lineEntries.add(new Entry(7, average7));
                                                                                                                    lineEntries.add(new Entry(8, average8));
                                                                                                                    lineEntries.add(new Entry(9, average9));
                                                                                                                    lineEntries.add(new Entry(10, average10));
                                                                                                                    lineEntries.add(new Entry(11, average11));
                                                                                                                    lineEntries.add(new Entry(12, average12));

                                                                                                                    lineDataSet = new LineDataSet(lineEntries, "Relax Progress");
                                                                                                                    lineData = new LineData(lineDataSet);
                                                                                                                    lineChart.setData(lineData);

                                                                                                                    lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                                                                                                    lineDataSet.setValueTextColor(Color.WHITE);
                                                                                                                    lineDataSet.setValueTextSize(10f);

                                                                                                                    lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                                                                                    lineChart.setBorderColor(Color.TRANSPARENT);
                                                                                                                    lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                                                                                    lineChart.getAxisLeft().setDrawGridLines(false);
                                                                                                                    lineChart.getXAxis().setDrawGridLines(false);
                                                                                                                    lineChart.getAxisRight().setDrawGridLines(false);
                                                                                                                    lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
                                                                                                                    lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                                                                                    lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
                                                                                                                    lineChart.getDescription().setTextColor(R.color.white);
                                                                                                                    lineChart.invalidate();
                                                                                                                    lineChart.refreshDrawableState();


                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                }
                                                                                                            });
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                        }
                                                                                                    });

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                }
                                                                                            });
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


//                Log.d("Line Entry", String.valueOf(lineEntries));
                }

            }
        }, 3000);
    }


    private void NavigationBar() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.relax:
                        startActivity(new Intent(getApplicationContext(), Relax.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.journal:
                        startActivity(new Intent(getApplicationContext(), Journal.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.challenge:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileMain.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}