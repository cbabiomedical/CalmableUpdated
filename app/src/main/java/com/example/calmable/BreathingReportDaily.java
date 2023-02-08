package com.example.calmable;

        import androidx.annotation.RequiresApi;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.graphics.Color;
        import android.os.Build;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatDelegate;
        import androidx.appcompat.widget.AppCompatButton;

        import android.content.Intent;
        import android.os.CountDownTimer;
        import android.os.Handler;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.github.mikephil.charting.charts.LineChart;
        import com.github.mikephil.charting.data.LineData;
        import com.github.mikephil.charting.data.LineDataSet;
        //import com.github.mikephil.charting.formatter.ValueFormatter;
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
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.io.InputStream;
        import java.text.Format;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.Locale;

        import com.github.mikephil.charting.data.Entry;

        import pl.droidsonroids.gif.GifImageView;

public class BreathingReportDaily extends AppCompatActivity {

    private Context context;
    private Button monthly, yearly, weekly;
    FirebaseUser mUser;
    ImageView concentrationBtn;
    String text;
    GifImageView c1gif, c2gif;
    int color;
    View c1, c2;
    File fileName;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    TextView tvDate;

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();


    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing_report_daily);


        monthly = findViewById(R.id.monthly);
        monthly.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(BreathingReportDaily.this, BreathingReportMonthly.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        weekly = findViewById(R.id.weekly);
        weekly.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(BreathingReportDaily.this, BreathingReportWeekly.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        yearly = findViewById(R.id.yearly);
        yearly.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(BreathingReportDaily.this, BreathingReportYearly.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        lineChart = findViewById(R.id.lineChartDaily);

        tvDate = (TextView) findViewById(R.id.tvDate);

        Date realDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = sdf.format(realDate);
        tvDate.setText(date);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        NavigationBar();
        /////////////////////////////////////////////////////////////////////
        getEntries();

        //Initializing arraylist and storing input data to arraylist

        try {
            fileName = new File(getCacheDir() + "/memRepDailyX.txt");  //Writing data to file
            FileWriter fw;
            fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            int size = xVal.size();
            for (int i = 0; i < size; i++) {
                output.write(xVal.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing X Data", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
//            Toast.makeText(this, "Failed Writing X Data", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        }

//
        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();
//
//        // Uploading saved data containing file to firebase storage
        StorageReference storageXAxis = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageXAxis.child("memRepDailyX.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(MemoryReportDaily.this, "File Uploaded X data", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(MemoryReportDaily.this, "File Uploading Failed X", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
        try {
            fileName = new File(getCacheDir() + "/memRepDailyY.txt");  //Writing data to file
            FileWriter fw;
            fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            int size = yVal.size();
            for (int i = 0; i < size; i++) {
                output.write(yVal.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing Y data", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        StorageReference storageYAxis = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageYAxis.child("memRepDailyY.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(MemoryReportDaily.this, "File Uploaded Y Axis", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(MemoryReportDaily.this, "File Uploading Failed Y Data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



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
            Long average1, average2, average3, average4, average5, average6, average7;
            int outside1, outside2, outside3, outside4, outside5, outside6, outside7;

            @Override
            public void run() {
                lineEntries = new ArrayList();

                DatabaseReference reference0 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Breathing Patterns").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");

                reference0.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        int sum = (0);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                            Long av1 = (Long) dataSnapshot.getValue();
                            Log.d("AV1", String.valueOf(av1));
                            sumElement.add(av1);
                            sum += av1;

                        }
                        Log.d("SUM", String.valueOf(sum));
                        if (sum != 0) {
                            average1 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                            Log.d("Average Mon", String.valueOf(average1));
                        } else {
                            average1 = Long.valueOf(0);
                        }
                        outside1 = Math.toIntExact(average1);
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Breathing Patterns").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");

                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                int sum = (0);
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                    Long av1 = (Long) dataSnapshot.getValue();
                                    Log.d("AV1", String.valueOf(av1));
                                    sumElement.add(av1);
                                    sum += av1;

                                }
                                Log.d("SUM", String.valueOf(sum));
                                if (sum != 0) {
                                    average2 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                    Log.d("Average Tue", String.valueOf(average2));

                                } else {
                                    average2 = Long.valueOf(0);
                                }

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Breathing Patterns").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");

                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        int sum = (0);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                            Long av1 = (Long) dataSnapshot.getValue();
                                            Log.d("AV1", String.valueOf(av1));
                                            sumElement.add(av1);
                                            sum += av1;

                                        }
                                        Log.d("SUM", String.valueOf(sum));
                                        if (sum != 0) {
                                            average3 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                            Log.d("Average Wed", String.valueOf(average3));

                                        } else {
                                            average3 = Long.valueOf(0);
                                        }

                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Breathing Patterns").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");

                                        reference3.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                int sum = (0);
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                                    Long av1 = (Long) dataSnapshot.getValue();
                                                    Log.d("AV1", String.valueOf(av1));
                                                    sumElement.add(av1);
                                                    sum += av1;

                                                }
                                                Log.d("SUM", String.valueOf(sum));
                                                if (sum != 0) {
                                                    average4 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                    Log.d("Average Thur", String.valueOf(average4));
                                                } else {
                                                    average4 = Long.valueOf(0);
                                                }
                                                DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Breathing Patterns").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");

                                                reference4.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();
                                                        int sum = (0);
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                            Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                                            Long av1 = (Long) dataSnapshot.getValue();
                                                            Log.d("AV1", String.valueOf(av1));
                                                            sumElement.add(av1);
                                                            sum += av1;

                                                        }
                                                        Log.d("SUM", String.valueOf(sum));
                                                        if (sum != 0) {
                                                            average5 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                            Log.d("Average Fri", String.valueOf(average5));
                                                        } else {
                                                            average5 = Long.valueOf(0);
                                                        }
                                                        DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Breathing Patterns").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");

                                                        reference5.addValueEventListener(new ValueEventListener() {
                                                            @RequiresApi(api = Build.VERSION_CODES.N)
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                ArrayList sumElement = new ArrayList();
                                                                int sum = (0);
                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                    Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                                                    Long av1 = (Long) dataSnapshot.getValue();
                                                                    Log.d("AV1", String.valueOf(av1));
                                                                    sumElement.add(av1);
                                                                    sum += av1;

                                                                }
                                                                Log.d("SUM", String.valueOf(sum));
                                                                if (sum != 0) {
                                                                    average6 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                    Log.d("Average Sat", String.valueOf(average6));

                                                                } else {
                                                                    average6 = Long.valueOf(0);
                                                                }

                                                                DatabaseReference reference6 = FirebaseDatabase.getInstance().getReference("Report").child(mUser.getUid()).child("Breathing Patterns").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");

                                                                reference6.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        ArrayList sumElement = new ArrayList();
                                                                        int sum = (0);
                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                            Log.d("Values", String.valueOf(dataSnapshot.getChildren()));

                                                                            Long av1 = (Long) dataSnapshot.getValue();
                                                                            Log.d("AV1", String.valueOf(av1));
                                                                            sumElement.add(av1);
                                                                            sum += av1;

                                                                        }
                                                                        Log.d("SUM", String.valueOf(sum));
                                                                        if (sum != 0) {
                                                                            average7 = sum / Long.parseLong(String.valueOf(sumElement.size()));
                                                                            Log.d("Average Sun", String.valueOf(average7));
                                                                        } else {
                                                                            average7 = Long.valueOf(0);
                                                                        }
                                                                        Log.d("Average Outside1", String.valueOf(average1));
                                                                        Log.d("Average Outside2", String.valueOf(average2));
                                                                        Log.d("Average Outside3", String.valueOf(average3));
                                                                        Log.d("Average Outside4", String.valueOf(average4));
                                                                        Log.d("Average Outside5", String.valueOf(average5));
                                                                        Log.d("Average Outside6", String.valueOf(average6));
                                                                        Log.d("Average Outside7", String.valueOf(average7));

                                                                        lineEntries.add(new Entry(1, average1));
                                                                        lineEntries.add(new Entry(2, average2));
                                                                        lineEntries.add(new Entry(3, average3));
                                                                        lineEntries.add(new Entry(4, average4));
                                                                        lineEntries.add(new Entry(5, average5));
                                                                        lineEntries.add(new Entry(6, average6));
                                                                        lineEntries.add(new Entry(7, average7));


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

                                                                outside6 = Math.toIntExact(average6);

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


                Log.d("Average Outside", String.valueOf(outside6));


            }
        }, 3000);
    }


//    public class MyBarDataset extends BarDataSet {
//
//        private List<Float> credits;
//
//        MyBarDataset(List<BarEntry> yVals, String label, List<Float> credits) {
//            super(yVals, label);
//            this.credits = credits;
//        }
//
//        @Override
//        public int getColor(int index) {
//            float c = credits.get(index);
//
//            if (c > 80) {
//                return mColors.get(0);
//            } else if (c > 60) {
//                return mColors.get(1);
//            } else if (c > 40) {
//                return mColors.get(2);
//            } else if (c > 20) {
//                return mColors.get(3);
//            } else {
//                return mColors.get(4);
//            }
//
//        }
//    }


//    public void monthly(View v) {
//        Intent intent2 = new Intent(this, ReportMonthly.class);
//        startActivity(intent2);
//
//    }
//
//    public void yearly(View view) {
//        Intent intent2 = new Intent(this, ReportYearly.class);
//        startActivity(intent2);
//    }
//
//    public void weekly(View view) {
//        Intent intent2 = new Intent(this, BreathingReportWeekly.class);
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
                Intent intent = new Intent(getApplicationContext(), BreathPatterns.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

        }
