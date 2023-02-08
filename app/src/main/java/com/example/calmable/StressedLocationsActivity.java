package com.example.calmable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calmable.adapter.StressedLocationAdapter;
import com.example.calmable.db.StressedLocationsDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StressedLocationsActivity extends AppCompatActivity {

    RecyclerView locationRecyclerView;
    StressedLocationsDB myDB;
    ArrayList<String>list_location_id, list_location_address, list_location_time;
    StressedLocationAdapter stressedLocationAdapter;
    TextView noDataTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stressed_locations);

        noDataTv = (TextView) findViewById(R.id.noData);

        locationRecyclerView = findViewById(R.id.locationRecyclerView);

        TextView todayDateTv = (TextView) findViewById(R.id.tvDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        todayDateTv.setText(currentDate);

        myDB = new StressedLocationsDB(getApplicationContext());
        list_location_id = new ArrayList<>();
        list_location_address = new ArrayList<>();
        list_location_time = new ArrayList<>();

        storeDataInArrays();

        stressedLocationAdapter = new StressedLocationAdapter(getApplicationContext(),this, list_location_id, list_location_address, list_location_time);
        locationRecyclerView.setAdapter(stressedLocationAdapter);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void storeDataInArrays() {
        Cursor cursor = myDB.readAllLocationData();
        if(cursor.getCount() == 0){

            noDataTv.setVisibility(View.VISIBLE);

            Toast.makeText(getApplicationContext(), "No data!", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                noDataTv.setVisibility(View.INVISIBLE);

                list_location_id.add(cursor.getString(0));
                list_location_address.add(cursor.getString(1));
                list_location_time.add(cursor.getString(2));
            }
        }
    }

    // for go back
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}