package com.example.calmable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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

public class StressedNotes extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter1 myAdapter1;
    ArrayList<Note1> list1;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stressed_notes);

        NavigationBar();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("---WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("---MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("---YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("---DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());

        System.out.println("Day Name: " + str);
        Log.d("---DayName", str);

        int month = now.get(Calendar.MONTH)+1;

        recyclerView = findViewById(R.id.noteList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list1 = new ArrayList<>();
        myAdapter1 = new MyAdapter1(this, list1);
        recyclerView.setAdapter(myAdapter1);

        //from monday
        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Monday");


        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Note1 note1 = dataSnapshot.getValue(Note1.class);
                    list1.add(note1);

                }
                myAdapter1.notifyDataSetChanged();

                //from tuesday
                DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Tuesday");

                database2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Note1 note1 = dataSnapshot.getValue(Note1.class);
                            list1.add(note1);

                        }
                        myAdapter1.notifyDataSetChanged();

                        //from wednesday
                        DatabaseReference database3 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Wednesday");

                        database3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Note1 note1 = dataSnapshot.getValue(Note1.class);
                                    list1.add(note1);

                                }
                                myAdapter1.notifyDataSetChanged();


                                DatabaseReference database4 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Thursday");

                                database4.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Note1 note1 = dataSnapshot.getValue(Note1.class);
                                            list1.add(note1);

                                        }
                                        myAdapter1.notifyDataSetChanged();

                                        DatabaseReference database5 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Friday");

                                        database5.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Note1 note1 = dataSnapshot.getValue(Note1.class);
                                                    list1.add(note1);

                                                }
                                                myAdapter1.notifyDataSetChanged();

                                                DatabaseReference database6 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Saturday");

                                                database6.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                            Note1 note1 = dataSnapshot.getValue(Note1.class);
                                                            list1.add(note1);

                                                        }
                                                        myAdapter1.notifyDataSetChanged();

                                                        DatabaseReference database7 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(now.get(Calendar.YEAR)))
                                                                .child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child("Sunday");

                                                        database7.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                    Note1 note1 = dataSnapshot.getValue(Note1.class);
                                                                    list1.add(note1);

                                                                }
                                                                myAdapter1.notifyDataSetChanged();

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
        Intent intent = new Intent(getApplicationContext(), Journal.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}