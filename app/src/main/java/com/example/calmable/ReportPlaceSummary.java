package com.example.calmable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ReportPlaceSummary extends AppCompatActivity  {

    LineChart lineChart;
    FirebaseUser mUser;
    private Random random;
    private TableLayout table;
    private Context context;
    TextView place1, place2, place3, place4, place5, place6, place7, place8, place9, place10;
    TextView time1, time2, time3, time4, time5, time6, time7, time8, time9, time10;
    //LinearLayout MainLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_place_summary);

        //pieChart = findViewById(R.id.pieChart_view);
        //lineChart = findViewById(R.id.lineChartDaily);


        place1 = (TextView) findViewById(R.id.name1);
        place2 = (TextView) findViewById(R.id.name2);
        place3 = (TextView) findViewById(R.id.name3);
        place4 = (TextView) findViewById(R.id.name4);
        place5 = (TextView) findViewById(R.id.name5);
        place6 = (TextView) findViewById(R.id.name6);
        place7 = (TextView) findViewById(R.id.name7);
        place8 = (TextView) findViewById(R.id.name8);
        place9 = (TextView) findViewById(R.id.name9);
        place10 = (TextView) findViewById(R.id.name10);

        time1 = (TextView) findViewById(R.id.time1);
        time2 = (TextView) findViewById(R.id.time2);
        time3 = (TextView) findViewById(R.id.time3);
        time4 = (TextView) findViewById(R.id.time4);
        time5 = (TextView) findViewById(R.id.time5);
        time6 = (TextView) findViewById(R.id.time6);
        time7 = (TextView) findViewById(R.id.time7);
        time8 = (TextView) findViewById(R.id.time8);
        time9 = (TextView) findViewById(R.id.time9);
        time10 = (TextView) findViewById(R.id.time10);


        //LinearLayout mainLayout = findViewById(R.id.mainLayout);
        //mainLayout.addView(table);

        random = new Random();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        final Handler handler = new Handler();
        final int delay = 7000;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getEntries();
//        openDialog();
    }

    private void getEntries(){


        Handler handler = new Handler();
        final int delay = 5000;


        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("---WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("---MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("---YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("---DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("---DayName", str);

        int month = now.get(Calendar.MONTH)+1;

        NavigationBar();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //List<String> list = new ArrayList<String>();

                ArrayList element = new ArrayList();
                ArrayList element1 = new ArrayList();
                Log.d("---Ele1", String.valueOf(element1));

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");

                Log.d("-------runnn------","runnn4");
                Log.d("---Children1", String.valueOf(month));

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //ArrayList element = new ArrayList();
                        //ArrayList element1= new ArrayList();


                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("---------runmm--------","runmm4");
                            Log.d("Children", String.valueOf(dataSnapshot.getValue()));
                            //for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                Log.d("-----log-------", String.valueOf(dataSnapshot1.toString()));

                                if (dataSnapshot1.getKey().equals("place")) {
                                    dataSnapshot1.getValue();
                                    Log.d("------Monday-----", "MondayPlace");
                                    Log.d("------places-----", String.valueOf(dataSnapshot1.getValue()));

                                    element1.add(dataSnapshot1.getValue());
                                    Log.d("-------elements--------", String.valueOf(element1));
                                    Log.d("------Monday-----", "MondayPlace1");
                                }


                                //Log.d("-------elements--------", String.valueOf(dataSnapshot1.child("person").getValue()));
                                //}
                            }

                        }

                        //for tuesdays
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");



                        reference2.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                ArrayList element1 = new ArrayList();
                                //ArrayList element = new ArrayList();
                                Log.d("------runnn5------","runnn5");

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Log.d("---------run--------","run4");
                                    Log.d("Children", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        Log.d("-----log-------",String.valueOf(dataSnapshot1.toString()));

                                        if (dataSnapshot1.getKey().equals("place")) {
                                            dataSnapshot1.getValue();
                                            Log.d("------places-----", String.valueOf(dataSnapshot1.getValue()));

                                            element1.add(dataSnapshot1.getValue());
                                            Log.d("-------elements--------", String.valueOf(element1));
                                        }


                                        //Log.d("-------elements--------", String.valueOf(dataSnapshot1.child("person").getValue()));
                                    }

                                }

                                //for wednesdays
                                DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");



                                reference3.addValueEventListener(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        ArrayList element1 = new ArrayList();


                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Log.d("---------run--------","run4");
                                            Log.d("Children", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                Log.d("-----log-------",String.valueOf(dataSnapshot1.toString()));

                                                if (dataSnapshot1.getKey().equals("place")) {
                                                    dataSnapshot1.getValue();
                                                    Log.d("------places-----", String.valueOf(dataSnapshot1.getValue()));

                                                    element1.add(dataSnapshot1.getValue());
                                                    Log.d("-------elements--------", String.valueOf(element1));
                                                }


                                                //Log.d("-------elements--------", String.valueOf(dataSnapshot1.child("person").getValue()));
                                            }

                                        }

                                        //for thursdays
                                        DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");



                                        reference4.addValueEventListener(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.N)
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                ArrayList element1 = new ArrayList();


                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    Log.d("---------run--------","run4");
                                                    Log.d("Children", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                        Log.d("-----log-------",String.valueOf(dataSnapshot1.toString()));

                                                        if (dataSnapshot1.getKey().equals("place")) {
                                                            dataSnapshot1.getValue();
                                                            Log.d("------places-----", String.valueOf(dataSnapshot1.getValue()));

                                                            element1.add(dataSnapshot1.getValue());
                                                            Log.d("-------elements--------", String.valueOf(element1));
                                                        }


                                                        //Log.d("-------elements--------", String.valueOf(dataSnapshot1.child("person").getValue()));
                                                    }

                                                }

                                                //for tuesdays
                                                DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");



                                                reference5.addValueEventListener(new ValueEventListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        ArrayList element1 = new ArrayList();


                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Log.d("---------run--------","run4");
                                                            Log.d("Children", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                                Log.d("-----log-------",String.valueOf(dataSnapshot1.toString()));

                                                                if (dataSnapshot1.getKey().equals("place")) {
                                                                    dataSnapshot1.getValue();
                                                                    Log.d("------places-----", String.valueOf(dataSnapshot1.getValue()));

                                                                    element1.add(dataSnapshot1.getValue());
                                                                    Log.d("-------elements--------", String.valueOf(element1));
                                                                }


                                                                //Log.d("-------elements--------", String.valueOf(dataSnapshot1.child("person").getValue()));
                                                            }

                                                        }

                                                        //for saturdays
                                                        DatabaseReference reference6 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");



                                                        reference6.addValueEventListener(new ValueEventListener() {
                                                            @RequiresApi(api = Build.VERSION_CODES.N)
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                ArrayList element1 = new ArrayList();


                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                    Log.d("---------run--------","run4");
                                                                    Log.d("Children", String.valueOf(dataSnapshot.getValue()));
                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                                        Log.d("-----log-------",String.valueOf(dataSnapshot1.toString()));

                                                                        if (dataSnapshot1.getKey().equals("place")) {
                                                                            dataSnapshot1.getValue();
                                                                            Log.d("------places-----", String.valueOf(dataSnapshot1.getValue()));

                                                                            element1.add(dataSnapshot1.getValue());
                                                                            Log.d("-------elements--------", String.valueOf(element1));
                                                                        }


                                                                        //Log.d("-------elements--------", String.valueOf(dataSnapshot1.child("person").getValue()));
                                                                    }

                                                                }

                                                                //for sundays
                                                                DatabaseReference reference7 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");



                                                                reference7.addValueEventListener(new ValueEventListener() {
                                                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                        ArrayList element1 = new ArrayList();


                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                            Log.d("---------run--------","run4");
                                                                            Log.d("Children", String.valueOf(dataSnapshot.getValue()));
                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                                                Log.d("-----log-------",String.valueOf(dataSnapshot1.toString()));

                                                                                if (dataSnapshot1.getKey().equals("place")) {
                                                                                    dataSnapshot1.getValue();
                                                                                    Log.d("------places-----", String.valueOf(dataSnapshot1.getValue()));

                                                                                    element1.add(dataSnapshot1.getValue());
                                                                                    Log.d("-------elements--------", String.valueOf(element1));
                                                                                }


                                                                                //Log.d("-------elements--------", String.valueOf(dataSnapshot1.child("person").getValue()));
                                                                            }

                                                                        }


