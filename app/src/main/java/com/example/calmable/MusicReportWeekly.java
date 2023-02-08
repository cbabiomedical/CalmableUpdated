package com.example.calmable;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

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
import com.github.mikephil.charting.components.AxisBase;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class MusicReportWeekly extends AppCompatActivity {

    GifImageView c1gif, c2gif;
    int color;
    View c1, c2;
    AppCompatButton daily, yearly, monthly;
    FirebaseUser mUser;
    ImageView concentrationBtn, relaxationBtn;
    String text;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Long average1, average2, average3, average4;
    Double average11, average12, average13, average14;
    TextView tvDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_report_weekly);

        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.yearly);
        daily = findViewById(R.id.daily);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        lineChart = findViewById(R.id.lineChartWeekly);

        tvDate = (TextView) findViewById(R.id.tvDate);

        Date realDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = sdf.format(realDate);
        tvDate.setText("Until " + date);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        NavigationBar();
        /////////////////////////////////////////////////////////////////////

        getEntries();


//
        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();

        // On click listener of daily button
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MusicReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MusicReportMonthly.class);
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
                            .child(String.valueOf(month)).child(String.valueOf(1));

                    reference.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList sumElement = new ArrayList();
                            Double sum = (0.0);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Log.d("Weekly Val", String.valueOf(dataSnapshot.getValue()));
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    Log.d("Array", String.valueOf(snapshot1.getValue()));
                                    Double av1 = Double.parseDouble(String.valueOf(snapshot1.getValue()));
                                    sumElement.add(snapshot1.getValue());
                                    sum += av1;


                                }
                            }
                            Log.d("Weekly Array", String.valueOf(sumElement));
                            Log.d("SUM", String.valueOf(sum));
                            if (sum != 0.0) {
                                average11 = sum / sumElement.size();
                            } else {
                                average11 = 0.0;
                            }
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                    .child(String.valueOf(month)).child(String.valueOf(2));

                            reference.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList sumElement = new ArrayList();
                                    Double sum = (0.0);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Log.d("Weekly2 Val", String.valueOf(dataSnapshot.getValue()));
                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                            Log.d("Array", String.valueOf(snapshot1.getValue()));
                                            Double av1 = Double.parseDouble(String.valueOf(snapshot1.getValue()));
                                            sumElement.add(snapshot1.getValue());
                                            sum += av1;


                                        }
                                    }
                                    Log.d("Weekly Array", String.valueOf(sumElement));
                                    Log.d("SUM", String.valueOf(sum));
                                    if (sum != 0.0) {
                                        average12 = sum / sumElement.size();
                                    } else {
                                        average12 = 0.0;
                                    }

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                            .child(String.valueOf(month)).child(String.valueOf(3));

                                    reference.addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ArrayList sumElement = new ArrayList();
                                            Double sum = (0.0);
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                Log.d("Weekly3 Val", String.valueOf(dataSnapshot.getValue()));
                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                    Log.d("Array", String.valueOf(snapshot1.getValue()));
                                                    Double av1 = Double.parseDouble(String.valueOf(snapshot1.getValue()));
                                                    sumElement.add(snapshot1.getValue());
                                                    sum += av1;


                                                }
                                            }
                                            Log.d("Weekly 3 Array", String.valueOf(sumElement));
                                            Log.d("SUM", String.valueOf(sum));
                                            if (sum != 0) {
                                                average13 = sum / sumElement.size();
                                            } else {
                                                average13 = 0.0;
                                            }

                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                    .child(String.valueOf(month)).child(String.valueOf(4));

                                            reference.addValueEventListener(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    ArrayList sumElement = new ArrayList();
                                                    Double sum = (0.0);
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        Log.d("Weekly4 Val", String.valueOf(dataSnapshot.getValue()));
                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                            Log.d("Array", String.valueOf(snapshot1.getValue()));
                                                            Double av1 = Double.parseDouble(String.valueOf(snapshot1.getValue()));
                                                            sumElement.add(snapshot1.getValue());
                                                            sum += av1;


                                                        }
                                                    }
                                                    Log.d("Weekly 4 Array", String.valueOf(sumElement));
                                                    Log.d("SUM", String.valueOf(sum));
                                                    if (sum != 0.0) {
                                                        average14 = sum / sumElement.size();
                                                    } else {
                                                        average14 = 0.0;
                                                    }

                                                    Log.d("Average Week1", String.valueOf(average1));
                                                    Log.d("Average Week2", String.valueOf(average2));
                                                    Log.d("Average Week3", String.valueOf(average3));
                                                    Log.d("Average Week3", String.valueOf(average4));

                                                    lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(average11))));
                                                    lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(average12))));
                                                    lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(average13))));
                                                    lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(average14))));

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
                                                    lineChart.getXAxis().setTextColor(R.color.white);
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
                } else {


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                            .child(String.valueOf(month)).child(String.valueOf(1));

                    reference.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList sumElement = new ArrayList();
                            int sum = (0);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Log.d("Weekly Val", String.valueOf(dataSnapshot.getValue()));
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    Log.d("Array", String.valueOf(snapshot1.getValue()));
                                    Long av1 = (Long) snapshot1.getValue();
                                    sumElement.add(snapshot1.getValue());
                                    sum += av1;


                                }
                            }
                            Log.d("Weekly Array", String.valueOf(sumElement));
                            Log.d("SUM", String.valueOf(sum));
                            if (sum != 0) {
                                average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                            } else {
                                average1 = 0L;
                            }
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                    .child(String.valueOf(month)).child(String.valueOf(2));

                            reference.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList sumElement = new ArrayList();
                                    int sum = (0);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Log.d("Weekly2 Val", String.valueOf(dataSnapshot.getValue()));
                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                            Log.d("Array", String.valueOf(snapshot1.getValue()));
                                            Long av1 = (Long) snapshot1.getValue();
                                            sumElement.add(snapshot1.getValue());
                                            sum += av1;


                                        }
                                    }
                                    Log.d("Weekly Array", String.valueOf(sumElement));
                                    Log.d("SUM", String.valueOf(sum));
                                    if (sum != 0) {
                                        average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                    } else {
                                        average2 = 0L;
                                    }

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                            .child(String.valueOf(month)).child(String.valueOf(3));

                                    reference.addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ArrayList sumElement = new ArrayList();
                                            int sum = (0);
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                Log.d("Weekly3 Val", String.valueOf(dataSnapshot.getValue()));
                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                    Log.d("Array", String.valueOf(snapshot1.getValue()));
                                                    Long av1 = (Long) snapshot1.getValue();
                                                    sumElement.add(snapshot1.getValue());
                                                    sum += av1;


                                                }
                                            }
                                            Log.d("Weekly 3 Array", String.valueOf(sumElement));
                                            Log.d("SUM", String.valueOf(sum));
                                            if (sum != 0) {
                                                average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                            } else {
                                                average3 = 0L;
                                            }

                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)))
                                                    .child(String.valueOf(month)).child(String.valueOf(4));

                                            reference.addValueEventListener(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    ArrayList sumElement = new ArrayList();
                                                    int sum = (0);
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        Log.d("Weekly4 Val", String.valueOf(dataSnapshot.getValue()));
                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                            Log.d("Array", String.valueOf(snapshot1.getValue()));
                                                            Long av1 = (Long) snapshot1.getValue();
                                                            sumElement.add(snapshot1.getValue());
                                                            sum += av1;


                                                        }
                                                    }
                                                    Log.d("Weekly 4 Array", String.valueOf(sumElement));
                                                    Log.d("SUM", String.valueOf(sum));
                                                    if (sum != 0) {
                                                        average4 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                    } else {
                                                        average4 = 0L;
                                                    }

                                                    Log.d("Average Week1", String.valueOf(average1));
                                                    Log.d("Average Week2", String.valueOf(average2));
                                                    Log.d("Average Week3", String.valueOf(average3));
                                                    Log.d("Average Week3", String.valueOf(average4));

                                                    lineEntries.add(new Entry(1, average1));
                                                    lineEntries.add(new Entry(2, average2));
                                                    lineEntries.add(new Entry(3, average3));
                                                    lineEntries.add(new Entry(4, average4));

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
                                                    lineChart.getXAxis().setTextColor(R.color.white);
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