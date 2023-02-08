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
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.io.FileReader;
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

import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ReportYearly extends AppCompatActivity {

    BarChart barChartYearly;
    AppCompatButton monthly;
    AppCompatButton weekly;
    AppCompatButton daily, whereAmI;
    File fileName, localFile;
    StorageReference storageReference;
    FirebaseUser mUser;
    String text, finalValue, finalTime;
    String stressedTime;
    Button place;
    TextView person, time, tvDate;
    public static String word;
    Long average1, average2, average3, average;
    int sum1, sum2, sum3, sum4;
    Long averageR1, averageR2, averageR3, averageR;
    int sumR1, sumR2, sumR3, sumR4;

    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_yearly);

        barChartYearly = (BarChart) findViewById(R.id.barChartYearly);
        monthly = findViewById(R.id.monthly);
        weekly = findViewById(R.id.weekly);
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
        finalTime = sharedPreferences.getString("time", null);
//        stressedTime = Integer.parseInt(finalTime) + ":00" + " - " + (Integer.parseInt(finalTime) + 1) + ":00";
//        Log.d("stressedTimeWeekly----", stressedTime);

//        time.setText(stressedTime);

        NavigationBar();

        //Initializing arraylist and storing input data to arraylist
        ArrayList<Float> obj = new ArrayList<>(
                Arrays.asList(30f, 86f, 10f, 50f)); //Array list1 to write data to file
        //Writing data to file
        try {
            fileName = new File(getCacheDir() + "/reportYearly.txt");
            String line = "";
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
            StorageReference mountainsRef = storageReference1a.child("reportYearly.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportYearly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportYearly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
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
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        int year1 = now.get(Calendar.YEAR) - 3;
        int year2 = now.get(Calendar.YEAR) - 2;
        int year3 = now.get(Calendar.YEAR) - 1;
        //prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);


        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                mUser.getUid();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Stressed").child(mUser.getUid()).child(String.valueOf(year1));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("AV1Year1", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    Log.d("DataSnapshot2", String.valueOf(dataSnapshot2.getValue()));
                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                        Double av1 = Double.parseDouble(String.valueOf(dataSnapshot3.getValue()));
                                        sum1 += av1;
                                        sumElement.add(av1);
                                    }
                                }
                            }
                        }
                        if (sum1 != 0) {
                            average1 = sum1 / Long.parseLong(String.valueOf(sumElement.size()));
                            Log.d("AverageYear1", String.valueOf(average1));
                        } else {
                            average1 = 0L;
                        }
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Relaxed").child(mUser.getUid()).child(String.valueOf(year1));
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElementR = new ArrayList();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                            Log.d("DataSnapshot2", String.valueOf(dataSnapshot2.getValue()));
                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                Double av2 = Double.parseDouble(String.valueOf(dataSnapshot3.getValue()));
                                                sumR1 += av2;
                                                sumElementR.add(av2);
                                            }
                                        }
                                    }
                                }
                                if (sumR1 != 0) {
                                    averageR1 = sumR1 / Long.parseLong(String.valueOf(sumElementR.size()));
                                } else {
                                    averageR1 = 0L;
                                }
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Stressed").child(mUser.getUid()).child(String.valueOf(year2));
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Log.d("AV1Year1", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                    Log.d("DataSnapshot2", String.valueOf(dataSnapshot2.getValue()));
                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                        Double av1 = Double.parseDouble(String.valueOf(dataSnapshot3.getValue()));
                                                        sum2 += av1;
                                                        sumElement.add(av1);
                                                    }
                                                }
                                            }
                                        }
                                        if (sum2 != 0) {
                                            average2 = sum2 / Long.parseLong(String.valueOf(sumElement.size()));
                                            Log.d("AverageYear2", String.valueOf(average2));
                                        } else {
                                            average2 = 0L;
                                        }
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Relaxed").child(mUser.getUid()).child(String.valueOf(year2));
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElementR = new ArrayList();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                            Log.d("DataSnapshot2", String.valueOf(dataSnapshot2.getValue()));
                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                Double av2 = Double.parseDouble(String.valueOf(dataSnapshot3.getValue()));
                                                                sumR2 += av2;
                                                                sumElementR.add(av2);
                                                            }
                                                        }
                                                    }
                                                }
                                                if (sumR2 != 0) {
                                                    averageR2 = sumR2 / Long.parseLong(String.valueOf(sumElementR.size()));
                                                } else {
                                                    averageR2 = 0L;
                                                }
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Stressed").child(mUser.getUid()).child(String.valueOf(year3));
                                                reference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();

                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Log.d("AV1Year3", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                    Log.d("DataSnapshot2", String.valueOf(dataSnapshot2.getValue()));
                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                        Double av1 = Double.parseDouble(String.valueOf(dataSnapshot3.getValue()));
                                                                        sum3 += av1;
                                                                        sumElement.add(av1);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if (sum3 != 0) {
                                                            average3 = sum3 / Long.parseLong(String.valueOf(sumElement.size()));
                                                            Log.d("AverageYear3", String.valueOf(average3));
                                                        } else {
                                                            average3 = 0L;
                                                        }
                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Relaxed").child(mUser.getUid()).child(String.valueOf(year3));
                                                        reference.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElementR = new ArrayList();
                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                            Log.d("DataSnapshot2", String.valueOf(dataSnapshot2.getValue()));
                                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                Double av2 = Double.parseDouble(String.valueOf(dataSnapshot3.getValue()));
                                                                                sumR3 += av2;
                                                                                sumElementR.add(av2);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if (sumR3 != 0) {
                                                                    averageR3 = sumR3 / Long.parseLong(String.valueOf(sumElementR.size()));
                                                                } else {
                                                                    averageR3 = 0L;
                                                                }
                                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Stressed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)));
                                                                reference.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();

                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                            Log.d("AV1Year3", String.valueOf(dataSnapshot.getValue()));
                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                                    Log.d("DataSnapshot2", String.valueOf(dataSnapshot2.getValue()));
                                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                        Double av1 = Double.parseDouble(String.valueOf(dataSnapshot3.getValue()));
                                                                                        sum4 += av1;
                                                                                        sumElement.add(av1);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        if (sum4 != 0) {
                                                                            average = sum4 / Long.parseLong(String.valueOf(sumElement.size()));
                                                                            Log.d("AverageYear4", String.valueOf(average));
                                                                        } else {
                                                                            average = 0L;
                                                                        }
                                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeChart").child("Time Relaxed").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)));
                                                                        reference.addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                ArrayList sumElementR = new ArrayList();
                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                        Log.d("DataSnapshot", String.valueOf(dataSnapshot1.getValue()));
                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                                                            Log.d("DataSnapshot2", String.valueOf(dataSnapshot2.getValue()));
                                                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                                                Double av2 = Double.parseDouble(String.valueOf(dataSnapshot3.getValue()));
                                                                                                sumR4 += av2;
                                                                                                sumElementR.add(av2);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if (sumR4 != 0) {
                                                                                    averageR = sumR4 / Long.parseLong(String.valueOf(sumElementR.size()));
                                                                                } else {
                                                                                    averageR = 0L;
                                                                                }
                                                                                Log.d("AverageStressYear1", String.valueOf(average1));
                                                                                Log.d("AverageRelaxYear1", String.valueOf(averageR1));
                                                                                Log.d("AverageStressYear2", String.valueOf(average2));
                                                                                Log.d("AverageRelaxYear2", String.valueOf(averageR2));
                                                                                Log.d("AverageStressYear3", String.valueOf(average3));
                                                                                Log.d("AverageRelaxYear3", String.valueOf(averageR3));
                                                                                Log.d("AverageStressYear4", String.valueOf(average3));
                                                                                Log.d("AverageRelaxYear4", String.valueOf(averageR3));

                                                                                ArrayList<BarEntry> dataVals = new ArrayList<>();
                                                                                dataVals.add(new BarEntry(0, new float[]{averageR1, average1}));
                                                                                dataVals.add(new BarEntry(1, new float[]{averageR2, average2}));
                                                                                dataVals.add(new BarEntry(2, new float[]{averageR3, average3}));
                                                                                dataVals.add(new BarEntry(3, new float[]{averageR, average}));
