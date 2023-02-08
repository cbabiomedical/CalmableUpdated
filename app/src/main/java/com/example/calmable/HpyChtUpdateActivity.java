package com.example.calmable;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.calmable.db.HpyChtLocationDB;
import com.example.calmable.db.HpyChtReportDB;

public class HpyChtUpdateActivity extends AppCompatActivity {

    TextView locationAddressTV, timeTV, rateTV;
    EditText eventEd;
    SeekBar seekBarRate;

    String id, address, time, eventNote;
    int rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpy_cht_update);

        locationAddressTV = (TextView) findViewById(R.id.locationAddress);
        rateTV = (TextView) findViewById(R.id.rateTV);
        timeTV = (TextView) findViewById(R.id.time);
        eventEd = (EditText) findViewById(R.id.stressEvent);
        seekBarRate = (SeekBar) findViewById(R.id.happyRate);

        seekBarRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                rateTV.setText(String.valueOf(progress));
                rate = progress;
                Log.d("TAG", "onProgressChanged: " + rate);
                //Toast.makeText(getApplicationContext(), "seekbar progress: " + progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });

        getAndSetIntentData();


    }


    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("address") &&
                getIntent().hasExtra("time")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            address = getIntent().getStringExtra("address");
            time = getIntent().getStringExtra("time");

            //Setting Intent Data
            locationAddressTV.setText(address);
            timeTV.setText(time);

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }


    public void btnDeleteLocation(View view) {
        confirmDialog();
    }

    public void btnSubmit(View view) {

        eventNote = eventEd.getText().toString();
        int rateFinal = Integer.parseInt(rateTV.getText().toString());
        // add locations to locationDB
        HpyChtReportDB myDB = new HpyChtReportDB(HpyChtUpdateActivity.this);
        myDB.saveFinalLocation(eventNote, rateFinal, time);

        Log.d("TAG", "------------------- " + address);
        Log.d("TAG", "------------------- " + time);
        Log.d("TAG", "------------------- " + eventNote);
        Log.d("TAG", "------------------- " + rateFinal);


        Toast.makeText(getApplicationContext(), "Submit Done!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), LocationReminderActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        HpyChtLocationDB myDB1 = new HpyChtLocationDB(HpyChtUpdateActivity.this);
        myDB1.deleteOneRow(id);
        finish();

//        Intent startIntent = new Intent(getApplicationContext(), HpyChtReportActivity.class);
//        startIntent.putExtra("com.example.willh.seekbar.something", rateFinal);
//        startActivity(startIntent);

    }


    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure ?");
        builder.setMessage("You are not stressed there?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HpyChtLocationDB myDB = new HpyChtLocationDB(HpyChtUpdateActivity.this);
                myDB.deleteOneRow(id);
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

    public void btnClose (View view) {
        startActivity(new Intent(getApplicationContext() , LocationReminderActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