//                                                                    ArrayList fr = new ArrayList(element.size());
//                                                                    int v = -1;
//                                                                    for(int i = 0; i < element.size(); i++){
//                                                                        int count = 1;
//                                                                        for(int j = i+1; j < element.size(); j++){
//                                                                            if(element.get(i) == element.get(j)){
//                                                                                count++;
//                                                                                //To avoid counting same element again
                                                                        //fr[j] = v;
//
//                                                                                element.size() = v;
//                                                                            }
//                                                                        }
//                                                                        if(fr[i] != v)
//                                                                            fr[i] = count;
//                                                                    }

//                                                                    for (int i = 0; i < element.size(); i++){
//                                                                        int freq = Collections.frequency(element,element.get(i));
//                                                                        Log.d("--------frequency-----", String.valueOf(freq));
//                                                                    }


                                                                        Set<String> uniqueSet = new HashSet<String>(element1);
                                                                        for (String i  : uniqueSet){
                                                                            Log.d("------sorted fre----", String.valueOf(Collections.frequency(element1, i)));
                                                                            Log.d("------sorted place----", String.valueOf(i));
                                                                            // Converting HashSet to Array
                                                                            String[] Places = uniqueSet.toArray(new String[uniqueSet.size()]);
                                                                            //String[] Freq = uniqueSet.toArray(new String[Collections.frequency(element, i)]);

                                                                            // Accessing elements by index


                                                                            if (uniqueSet.size() == 1){
                                                                                Log.d("------1st place----", Places[0]);
                                                                                place1.setText(Places[0]);


                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                            }
                                                                            else if (uniqueSet.size() == 2){
                                                                                Log.d("------1st person----", Places[0]);
                                                                                place1.setText(Places[0]);
                                                                                Log.d("------2nd person----", Places[1]);
                                                                                place2.setText(Places[1]);

                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
                                                                            }
                                                                            else if (uniqueSet.size() == 3){
                                                                                Log.d("------1st person----", Places[0]);
                                                                                place1.setText(Places[0]);
                                                                                Log.d("------2nd person----", Places[1]);
                                                                                place2.setText(Places[1]);
                                                                                Log.d("------3rd person----", Places[2]);
                                                                                place3.setText(Places[2]);

                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                Log.d("------3rd freq----", String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                time3.setText(String.valueOf(Collections.frequency(element1, Places[2])));
                                                                            }
                                                                            else if (uniqueSet.size() == 4){
                                                                                Log.d("------1st person----", Places[0]);
                                                                                place1.setText(Places[0]);
                                                                                Log.d("------2nd person----", Places[1]);
                                                                                place2.setText(Places[1]);
                                                                                Log.d("------3rd person----", Places[2]);
                                                                                place3.setText(Places[2]);
                                                                                Log.d("------4th person----", Places[3]);
                                                                                place4.setText(Places[3]);

                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                Log.d("------3rd freq----", String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                time3.setText(String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                Log.d("------4th freq----", String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                time4.setText(String.valueOf(Collections.frequency(element1, Places[3])));
                                                                            }
                                                                            else if (uniqueSet.size() == 5){
                                                                                Log.d("------1st person----", Places[0]);
                                                                                place1.setText(Places[0]);
                                                                                Log.d("------2nd person----", Places[1]);
                                                                                place2.setText(Places[1]);
                                                                                Log.d("------3rd person----", Places[2]);
                                                                                place3.setText(Places[2]);
                                                                                Log.d("------4th person----", Places[3]);
                                                                                place4.setText(Places[3]);
                                                                                Log.d("------4th person----", Places[4]);
                                                                                place5.setText(Places[4]);

                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                Log.d("------3rd freq----", String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                time3.setText(String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                Log.d("------4th freq----", String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                time4.setText(String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                Log.d("------4th freq----", String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                time5.setText(String.valueOf(Collections.frequency(element1, Places[4])));
                                                                            }
                                                                            else if (uniqueSet.size() == 6){
                                                                                Log.d("------1st person----", Places[0]);
                                                                                place1.setText(Places[0]);
                                                                                Log.d("------2nd person----", Places[1]);
                                                                                place2.setText(Places[1]);
                                                                                Log.d("------3rd person----", Places[2]);
                                                                                place3.setText(Places[2]);
                                                                                Log.d("------4th person----", Places[3]);
                                                                                place4.setText(Places[3]);
                                                                                Log.d("------5th person----", Places[4]);
                                                                                place5.setText(Places[4]);
                                                                                Log.d("------6th person----", Places[5]);
                                                                                place6.setText(Places[5]);

                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                Log.d("------3rd freq----", String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                time3.setText(String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                Log.d("------4th freq----", String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                time4.setText(String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                Log.d("------5th freq----", String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                time5.setText(String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                Log.d("------6th freq----", String.valueOf(Collections.frequency(element1, Places[5])));
                                                                                time6.setText(String.valueOf(Collections.frequency(element1, Places[5])));
                                                                            }
                                                                            else if (uniqueSet.size() == 7){
                                                                                Log.d("------1st person----", Places[0]);
                                                                                place1.setText(Places[0]);
                                                                                Log.d("------2nd person----", Places[1]);
                                                                                place2.setText(Places[1]);
                                                                                Log.d("------3rd person----", Places[2]);
                                                                                place3.setText(Places[2]);
                                                                                Log.d("------4th person----", Places[3]);
                                                                                place4.setText(Places[3]);
                                                                                Log.d("------5th person----", Places[4]);
                                                                                place5.setText(Places[4]);
                                                                                Log.d("------6th person----", Places[5]);
                                                                                place6.setText(Places[5]);
                                                                                Log.d("------7th person----", Places[6]);
                                                                                place7.setText(Places[5]);

                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                Log.d("------3rd freq----", String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                time3.setText(String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                Log.d("------4th freq----", String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                time4.setText(String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                Log.d("------5th freq----", String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                time5.setText(String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                Log.d("------6th freq----", String.valueOf(Collections.frequency(element1, Places[5])));
                                                                                time6.setText(String.valueOf(Collections.frequency(element1, Places[5])));
                                                                                Log.d("------7th freq----", String.valueOf(Collections.frequency(element1, Places[6])));
                                                                                time7.setText(String.valueOf(Collections.frequency(element1, Places[6])));
                                                                            }
                                                                            else if (uniqueSet.size() == 8){
                                                                                Log.d("------1st person----", Places[0]);
                                                                                place1.setText(Places[0]);
                                                                                Log.d("------2nd person----", Places[1]);
                                                                                place2.setText(Places[1]);
                                                                                Log.d("------3rd person----", Places[2]);
                                                                                place3.setText(Places[2]);
                                                                                Log.d("------4th person----", Places[3]);
                                                                                place4.setText(Places[3]);
                                                                                Log.d("------5th person----", Places[4]);
                                                                                place5.setText(Places[4]);
                                                                                Log.d("------6th person----", Places[5]);
                                                                                place6.setText(Places[5]);
                                                                                Log.d("------7th person----", Places[6]);
                                                                                place7.setText(Places[6]);
                                                                                Log.d("------8th person----", Places[7]);
                                                                                place8.setText(Places[7]);

                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                Log.d("------3rd freq----", String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                time3.setText(String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                Log.d("------4th freq----", String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                time4.setText(String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                Log.d("------5th freq----", String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                time5.setText(String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                Log.d("------6th freq----", String.valueOf(Collections.frequency(element1, Places[5])));
                                                                                time6.setText(String.valueOf(Collections.frequency(element1, Places[5])));
                                                                                Log.d("------7th freq----", String.valueOf(Collections.frequency(element1, Places[6])));
                                                                                time7.setText(String.valueOf(Collections.frequency(element1, Places[6])));
                                                                                Log.d("------8th freq----", String.valueOf(Collections.frequency(element1, Places[7])));
                                                                                time8.setText(String.valueOf(Collections.frequency(element1, Places[7])));
                                                                            }
                                                                            else if (uniqueSet.size() == 9){
                                                                                Log.d("------1st person----", Places[0]);
                                                                                place1.setText(Places[0]);
                                                                                Log.d("------2nd person----", Places[1]);
                                                                                place2.setText(Places[1]);
                                                                                Log.d("------3rd person----", Places[2]);
                                                                                place3.setText(Places[2]);
                                                                                Log.d("------4th person----", Places[3]);
                                                                                place4.setText(Places[3]);
                                                                                Log.d("------5th person----", Places[4]);
                                                                                place5.setText(Places[4]);
                                                                                Log.d("------6th person----", Places[5]);
                                                                                place6.setText(Places[5]);
                                                                                Log.d("------7th person----", Places[6]);
                                                                                place7.setText(Places[6]);
                                                                                Log.d("------8th person----", Places[7]);
                                                                                place8.setText(Places[7]);
                                                                                Log.d("------9th person----", Places[8]);
                                                                                place9.setText(Places[8]);

                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                Log.d("------3rd freq----", String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                time3.setText(String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                Log.d("------4th freq----", String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                time4.setText(String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                Log.d("------5th freq----", String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                time5.setText(String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                Log.d("------6th freq----", String.valueOf(Collections.frequency(element1, Places[5])));
                                                                                time6.setText(String.valueOf(Collections.frequency(element1, Places[5])));
                                                                                Log.d("------7th freq----", String.valueOf(Collections.frequency(element1, Places[6])));
                                                                                time7.setText(String.valueOf(Collections.frequency(element1, Places[6])));
                                                                                Log.d("------8th freq----", String.valueOf(Collections.frequency(element1, Places[7])));
                                                                                time8.setText(String.valueOf(Collections.frequency(element1, Places[7])));
                                                                                Log.d("------9th freq----", String.valueOf(Collections.frequency(element1, Places[8])));
                                                                                time9.setText(String.valueOf(Collections.frequency(element1, Places[8])));
                                                                            }
                                                                            else if (uniqueSet.size() == 0){
                                                                                openDialog();
                                                                            }
                                                                            else {
                                                                                Log.d("------1st person----", Places[0]);
                                                                                place1.setText(Places[0]);
                                                                                Log.d("------2nd person----", Places[1]);
                                                                                place2.setText(Places[1]);
                                                                                Log.d("------3rd person----", Places[2]);
                                                                                place3.setText(Places[2]);
                                                                                Log.d("------4th person----", Places[3]);
                                                                                place4.setText(Places[3]);
                                                                                Log.d("------5th person----", Places[4]);
                                                                                place5.setText(Places[4]);
                                                                                Log.d("------6th person----", Places[5]);
                                                                                place6.setText(Places[5]);
                                                                                Log.d("------7th person----", Places[6]);
                                                                                place7.setText(Places[6]);
                                                                                Log.d("------8th person----", Places[7]);
                                                                                place8.setText(Places[7]);
                                                                                Log.d("------9th person----", Places[8]);
                                                                                place9.setText(Places[8]);
                                                                                Log.d("------10th person----", Places[9]);
                                                                                place10.setText(Places[9]);

                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
                                                                                Log.d("------3rd freq----", String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                time3.setText(String.valueOf(Collections.frequency(element1, Places[2])));
                                                                                Log.d("------4th freq----", String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                time4.setText(String.valueOf(Collections.frequency(element1, Places[3])));
                                                                                Log.d("------5th freq----", String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                time5.setText(String.valueOf(Collections.frequency(element1, Places[4])));
                                                                                Log.d("------6th freq----", String.valueOf(Collections.frequency(element1, Places[5])));
                                                                                time6.setText(String.valueOf(Collections.frequency(element1, Places[5])));
                                                                                Log.d("------7th freq----", String.valueOf(Collections.frequency(element1, Places[6])));
                                                                                time7.setText(String.valueOf(Collections.frequency(element1, Places[6])));
                                                                                Log.d("------8th freq----", String.valueOf(Collections.frequency(element1, Places[7])));
                                                                                time8.setText(String.valueOf(Collections.frequency(element1, Places[7])));
                                                                                Log.d("------9th freq----", String.valueOf(Collections.frequency(element1, Places[8])));
                                                                                time9.setText(String.valueOf(Collections.frequency(element1, Places[8])));
                                                                                Log.d("------10th freq----", String.valueOf(Collections.frequency(element1, Places[9])));
                                                                                time10.setText(String.valueOf(Collections.frequency(element1, Places[9])));
                                                                            }


//                                                                                Log.d("------1st person----", Places[0]);
//                                                                                place1.setText(Places[0]);
//                                                                                Log.d("------2nd person----", Places[1]);
//                                                                                place2.setText(Places[1]);
//                                                                                Log.d("------3rd person----", Places[2]);
//                                                                                place3.setText(Places[2]);
//                                                                                Log.d("------4th person----", Places[3]);
//                                                                                place4.setText(Places[3]);
//
//                                                                                Log.d("------1st freq----", String.valueOf(Collections.frequency(element1, Places[0])));
//                                                                                time1.setText(String.valueOf(Collections.frequency(element1, Places[0])));
//                                                                                Log.d("------2nd freq----", String.valueOf(Collections.frequency(element1, Places[1])));
//                                                                                time2.setText(String.valueOf(Collections.frequency(element1, Places[1])));
//                                                                                Log.d("------3rd freq----", String.valueOf(Collections.frequency(element1, Places[2])));
//                                                                                time3.setText(String.valueOf(Collections.frequency(element1, Places[2])));
//                                                                                Log.d("------4th freq----", String.valueOf(Collections.frequency(element1, Places[3])));
//                                                                                time4.setText(String.valueOf(Collections.frequency(element1, Places[3])));


                                                                        }


//                                                                        for (int i=0; i < element1.size(); i++) {
//                                                                            TableRow row = new TableRow(ReportSummary.this);
//                                                                            for (int j=0; j < 2; j++) {
//                                                                                int value = random.nextInt(100) + 1;
//                                                                                TextView tv = new TextView(ReportSummary.this);
//                                                                                tv.setText(String.valueOf(value));
//                                                                                row.addView(tv);
//                                                                            }
//                                                                            table.addView(row);
//                                                                        }
                                                                        //mainLayout.addView(table);


                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });


                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }, 3000);
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReportPlaceSummary.this);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Alert!");

        builder.setMessage("No data to show !");

//        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//            }
//        });

        //to go back
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), ReportHome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

    public void btnGoPersonChart(View view) {
        startActivity(new Intent(getApplicationContext(), ReportSummary.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnGoTimeChart(View view) {
        startActivity(new Intent(getApplicationContext(), ReportTimeSummary.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}