//
                                                                                BarDataSet barDataSet = new BarDataSet(dataVals, "Time");
                                                                                int[] colorClassArray = new int[]{Color.rgb(1, 135, 134), Color.rgb(255, 51, 51)};
                                                                                barDataSet.setColors(colorClassArray);
                                                                                barChartYearly.setDrawGridBackground(false);
                                                                                barDataSet.setStackLabels(new String[]{"Relaxed Time", "Stressed"});
                                                                                String[] daysS = new String[]{String.valueOf(year1), String.valueOf(year2), String.valueOf(year3), String.valueOf(now.get(Calendar.YEAR))};
                                                                                XAxis xAxis = barChartYearly.getXAxis();
                                                                                xAxis.setValueFormatter(new IndexAxisValueFormatter(daysS));
                                                                                barChartYearly.getXAxis().setLabelCount(4);

                                                                                BarData barData = new BarData(barDataSet);
                                                                                barChartYearly.setData(barData);

                                                                                barChartYearly.animateXY(1500, 1500);
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

        // On click listener of weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportWeekly.class);
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

//        // On click listener of where am i toggle button
//        whereAmI.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ConcentrationReportWhereamI.class);
//                startActivity(intent);
//            }
//        });

        //get max word
        try {
            String line = "";
            word = "";
            int count = 0, maxCount = 0;
            ArrayList<String> words = new ArrayList<String>();

            //Opens file in read mode
            FileReader file = new FileReader(getCacheDir() + "/stressedPeopleMonthly.txt");
            BufferedReader br = new BufferedReader(file);

            //Reads each line
            while ((line = br.readLine()) != null) {
                String string[] = line.toLowerCase().split("([,.\\s]+) ");
                //Adding all words generated in previous step into words
                for (String s : string) {
                    words.add(s);
                }
            }

            //Determine the most repeated word in a file
            for (int i = 0; i < words.size(); i++) {
                count = 1;
                //Count each word in the file and store it in variable count
                for (int j = i + 1; j < words.size(); j++) {
                    if (words.get(i).equals(words.get(j))) {
                        count++;
                    }
                }
                //If maxCount is less than count then store value of count in maxCount
                //and corresponding word to variable word
                if (count > maxCount) {
                    maxCount = count;
                    word = words.get(i);
                }
            }

//            //To save
//            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("word", word);
//            editor.commit();

            System.out.println("Most repeated word: " + word);
            person.setText(String.valueOf(word));
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private class MyXAxisValueFormatter extends ReportYearly.ValueFormatter {
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
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.relax:
                        startActivity(new Intent(getApplicationContext(), Relax.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.journal:
                        startActivity(new Intent(getApplicationContext(), Journal.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.challenge:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileMain.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
