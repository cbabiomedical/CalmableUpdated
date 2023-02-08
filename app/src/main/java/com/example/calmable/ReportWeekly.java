package com.example.calmable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
//import com.github.mikephil.charting.formatter.ValueFormatter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ReportWeekly extends AppCompatActivity {

    BarChart barChartweekly, barChart2;
    AppCompatButton monthly;
    StorageReference storageReference;
    AppCompatButton yearly;
    AppCompatButton daily, whereAmI;
    Button place;
    File fileName, localFile;
    FirebaseUser mUser;
    TextView tvDate;
    TextView person, time;
    String text, finalValue, finalTime;
    String stressedTime;
    Long average1, average2, average3, average4, average5;
    int sum1, sum2, sum3, sum4, sum5;
    Long averageR1, averageR2, averageR3, averageR4, averageR5;
    int sumR1, sumR2, sumR3, sumR4, sumR5;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_weekly);

        barChartweekly = (BarChart) findViewById(R.id.barChartWeekly);
        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.yearly);
        daily = findViewById(R.id.daily);
        place = findViewById(R.id.tv4);
        person = findViewById(R.id.tv5);
        time = findViewById(R.id.tv6);

        tvDate = (TextView) findViewById(R.id.tvDate);

        Date realDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = sdf.format(realDate);
        tvDate.setText(date);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
        finalValue = sharedPreferences.getString("word", null);
        finalTime = sharedPreferences.getString("time", null);
//        stressedTime = Integer.parseInt(finalTime)+ ":00" + " - " +(Integer.parseInt(finalTime)+1)+ ":00";
//        Log.d("stressedTimeWeekly----", stressedTime);

        person.setText(String.valueOf(finalValue));
