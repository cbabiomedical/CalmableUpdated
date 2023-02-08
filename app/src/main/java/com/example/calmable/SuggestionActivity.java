package com.example.calmable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.calmable.adapter.CalmChartAdapter;
import com.example.calmable.adapter.SuggetionAdapter;
import com.example.calmable.model.CalmChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SuggestionActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    SuggetionAdapter suggetionAdapter;
    ArrayList<CalmChart> listOfCalmChrt;
    DatabaseReference databaseReference;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);


        recyclerView = findViewById(R.id.listOfSongRecycleView);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("CalmChart");


        initData();

        getDataId();

    }

    private void initData() {

        listOfCalmChrt = new ArrayList<>();
        Query reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("CalmChart")
                .orderByChild("timeToRelaxIndex");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    CalmChart calmChart = dataSnapshot.getValue(CalmChart.class);
                    listOfCalmChrt.add(calmChart);

                    for(int i = 0; i<listOfCalmChrt.size();i++)

                        Log.d("TAG", "calm chart data  : " + listOfCalmChrt);

                }

//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                calmChartAdapter = new CalmChartAdapter(getApplicationContext(), listOfCalmChrt);
//                recyclerView.setAdapter(calmChartAdapter);

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(mLayoutManager);
                suggetionAdapter = new SuggetionAdapter(listOfCalmChrt, getApplicationContext());
                recyclerView.setAdapter(suggetionAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //get songs id's
    private void getDataId() {

        Query reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("CalmChart").orderByChild("timeToRelaxIndex");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> listOfID = new ArrayList<>();

                for (DataSnapshot postDataSnapshot : snapshot.getChildren()) {

                    String post = postDataSnapshot.child("songName").getValue(String.class);
                    Log.d("Post --> ", String.valueOf(post));
                    listOfID.add(String.valueOf(post));
                }
                Log.d("list of songs id's -->", String.valueOf(listOfID));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void btnGoSugBreathing(View view) {
        startActivity(new Intent(getApplicationContext(), BreathPatterns.class));
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}