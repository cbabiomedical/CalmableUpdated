package com.example.calmable.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class HpyChtReportDB extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "HpyChtReport.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_hpy_cht_report";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_EVENT = "event";
    private static final String COLUMN_RATE = "rate";
    private static final String COLUMN_DATE = "date";


    public HpyChtReportDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_EVENT + " TEXT, " +
                COLUMN_RATE + " INTEGER  );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void saveFinalLocation(String event, int rate , String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EVENT, event);
        cv.put(COLUMN_RATE, rate);
        cv.put(COLUMN_DATE, date);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllFinaData() {

//        Cursor cursor = db.query(my_hpy_cht_report, new String[] {_id, event,rate,date},
//                null, null, null, date + " ASC, " + semester  + " ASC");


        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_RATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
