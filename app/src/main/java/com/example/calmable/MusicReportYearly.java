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

public class MusicReportYearly extends AppCompatActivity {

    AppCompatButton daily, weekly, monthly;
    String text;
    ImageView concentrationBtn, relaxationBtn;
    FirebaseUser mUser;
    GifImageView c1gif, c2gif;
    int color;
    View c1, c2;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Long average, average1, average2, average3;
    Double averageW, average11, average12, average13;
    TextView tvDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_report_yearly);
        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        monthly = findViewById(R.id.monthly);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        lineChart = findViewById(R.id.lineChartYearly);

        tvDate = (TextView) findViewById(R.id.tvDate);

        Date realDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = sdf.format(realDate);
        tvDate.setText("Until " + date);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        NavigationBar();
        ////////////////////////////////////////////

        getEntries();


        //Initializing arraylist and storing input data to arraylist
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MusicReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MusicReportWeekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MusicReportMonthly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    private void getEntries() {

        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        int year1 = now.get(Calendar.YEAR) - 3;
        int year2 = now.get(Calendar.YEAR) - 2;
        int year3 = now.get(Calendar.YEAR) - 1;
        //prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                lineEntries = new ArrayList();

                if (DeviceActivity.connected) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(year1));
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList sumElement = new ArrayList();
                            Double sum = (0.0);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                            Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                            Double av1 = Double.parseDouble(String.valueOf(snapshot3.getValue()));
                                            sumElement.add(snapshot1.getValue());
                                            sum += av1;
                                        }
                                    }
                                }
                            }
                            Log.d("SUM", String.valueOf(sum));
                            if (sum != 0) {
                                average11 = sum / sumElement.size();
                            } else {
                                average11 = 0.0;
                            }

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(year2));
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList sumElement = new ArrayList();
                                    Double sum = (0.0);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                    Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                    Double av1 = Double.parseDouble(String.valueOf(snapshot3.getValue()));
                                                    sumElement.add(snapshot1.getValue());
                                                    sum += av1;
                                                }
                                            }
                                        }
                                    }
                                    Log.d("SUM", String.valueOf(sum));
                                    if (sum != 0) {
                                        average12 = sum / sumElement.size();
                                    } else {
                                        average12 = 0.0;
                                    }

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(year3));
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ArrayList sumElement = new ArrayList();
                                            Double sum = (0.0);
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                            Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                            Double av1 = Double.parseDouble(String.valueOf(snapshot3.getValue()));
                                                            sumElement.add(snapshot1.getValue());
                                                            sum += av1;
                                                        }
                                                    }
                                                }
                                            }
                                            Log.d("SUM", String.valueOf(sum));
                                            if (sum != 0) {
                                                average13 = sum / sumElement.size();
                                            } else {
                                                average13 = 0.0;
                                            }
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)));
                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    ArrayList sumElement = new ArrayList();
                                                    Double sum = (0.0);
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                                    Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                                    Double av1 = Double.parseDouble(String.valueOf(snapshot3.getValue()));
                                                                    sumElement.add(snapshot1.getValue());
                                                                    sum += av1;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    Log.d("SUM", String.valueOf(sum));
                                                    if (sum != 0) {
                                                        averageW = sum / sumElement.size();
                                                    } else {
                                                        averageW = 0.0;
                                                    }
                                                    Log.d("Average", String.valueOf(averageW));
                                                    Log.d("Average1", String.valueOf(average1));
                                                    Log.d("Average2", String.valueOf(average2));
                                                    Log.d("Average4", String.valueOf(average3));


                                                    lineEntries.add(new Entry(year1, Float.parseFloat(String.valueOf(average11))));
                                                    lineEntries.add(new Entry(year2, Float.parseFloat(String.valueOf(average12))));
                                                    lineEntries.add(new Entry(year3, Float.parseFloat(String.valueOf(average13))));
                                                    lineEntries.add(new Entry(now.get(Calendar.YEAR), Float.parseFloat(String.valueOf(averageW))));
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
                } else {


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(year1));
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList sumElement = new ArrayList();
                            int sum = (0);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                            Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                            Long av1 = (Long) snapshot3.getValue();
                                            sumElement.add(snapshot1.getValue());
                                            sum += av1;
                                        }
                                    }
                                }
                            }
                            Log.d("SUM", String.valueOf(sum));
                            if (sum != 0) {
                                average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                            } else {
                                average1 = 0L;
                            }

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(year2));
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList sumElement = new ArrayList();
                                    int sum = (0);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                    Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                    Long av1 = (Long) snapshot3.getValue();
                                                    sumElement.add(snapshot1.getValue());
                                                    sum += av1;
                                                }
                                            }
                                        }
                                    }
                                    Log.d("SUM", String.valueOf(sum));
                                    if (sum != 0) {
                                        average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                    } else {
                                        average2 = 0L;
                                    }

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(year3));
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ArrayList sumElement = new ArrayList();
                                            int sum = (0);
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                            Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                            Long av1 = (Long) snapshot3.getValue();
                                                            sumElement.add(snapshot1.getValue());
                                                            sum += av1;
                                                        }
                                                    }
                                                }
                                            }
                                            Log.d("SUM", String.valueOf(sum));
                                            if (sum != 0) {
                                                average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                            } else {
                                                average3 = 0L;
                                            }
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Music").child(String.valueOf(now.get(Calendar.YEAR)));
                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    ArrayList sumElement = new ArrayList();
                                                    int sum = (0);
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                                for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                                    Log.d("Yearly", String.valueOf(snapshot3.getValue()));
                                                                    Long av1 = (Long) snapshot3.getValue();
                                                                    sumElement.add(snapshot1.getValue());
                                                                    sum += av1;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    Log.d("SUM", String.valueOf(sum));
                                                    if (sum != 0) {
                                                        average = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                    } else {
                                                        average = 0L;
                                                    }
                                                    Log.d("Average", String.valueOf(average));
                                                    Log.d("Average1", String.valueOf(average1));
                                                    Log.d("Average2", String.valueOf(average2));
                                                    Log.d("Average4", String.valueOf(average3));


                                                    lineEntries.add(new Entry(year1, average1));
                                                    lineEntries.add(new Entry(year2, average2));
                                                    lineEntries.add(new Entry(year3, average3));
                                                    lineEntries.add(new Entry(now.get(Calendar.YEAR), average));
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