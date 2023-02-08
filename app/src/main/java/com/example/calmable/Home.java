package com.example.calmable;

import static android.content.ContentValues.TAG;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.example.calmable.device.DeviceActivity.connectStatus;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;

import com.example.calmable.db.HpyChtLocationDB;
import com.example.calmable.device.DeviceActivity;
import com.example.calmable.fitbit.FitbitMainActivity;
import com.example.calmable.scan.ScanActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Home extends AppCompatActivity implements PopUpOne.PopUpOneListener {

    static Home home;

    private static final String COINS = "coins";

    boolean stopThread = false;

    // for check location
    FusedLocationProviderClient fusedLocationProviderClient;
    String userLocationFullAddress;
    public static double latitude, longitude;
    File locationTxt , displayLocationTxt;
    ArrayList<String> listOFLocations;
    ArrayList<String> listOfTxtLocations;

    TextView txtHtRate;
    TextView txtProgress;
    TextView homeConnectStatus;
    TextView streesIndexTV, stressBanner;
    File fileName, fileName1,fileName2, filNameHeartRate , fileNameStressedIndexs;
    FirebaseFirestore database;
    FirebaseUser mUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference res;
    Button happy, awesome, relaxed, sleepy, sad;

    public static String viewPerson;
    public static String word;
    String viewPlace,viewReason;
    String time,finalword;
    int finalRateff , stressedIndex , stressedIndexAvg;
    List<Object> heartRateList;

    String timeAndHR;
    String timeAndHR2;
    String finalHRCompletedOne;
    public static String finalHR;
    public static String finalHR2;

    String selectedOption = "1";

    int markHeartRate = 0;
    TextView markHeartRateValue;

    private Handler mHandler , mHandlerStressedPopUp;
    private boolean automaticCalmingOption;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date;
    TextView GetDateAndTime;
    private DatabaseReference databaseReference;


    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // check current user id
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String fbID = mUser.getUid();
        Log.d("TAG", "*Current user ID : " + fbID);

        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Stress Index");

        streesIndexTV = (TextView) findViewById(R.id.streesIndex);
        stressBanner = (TextView) findViewById(R.id.stressBanner);


        /**
         * when click notification call special method
         *      stressed popup
         */
        if (getIntent().hasExtra("fromStressedIndexNotification")) {
            openDialog();
        }


        //call thread method - location
        stopThread = false;
        GetLocationRunnable runnable = new GetLocationRunnable();
        new Thread(runnable).start();

        // create text files in cache
        locationTxt = new File(getCacheDir() + "/locationData.txt");
        displayLocationTxt = new File(getCacheDir() + "/displayLocationTxt.txt");


        txtHtRate = (TextView) findViewById(R.id.htRate);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        TextView txtProgress2 = (TextView) findViewById(R.id.txtPastProgress);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        homeConnectStatus = (TextView) findViewById(R.id.homeConnectStatus);

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("data", 0);


        happy = findViewById(R.id.happyEmoji_1);
        awesome = findViewById(R.id.happyEmoji_2);
        relaxed = findViewById(R.id.happyEmoji_3);
        sleepy = findViewById(R.id.happyEmoji_4);
        sad = findViewById(R.id.happyEmoji_5);
        markHeartRateValue = (TextView) findViewById(R.id.markValue);

        // refresh Home for HR
        this.mHandler = new Handler();
        m_Runnable.run();

        // refresh Home for stressed pop up
        this.mHandlerStressedPopUp = new Handler();
        m_RunnableStressedPopup.run();

        Log.d("TAG", "------------>onCreate: " + finalRateff);


        if (DeviceActivity.connectStatus) {
            if (connectStatus = true){
                homeConnectStatus.setText("Device is Connected");
                homeConnectStatus.setTextColor(Color.GREEN);

            }

        }



        //updateLandingCoins();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.calmable", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //set button clicks to mood buttons
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int happyValue = 1;
                //Intent myIntent = new Intent(Home.this, CalenderActivity.class);
                //myIntent.putExtra("happyValue", happyValue);
                editor.putInt("happyValue", happyValue);
                editor.commit();
                //startActivity(myIntent);
                //Log.d("Happy value-------", String.valueOf(myIntent));
            }
        });

        awesome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int awesomeValue = 2;
                //Intent myIntent = new Intent(Home.this, CalenderActivity.class);
                editor.putInt("awesomeValue", awesomeValue);
                editor.commit();
