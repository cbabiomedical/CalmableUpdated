package com.example.calmable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.calmable.adapter.Calming396HzAdapter;
import com.example.calmable.adapter.ChillOutMusicAdapter;
import com.example.calmable.model.FavModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Calming396HzActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Calming396HzAdapter calming396HzAdapter;
    ArrayList<FavModel> listOfSongs;

    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming396_hz);

        recyclerView = findViewById(R.id.listOfSongRecycleView);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        initData();

    }

    private void initData() {

        listOfSongs = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Songs_Admin").child("Calming 396Hz");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postDataSnapshot : snapshot.getChildren()) {
                    FavModel post = postDataSnapshot.getValue(FavModel.class);
                    Log.d("Post", String.valueOf(post));
                    listOfSongs.add(post);
                }
                Log.d("List-->", String.valueOf(listOfSongs));

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                calming396HzAdapter = new Calming396HzAdapter(listOfSongs, getApplicationContext());
                recyclerView.setAdapter(calming396HzAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}