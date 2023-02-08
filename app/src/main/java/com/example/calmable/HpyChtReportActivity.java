package com.example.calmable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.calmable.adapter.HpyChtReportAdapter;
import com.example.calmable.db.HpyChtReportDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

public class HpyChtReportActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    HpyChtReportDB myDB;
    ArrayList<String> list_location_id, list_event, list_rate,list_time;
    HpyChtReportAdapter hpyChtReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpy_cht_report);

        recyclerView = findViewById(R.id.HpyChtReportRV);

        myDB = new HpyChtReportDB(HpyChtReportActivity.this);

        list_location_id = new ArrayList<>();
        list_event = new ArrayList<>();
        list_rate = new ArrayList<>();
        list_time = new ArrayList<>();



        storeDataInArrays();

        hpyChtReportAdapter = new HpyChtReportAdapter(HpyChtReportActivity.this,this, list_location_id, list_event,list_rate,list_time);
        recyclerView.setAdapter(hpyChtReportAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HpyChtReportActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    private void storeDataInArrays() {
        Cursor cursor = myDB.readAllFinaData();
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "No Data!", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){

                list_location_id.add(cursor.getString(0));
                list_event.add(cursor.getString(3));
                list_rate.add(cursor.getString(1));
                list_time.add(cursor.getString(2));

                //Collections.sort(list_event);

                Log.d("TAG", "---id------****---------"+list_location_id);
                Log.d("TAG", "---event------****---------"+list_event);
                Log.d("TAG", "---rate------****---------"+list_rate);
                Log.d("TAG", "---time-----****---------"+list_time);

                Log.d("TAG", "+++++++++++: " + list_event);


            }
        }
    }



//    public void OrderTable() {
//
//
//        try {
//            String sql = "select sdfs as 'sdfsfID' . name as 'name' , surname as 'My Surname', age as 'AGE' from EmpInfo order by ";
//            Object conn;
//            PreparedStatement pst = conn.pripareStatement(sql);
//            ResultSet rs = pst.executeQuery();
//            Table_E mp.setModel(DbUtils.resultSetToTableModel(rs));
//        } catch (Exception e){
//
//        }
//
//    }
}