//                startActivity(myIntent);
//                Log.d("awesome value-------", String.valueOf(myIntent));
            }
        });

        relaxed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int relaxedValue = 3;
                //Intent myIntent = new Intent(Home.this, CalenderActivity.class);
                editor.putInt("relaxedValue", relaxedValue);
                editor.commit();
//                startActivity(myIntent);
//                Log.d("relaxed value-------", String.valueOf(myIntent));
            }
        });

        sleepy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sleepyValue = 4;
                //Intent myIntent = new Intent(Home.this, CalenderActivity.class);
                editor.putInt("sleepyValue", sleepyValue);
                editor.commit();
//                startActivity(myIntent);
//                Log.d("sleepy value-------", String.valueOf(myIntent));
            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sadValue = 5;
                //Intent myIntent = new Intent(Home.this, CalenderActivity.class);
                editor.putInt("sadValue", sadValue);
                editor.commit();
//                startActivity(myIntent);
//                Log.d("sad value-------", String.valueOf(myIntent));
            }
        });



//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("MusicIntervention").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child("1");
//        reference.setValue(81);

        // get current time in hour only
        SimpleDateFormat simpleformat = new SimpleDateFormat("HH");
        String strHour = simpleformat.format(new Date());
        int hour = Integer.parseInt(strHour);
        Log.d("Current HOUR-------", strHour);
        Log.d("INT HOUR-------", String.valueOf(hour));

        SharedPreferences shaP = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
        int checkCount = shaP.getInt("count", 0);
        Log.d("checkCount SHARED---", String.valueOf(checkCount));

        if (hour > 18 && checkCount != 1) {
            //for testing stressed locations
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setCancelable(true);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setTitle("Stress Alert!");
            builder.setMessage("We noticed that you've been stressed today!");

            builder.setPositiveButton("VIEW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent in = new Intent(getApplicationContext(), StressedLocationsActivity.class);
                    startActivity(in);
                }
            });
            builder.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String[] options ={"1","2","3"};
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
                    builder1.setTitle("Remind me again in __ hour/s");
                    builder1.setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectedOption = options[i];
                        }
                    });
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d("OPTION----",selectedOption);

                            int newHour = hour + Integer.parseInt(selectedOption);
                            Log.d("NEW Hour----", String.valueOf(newHour));

                            //To save newHour & selected option
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("selectedOption",Integer.parseInt(selectedOption));
                            editor.putInt("newHour", newHour);
                            editor.commit();

                            dialogInterface.dismiss();
                        }
                    });
                    builder1.show();
                }
            });
            builder.show();

            //To save the first count
            int count =1;
            SharedPreferences sp = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
            SharedPreferences.Editor e = sp.edit();
            e.putInt("count", count);
            e.commit();
        }

        //access selected hour from shared preference
        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
        int newHour = sharedPreferences1.getInt("newHour", 0);
        int selectedHour = sharedPreferences1.getInt("selectedOption",0);
        Log.d("nHour SHARED---", String.valueOf(newHour));
        Log.d("selectedHour SHARED---", String.valueOf(selectedHour));

        SharedPreferences sha = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
        int checkCount1 = sha.getInt("count1", 0);
        //display skipped location popup according to selected hour
        if (newHour > 18 + selectedHour && checkCount1 !=2){
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setCancelable(true);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setTitle("Stress Alert!");
            builder.setMessage("We noticed that you've been stressed today!");

            builder.setNegativeButton("VIEW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent in = new Intent(getApplicationContext(), StressedLocationsActivity.class);
                    startActivity(in);
                }
            });
            builder.show();

            int count =2;
            SharedPreferences sp = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
            SharedPreferences.Editor e = sp.edit();
            e.putInt("count1", count);
            e.commit();
        }

        //get last day of the month to calculate
        int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

        //get today date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd");
        String date2 = simpleDateFormat2.format(calendar.getTime());
        Log.d("today DATE---", date2);
        Log.d("last DATE---", String.valueOf(lastDay));

        if (date2 == String.valueOf(lastDay)) {
            //Writing most stressed person to monthly text file in the end of every month
            try {
                fileName2 = new File(getCacheDir() + "/stressedPeopleMonthly.txt");
                //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                fileName2.createNewFile();
                if (!fileName2.exists()) {
                    fileName2.mkdirs();
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName2, true));

                SharedPreferences sp = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
                finalword = sp.getString("word", null);

                writer.write(finalword);
                writer.newLine();
                writer.flush();

                Log.d("TAG", "----------stressedPeopleMonthly File");
                //Toast.makeText(this, "Data has been written to stressedPeople File", Toast.LENGTH_SHORT).show();

                writer.close();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


//        Calendar cal = Calendar.getInstance();
//        Format f = new SimpleDateFormat("EEEE");
//        String today = f.format(new Date());
//
//        SharedPreferences shared= this.getSharedPreferences("com.example.calmable", 0);
//        String prevDay = shared.getString("prevDay", null);
//
//        Log.d("TODAY shared------", today);
//        //Log.d("PREVDAY shared-----", prevDay);
//
//        if(prevDay != today){
//            SharedPreferences.Editor ed = sharedPreferences.edit();
//            ed.putString("prevDay", today);
//            ed.commit();
//            removeDataFromPref();
//        }

        // clear necessary shared preferences daily after 10 pm
        if (hour > 21){
            removeDataFromPref();
        }



        NavigationBar();
        getData();
    }

    public void removeDataFromPref() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.calmable",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("count");
        editor.remove("count1");
        editor.remove("newHour");
        editor.remove("selectedOption");
        editor.apply();
        Log.d("TAG", "CLEARED SP-----");
    }


    //refresh activity
    private final Runnable m_Runnable = new Runnable() {
        public void run() {

            updateLandingHeartRate();


            updateLandingStressedIndex();

            //Toast.makeText(Home.this, "in runnable", Toast.LENGTH_SHORT).show();
            Home.this.mHandler.postDelayed(m_Runnable, 1000);
        }
    };


    //refresh stressed popup activity
    private final Runnable m_RunnableStressedPopup = new Runnable() {

        public void run() {

            Home.this.mHandlerStressedPopUp.postDelayed(m_RunnableStressedPopup, 1000);

        }

    };


