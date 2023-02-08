package com.example.calmable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class VideoReportYearly extends AppCompatActivity {
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
    Double average, average1, average2, average3;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_report_yearly);

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
                Intent intent = new Intent(getApplicationContext(), VideoReportDaily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoReportWeekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoReportMonthly.class);
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

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("VideoIntervention").child(mUser.getUid()).child(String.valueOf(year1));
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
                            average1 = sum / sumElement.size();
                        } else {
                            average1 = 0.0;
                        }

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("VideoIntervention").child(mUser.getUid()).child(String.valueOf(year2));
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
                                    average2 = sum /sumElement.size();
                                } else {
                                    average2 = 0.0;
                                }

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("VideoIntervention").child(mUser.getUid()).child(String.valueOf(year3));
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
                                            average3 = sum / sumElement.size();
                                        } else {
                                            average3 = 0.0;
                                        }
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReportWW").child("VideoIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)));
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
                                                    average = sum / sumElement.size();
                                                } else {
                                                    average = 0.0;
                                                }
                                                Log.d("Average", String.valueOf(average));
                                                Log.d("Average1", String.valueOf(average1));
                                                Log.d("Average2", String.valueOf(average2));
                                                Log.d("Average4", String.valueOf(average3));


                                                lineEntries.add(new Entry(year1, Float.parseFloat(String.valueOf(average1))));
                                                lineEntries.add(new Entry(year2, Float.parseFloat(String.valueOf(average2))));
                                                lineEntries.add(new Entry(year3, Float.parseFloat(String.valueOf(average3))));
                                                lineEntries.add(new Entry(now.get(Calendar.YEAR), Float.parseFloat(String.valueOf(average))));
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
