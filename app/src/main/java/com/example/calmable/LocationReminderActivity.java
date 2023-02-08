package com.example.calmable;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calmable.adapter.LocationAdapter;
import com.example.calmable.db.HpyChtLocationDB;
import com.example.calmable.sample.HpyChtPopUp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class LocationReminderActivity extends AppCompatActivity {


    RecyclerView locationRecyclerView;
    HpyChtLocationDB myDB;

    LocationAdapter locationAdapter;

    ArrayList<String> list_location_id, list_location_address, list_location_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_reminder);

        locationRecyclerView = findViewById(R.id.locationRecyclerView);

        TextView todayDateTv = (TextView) findViewById(R.id.todayDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        todayDateTv.setText(currentDateandTime);


        myDB = new HpyChtLocationDB(getApplicationContext());
        list_location_id = new ArrayList<>();
        list_location_address = new ArrayList<>();
        list_location_time = new ArrayList<>();


        storeDataInArrays();

//        // delete all data after 24h
//        if (currentDateandTime.equals(sdf.format(new Date()))){
//            storeDataInArrays();
//        } else{
//            HpyChtLocationDB myDB = new HpyChtLocationDB(getApplicationContext());
//            myDB.deleteAllData();
//            //Refresh Activity
//            Intent intent = new Intent(getApplicationContext(), ProfileMain.class);
//            startActivity(intent);
//            finish();
//        }


        locationAdapter = new LocationAdapter(getApplicationContext(),this, list_location_id, list_location_address, list_location_time);
        locationRecyclerView.setAdapter(locationAdapter);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    private void storeDataInArrays() {
        Cursor cursor = myDB.readAllLocationData();
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "No data!", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                list_location_id.add(cursor.getString(0));
                list_location_address.add(cursor.getString(1));
                list_location_time.add(cursor.getString(2));
            }
        }
    }

    public void btnGoBack (View view) {
        startActivity(new Intent(getApplicationContext() , ProfileMain.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void btnDeleteAll(View view) {
        confirmDialog();
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HpyChtLocationDB myDB = new HpyChtLocationDB(getApplicationContext());
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(getApplicationContext(), ProfileMain.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

}