//    //refresh pop up mage
//    private final Runnable m_Runnable_popup = new Runnable() {
//        public void run() {
//            updateLandingHeartRate();
//            if (finalRateff > 80) {
//                openDialog();
//            }
//            Toast.makeText(Home.this, "popup done!", Toast.LENGTH_SHORT).show();
//            Home.this.mHandler.postDelayed(m_Runnable, 10000);
//        }
//    };





    @Override
    protected void onPause() {

        // for HR refresh
        mHandler.removeCallbacks(m_Runnable);
        finish();

        // for stressed pop up refresh
        mHandlerStressedPopUp.removeCallbacks(m_RunnableStressedPopup);
        finish();


        //call thread method - get loaction
        stopThread = false;
        GetLocationRunnable runnable = new GetLocationRunnable();
        new Thread(runnable).start();

        super.onPause();
    }



    // show landing page heart rate
    public void updateLandingHeartRate() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
        finalRateff = sharedPreferences.getInt("heartRate", 0);
        txtProgress.setText(String.valueOf(finalRateff));
        String chr = getColoredSpanned(Integer.toString(finalRateff), "#800000");
        String BPM = getColoredSpanned("\u1D2E\u1D3E\u1D39", "#000000");
        txtProgress.setText(Html.fromHtml(chr + " " + BPM));

        //writeHRData();

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    // show landing page stressed index
    public void updateLandingStressedIndex() {

        SharedPreferences prefs = getSharedPreferences("com.example.calmable", MODE_PRIVATE);
        stressedIndex = prefs.getInt("stressedIndex", 0);
        streesIndexTV.setText(String.valueOf(stressedIndex));

        Date=simpleDateFormat.format(calendar.getTime());

        String uploadId = databaseReference.child("1").push().getKey();

        databaseReference.child("Date and Time").child(uploadId).setValue(Date);
        databaseReference.child("Index").child(uploadId).setValue(stressedIndex);

        stressedIndexAvg = prefs.getInt("stressedIndexAvg", 0);
        Log.d(TAG, "StressedIndex avg =  " + stressedIndexAvg);

    }


    // for collect data
    private void writeHRData() {
        // write heart rate to txt - start
        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
        timeAndHR = sharedPreferences1.getString("timeAndHR", "0");
        timeAndHR2 = sharedPreferences1.getString("timeAndHR2", "0");


        finalHR = timeAndHR;
        finalHR2 = timeAndHR2;
        String b = String.valueOf(markHeartRateValue);

        heartRateList = new ArrayList<>();
        heartRateList.add(finalHR2);
        heartRateList.add(finalHR);
        heartRateList.add(markHeartRate);
        //heartRateList.add(completeList);


        //Writing data to txt file
        try {
            filNameHeartRate = new File(getCacheDir() + "/heartRateReport1212.txt");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            filNameHeartRate.createNewFile();
            if (!filNameHeartRate.exists()) {
                filNameHeartRate.mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filNameHeartRate, true));
            int size = heartRateList.size();
            for (int i = 0; i < size; i++) {
                writer.write(heartRateList.get(i).toString());
                writer.newLine();
                writer.flush();
                //Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
            }

            writer.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        // write hr to txt - end


        // upload to firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage
        StorageReference storageReference1a = storageReference.child("users/" + mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1a.child("/heartRateReport1212.txt");
            InputStream stream = new FileInputStream(new File(filNameHeartRate.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // update coins in CALMable

//    public void updateLandingCoins() {
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference noteRef = db.collection("users")
//                .document(FirebaseAuth.getInstance().getUid());
//
//        noteRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            long title = documentSnapshot.getLong(COINS);
//
//                            String totalMusicCoins = String.valueOf(title);
//
//                            tvMusicCoins.setText(totalMusicCoins);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//    }


    @Override
    public void applyText(String person, String place, String reason) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        String date = simpleDateFormat1.format(calendar.getTime());

        int month=calendar.get(Calendar.MONTH)+1;
        Format f = new SimpleDateFormat("EEEE");
        String day = f.format(new Date());

        time = dateTime;
        viewPerson = person;
        viewPlace = place;
        viewReason = reason;

        Log.d("Date", date);
        Log.d("Time", time);
        Log.d("Person Value", viewPerson);
        Log.d("Place Value", viewPlace);
        Log.d("Reason Value", viewReason);

        HashMap<String, Object> Reports = new HashMap<>();
        List<Object> reportList = new ArrayList<>();
        reportList.add(date);
        reportList.add(time);
        reportList.add(viewPerson);
        reportList.add(viewPlace);
        reportList.add(viewReason);

        Log.d("----Array----", String.valueOf(reportList));

        //Writing stressed people data to text file
        try {
            fileName1 = new File(getCacheDir() + "/stressedPeople.txt");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            fileName1.createNewFile();
            if (!fileName1.exists()) {
                fileName1.mkdirs();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName1, true));

            writer.write(Home.viewPerson);
            writer.newLine();
            writer.flush();

            Log.d("TAG", "----------stressedPeople File");
            //Toast.makeText(this, "Data has been written to stressedPeople File", Toast.LENGTH_SHORT).show();

            writer.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        storageReference = FirebaseStorage.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        // Uploading file created to firebase storage
        StorageReference storageReference1a = storageReference.child("users/" + mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1a.child("stressedPeople.txt");
            InputStream stream = new FileInputStream(new File(fileName1.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportMonthly.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //get max word
        try {
            String line = "";
            word = "";
            int count = 0, maxCount = 0;
            ArrayList<String> words = new ArrayList<String>();

            //Opens file in read mode
            FileReader file = new FileReader(getCacheDir() + "/stressedPeople.txt");
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

            //To save
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("word", word);
            editor.commit();

            System.out.println("Most repeated word: " + word);


            //uploading most stressed person to realtime db
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getUid();
            FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("MostStressedPerson").setValue(word)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Home.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                    });

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //}


        //Writing data to file
        try {
            fileName = new File(getCacheDir() + "/reportStress.txt");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            fileName.createNewFile();
            if (!fileName.exists()) {
                fileName.mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            int size = reportList.size();
            for (int i = 0; i < size; i++) {
                writer.write(reportList.get(i).toString());
                writer.newLine();
                writer.flush();

                Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
            }
            writer.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // Uploading file created to firebase storage
        storageReference = FirebaseStorage.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        StorageReference storageReference1 = storageReference.child("users/" + mUser.getUid());
        try {
            StorageReference mountainsRef = storageReference1.child("reportStress.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ConcentrationReportDaily.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(ConcentrationReportDaily.this, "File Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Handler handler = new Handler();
        final int delay = 5000;

        //uploading reportList array values to firebase real time db-----

        //Reports.put("Reports", reportList);
        Report report = new Report(date,time,viewPerson,viewPlace,viewReason);
        FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(calendar.get(Calendar.YEAR)))
                .child(String.valueOf(month)).child(String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH))).child(day).push().setValue(report)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Home.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                });

        //uploading last report to firebase firestore
        database = FirebaseFirestore.getInstance();
        database.collection("users")
                .document(mUser.getUid())
                .set(Reports)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }


    //open are you stressed popup
    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Stress Alert!");
        builder.setMessage("Are you engaged in any physical activity?");

        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        //display getting user inputs popup
        builder.setPositiveButton("NO, I'm Stressed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PopUpOne popUpOne = new PopUpOne();
                popUpOne.show(getSupportFragmentManager(), "popup one");
            }
        });
        builder.show();
    }


    private void NavigationBar() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
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


    // landing page btn actions
    public void btnWatch(View view) {
        startActivity(new Intent(this, ScanActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnStressed(View view) {
        startActivity(new Intent(this, Relax.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnSleepy(View view) {
        startActivity(new Intent(getApplicationContext(), SleepyHome.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnMotivate(View view) {
        startActivity(new Intent(this, MotivateHome.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    public void btnBreath(View view) {
        startActivity(new Intent(this, BreathPatterns.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnReport(View view) {
        startActivity(new Intent(this, SearchReports.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnFitbitWatch(View view) {
        startActivity(new Intent(this, FitbitMainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnStartTest(View view) {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new file name ");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
        builder.setView(customLayout);

        // add a button
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // send data from the AlertDialog to the Activity
                        EditText editText = customLayout.findViewById(R.id.editText);
                        sendDialogDataToActivity(editText.getText().toString());
                    }
                });

        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        //Toast.makeText(Home.this, "Test started", Toast.LENGTH_SHORT).show();
    }

    // Do something with the data
    // coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {

        String newFileName = data + ".txt";
        Log.d("TAG", "New file name : " + newFileName);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
        timeAndHR = sharedPreferences1.getString("timeAndHR1", "0");
        String timeAndHRCompletedOne = sharedPreferences1.getString("timeAndHR", "0");


        //Log.d("TAG", "updateLandingHeartRate:  " + timeAndHR);

//        finalHR = timeAndHR;
        finalHRCompletedOne = timeAndHRCompletedOne;

        // add data to arraylist
        heartRateList = new ArrayList<>();
        //heartRateList.add(finalHR);
        heartRateList.add(finalHRCompletedOne);

        Log.d("TAG", "updateLandingHeartRate--------bbb-----: " + timeAndHRCompletedOne);

        //Writing data to file
        try {
            filNameHeartRate = new File(getCacheDir() + newFileName);
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            filNameHeartRate.createNewFile();
            if (!filNameHeartRate.exists()) {
                filNameHeartRate.mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filNameHeartRate, true));
            int size = heartRateList.size();
            for (int i = 0; i < size; i++) {
                writer.write(heartRateList.get(i).toString());
                writer.newLine();
                writer.flush();

                //Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
            }

            writer.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }


    // add mark
    public void btnAddPlus(View view) {

        markHeartRate++;
        markHeartRateValue.setText("" + markHeartRate);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("markValue1", markHeartRate);
        editor.commit();

//        Log.d("TAG", "btnAddPlus: " + markHeartRate);
        //Toast.makeText(getApplicationContext(), "Mark = " + markHeartRateValue, Toast.LENGTH_SHORT).show();
    }

    public void btnAddMinus(View view) {

        if (markHeartRate <= 0) markHeartRate = 0;

        else markHeartRate--;
        //Log.d("TAG", "btnAddMinus: " + markHeartRate);
        markHeartRateValue.setText("" + markHeartRate);
        //Toast.makeText(getApplicationContext(), "Minus 1", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("markValue2", markHeartRate);
        editor.commit();
    }

    // file download action
    public void btnDownloadTxtFile(View view) {

        storageReference = firebaseStorage.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        res = storageReference.child("users/" + mUser.getUid()).child("/heartRateReport1212.txt");
        res.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFiles(Home.this, "/heartRateReport1212", ".txt", DIRECTORY_DOWNLOADS, url);
                Toast.makeText(getApplicationContext(), "Check your downloads!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    // To download text file from firestore to device direct download
    public void downloadFiles(Context context, String fileNameX, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context
                .getSystemService(context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileNameX + fileExtension);

        downloadmanager.enqueue(request);
    }


    // To clear text file data
    public void btnClearTxtFile(View view) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(getCacheDir() + "/heartRateReport1212.txt");
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();

    }

    //call user name to home page
    private void getData() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("fullName").getValue(String.class);

                Log.d("TAG", "edit profile name : " + name);

                TextView textView = (TextView) findViewById(R.id.userName);
                textView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    /**
     *   call getLocation() method every 15 mins
     *   save every 15 mins current location
     */
    class GetLocationRunnable implements Runnable {
        @Override
        public void run() {

            for (int i = 0; i >= 0; i++) {

                //initialize fusedLocationProviderClient
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when permission granted
                    getLocation();
                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }


                try {
                    Thread.sleep(900000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopThread(View view) {
        stopThread = true;
    }


    // get current location
    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //initialize geocoder
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        //initialize address list
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //set address
                        userLocationFullAddress = addresses.get(0).getAddressLine(0);
                        //Log.d(TAG, "User current location address : " + userLocationFullAddress);

                        String str1 = String.valueOf(userLocationFullAddress);
                        //Removes the white spaces using regex.
                        str1 = str1.replaceAll("\\s+", "");

                        // take current time
                        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss", Locale.getDefault());
                        String currentDateandTime = sdf.format(new Date());
                        Log.d(TAG, "time---- : " + currentDateandTime);

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        LatLng place = new LatLng(latitude, longitude);
                        //Log.d("PLACE --------", String.valueOf(place));

                        //Creating a JSONObject object
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("address", userLocationFullAddress);
                        //jsonObject.put("index", place);
                        jsonObject.put("time", currentDateandTime);

                        //Log.d("Json array  : ", String.valueOf(jsonObject));

                        listOFLocations = new ArrayList<>();
                        // add json object to array
                        //listOFLocations.add(String.valueOf(jsonObject));
                        // add current location address to array
                        listOFLocations.add(str1);
                        Log.d(TAG, "Write array" + listOFLocations);


                        addLocationToDB();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }



    // write location address to txt file
    private void addLocationToDB() throws FileNotFoundException {

        //Writing data to txt file
        try {
            //File locationTxt = new File(getCacheDir() + "/serverData.txt");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            locationTxt.createNewFile();
            if (!locationTxt.exists()) {
                locationTxt.mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(locationTxt, true));
            int size = listOFLocations.size();
            for (int i = 0; i < size; i++) {
                writer.write(listOFLocations.get(i).toString());
                writer.newLine();
                writer.flush();
                //Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
            }
            writer.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        // write hr to txt - end


        // read txt file location data
        listOfTxtLocations = new ArrayList<String>();

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(getCacheDir() + "/locationData.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {
            listOfTxtLocations.add(scanner.next());
        }

        int arrSize = listOfTxtLocations.size();
        Log.d("TAG", "txt locations data --> : " + listOfTxtLocations);
        Log.d("TAG", "locations array size --> : " + arrSize);
        scanner.close();


        /**
         *    check past hour user stayed same place or not
         *    if same place it display in Profile(ProfileMain) --> Location Reminder(LocationReminderActivity)
         */
        if (arrSize == 4) {
            String val1 = listOfTxtLocations.get(0);
            String val2 = listOfTxtLocations.get(0);
            String val3 = listOfTxtLocations.get(0);
            String val4 = listOfTxtLocations.get(0);

//            Log.d(TAG, "writeHRData1: " + val1);
//            Log.d(TAG, "writeHRData2: " + val2);
//            Log.d(TAG, "writeHRData3: " + val3);
//            Log.d(TAG, "writeHRData4: " + val4);


            if (val1 == val2 && val2 == val3 && val3 == val4) {

                ArrayList<String> displayLocationsList = new ArrayList<>();

                // take current time
                SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss | dd-MM-yyyy ", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                //Log.d(TAG, "time---- : " + currentDateandTime);

                String saveLocation = val1;
                String saveDate = currentDateandTime;
                displayLocationsList.add(saveLocation);

                // add locations to locationDB
                HpyChtLocationDB myDB = new HpyChtLocationDB(Home.this);
                myDB.addHpyLocation(saveLocation , saveDate);

                Log.d(TAG, "save location ->" + displayLocationsList);

                // clear txt file
                PrintWriter writer;
                try {
                    writer = new PrintWriter(getCacheDir() + "/locationData.txt");
                    writer.print("");
                    writer.close();
                } catch (
                        FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}