//        time.setText(stressedTime);

        NavigationBar();


        //Initializing arraylist and storing input data to arraylist
        ArrayList<Float> obj = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f));//Array list1 to write data to file
        //Writing data to file
        try {
            fileName = new File(getCacheDir() + "/reportWeekly.txt");
            FileWriter fw;
            fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            int size = obj.size();
            for (int i = 0; i < size; i++) {
                output.write(obj.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        storageReference = FirebaseStorage.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        // Uploading file created to firebase storage
        StorageReference storageReference1a = storageReference.child("users/" + mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1a.child("reportWeekly.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportWeekly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportWeekly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        final int delay = 5000;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        int month = now.get(Calendar.MONTH) + 1;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Stressed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(1));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("AV1WEEK1", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                Double av1 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                sum1 += av1;
                                sumElement.add(av1);
                            }
                        }
                        if (sum1 != 0) {
                            average1 = sum1 / Long.parseLong(String.valueOf(sumElement.size()));
                            Log.d("AverageWEEK1", String.valueOf(average1));
                        } else {
                            average1 = 0L;
                        }
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Relaxed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(1));
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElementR = new ArrayList();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                        Double av2 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                        sumR1 += av2;
                                        sumElementR.add(av2);
                                    }
                                }
                                if (sumR1 != 0) {
                                    averageR1 = sumR1 / Long.parseLong(String.valueOf(sumElementR.size()));
                                } else {
                                    averageR1 = 0L;
                                }
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Stressed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(2));
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Log.d("AV1WEEK2", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("DataSnapshot2", String.valueOf(dataSnapshot1.getValue()));
                                                Double av1 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                                sum2 += av1;
                                                sumElement.add(av1);
                                            }
                                        }
                                        if (sum2 != 0) {
                                            average2 = sum2 / Long.parseLong(String.valueOf(sumElement.size()));
                                            Log.d("AverageWEEK2", String.valueOf(average2));
                                        } else {
                                            average2 = 0L;
                                        }
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Relaxed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(2));
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElementR = new ArrayList();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                        Double av2 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                                        sumR2 += av2;
                                                        sumElementR.add(av2);
                                                    }
                                                }
                                                if (sumR2 != 0) {
                                                    averageR2 = sumR2 / Long.parseLong(String.valueOf(sumElementR.size()));
                                                } else {
                                                    averageR2 = 0L;
                                                }
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Stressed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(3));
                                                reference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();

                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Log.d("AV1WEEK3", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("DataSnapshot3", String.valueOf(dataSnapshot1.getValue()));
                                                                Double av1 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                                                sum3 += av1;
                                                                sumElement.add(av1);
                                                            }
                                                        }
                                                        if (sum3 != 0) {
                                                            average3 = sum3 / Long.parseLong(String.valueOf(sumElement.size()));
                                                            Log.d("AverageWEEK3", String.valueOf(average3));
                                                        } else {
                                                            average3 = 0L;
                                                        }
                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Relaxed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(3));
                                                        reference.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElementR = new ArrayList();
                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                                        Double av2 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                                                        sumR3 += av2;
                                                                        sumElementR.add(av2);
                                                                    }
                                                                }
                                                                if (sumR3 != 0) {
                                                                    averageR3 = sumR3 / Long.parseLong(String.valueOf(sumElementR.size()));
                                                                } else {
                                                                    averageR3 = 0L;
                                                                }
                                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Stressed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(4));
                                                                reference.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();

                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                            Log.d("AV1WEEK4", String.valueOf(dataSnapshot.getValue()));
                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                Log.d("DataSnapshot4", String.valueOf(dataSnapshot1.getValue()));
                                                                                Double av1 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                                                                sum4 += av1;
                                                                                sumElement.add(av1);
                                                                            }
                                                                        }
                                                                        if (sum4 != 0) {
                                                                            average4 = sum4 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                            Log.d("AverageWEEK4", String.valueOf(average4));
                                                                        } else {
                                                                            average4 = 0L;
                                                                        }
                                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Relaxed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(4));
                                                                        reference.addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                ArrayList sumElementR = new ArrayList();
                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                        Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                                                        Double av2 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                                                                        sumR4 += av2;
                                                                                        sumElementR.add(av2);
                                                                                    }
                                                                                }
                                                                                if (sumR4 != 0) {
                                                                                    averageR4 = sumR4 / Long.parseLong(String.valueOf(sumElementR.size()));
                                                                                } else {
                                                                                    averageR4 = 0L;
                                                                                }
                                                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Stressed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(5));
                                                                                reference.addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        ArrayList sumElement = new ArrayList();

                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                            Log.d("AV1WEEK3", String.valueOf(dataSnapshot.getValue()));
                                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                Log.d("DataSnapshot3", String.valueOf(dataSnapshot1.getValue()));
                                                                                                Double av1 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                                                                                sum5 += av1;
                                                                                                sumElement.add(av1);
                                                                                            }
                                                                                        }
                                                                                        if (sum5 != 0) {
                                                                                            average5 = sum5 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                                            Log.d("AverageWEEK3", String.valueOf(average3));
                                                                                        } else {
                                                                                            average5 = 0L;
                                                                                        }
                                                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Relaxed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(5));
                                                                                        reference.addValueEventListener(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                ArrayList sumElementR = new ArrayList();
                                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                                        Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                                                                        Double av2 = Double.parseDouble(String.valueOf(dataSnapshot1.getValue()));
                                                                                                        sumR5 += av2;
                                                                                                        sumElementR.add(av2);
                                                                                                    }
                                                                                                }
                                                                                                if (sumR5 != 0) {
                                                                                                    averageR5 = sumR5 / Long.parseLong(String.valueOf(sumElementR.size()));
                                                                                                } else {
                                                                                                    averageR5 = 0L;
                                                                                                }


                                                                                                Log.d("AverageStressWeek1", String.valueOf(average1));
                                                                                                Log.d("AverageRelaxWeek1", String.valueOf(averageR1));
                                                                                                Log.d("AverageStressWeek2", String.valueOf(average2));
                                                                                                Log.d("AverageRelaxWeek2", String.valueOf(averageR2));
                                                                                                Log.d("AverageStressWeek3", String.valueOf(average3));
                                                                                                Log.d("AverageRelaxWeek3", String.valueOf(averageR3));
                                                                                                Log.d("AverageStressWeek4", String.valueOf(average4));
                                                                                                Log.d("AverageRelaxWeek4", String.valueOf(averageR4));
                                                                                                Log.d("AverageStressWeek5", String.valueOf(average5));
                                                                                                Log.d("AverageRelaxWeek5", String.valueOf(averageR5));

                                                                                                ArrayList<BarEntry> dataVals = new ArrayList<>();
                                                                                                dataVals.add(new BarEntry(0, new float[]{averageR1, average1}));
                                                                                                dataVals.add(new BarEntry(1, new float[]{averageR2, average2}));
                                                                                                dataVals.add(new BarEntry(2, new float[]{averageR3, average3}));
                                                                                                dataVals.add(new BarEntry(3, new float[]{averageR4, average4}));
                                                                                                dataVals.add(new BarEntry(4, new float[]{averageR5, average5}));
                                                                                                BarDataSet barDataSet = new BarDataSet(dataVals, "Time");
                                                                                                int[] colorClassArray = new int[]{Color.rgb(1, 135, 134), Color.rgb(255, 51, 51)};
                                                                                                barDataSet.setColors(colorClassArray);
                                                                                                barChartweekly.setDrawGridBackground(false);
                                                                                                barDataSet.setStackLabels(new String[]{"Relaxed Time", "Stressed"});
                                                                                                String[] daysS = new String[]{"Week1", "Week2", "Week3", "Week4"};
                                                                                                XAxis xAxis = barChartweekly.getXAxis();
                                                                                                xAxis.setValueFormatter(new IndexAxisValueFormatter(daysS));
                                                                                                barChartweekly.getXAxis().setLabelCount(5);

                                                                                                BarData barData = new BarData(barDataSet);
                                                                                                barChartweekly.setData(barData);

                                                                                                barChartweekly.animateXY(1500, 1500);
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

        }, delay);


        // On click listener of daily button
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportHome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportMonthly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        // On click listener of yearly button
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportYearly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
//
//        // On click listener of where am i toggle button
//        whereAmI.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWhereamI.class);
//                startActivity(intent);
//            }
//        });
    }


    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> credits) {
            super(yVals, label);
            this.credits = credits;
        }

        @Override
        public int getColor(int index) {
            float c = credits.get(index);

            if (c > 80) {
                return mColors.get(0);
            } else if (c > 60) {
                return mColors.get(1);
            } else if (c > 40) {
                return mColors.get(2);
            } else if (c > 20) {
                return mColors.get(3);
            } else {
                return mColors.get(4);
            }

        }
    }

    private class MyXAxisValueFormatter extends ReportWeekly.ValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
            Log.d("Tag", "monthly clicked hi");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }

    }

    public abstract class ValueFormatter implements IAxisValueFormatter, IValueFormatter {


        @Override
        @Deprecated
        public String getFormattedValue(float value, AxisBase axis) {
            return getFormattedValue(value);
        }


        @Override
        @Deprecated
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return getFormattedValue(value);
        }


        public String getFormattedValue(float value) {
            return String.valueOf(value);
        }


        public String getAxisLabel(float value, AxisBase axis) {
            return getFormattedValue(value);
        }


        public String getBarLabel(BarEntry barEntry) {
            return getFormattedValue(barEntry.getY());
        }


        public String getBarStackedLabel(float value, BarEntry stackedEntry) {
            return getFormattedValue(value);
        }


        public String getPointLabel(Entry entry) {
            return getFormattedValue(entry.getY());
        }


        public String getPieLabel(float value, PieEntry pieEntry) {
            return getFormattedValue(value);
        }


        public String getRadarLabel(RadarEntry radarEntry) {
            return getFormattedValue(radarEntry.getY());
        }


        public String getBubbleLabel(BubbleEntry bubbleEntry) {
            return getFormattedValue(bubbleEntry.getSize());
        }


        public String getCandleLabel(CandleEntry candleEntry) {
            return getFormattedValue(candleEntry.getHigh());
        }

    }

//    public void daily(View view) {
//        Intent intent2 = new Intent(this, reportHome.class);
//        startActivity(intent2);
//    }

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
        Intent intent = new Intent(getApplicationContext(), ReportHome.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
