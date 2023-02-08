package com.example.calmable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calmable.adapter.SuggestionSimpleCursorAdapter;
import com.example.calmable.db.StressedLocationsDB;
import com.example.calmable.db.SuggestionDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GetSkippedStressedDetails extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener{

    TextView editTime,editPlace;
    EditText editPerson,editReason;
    Button submit;
    String id, address, time;
    File fileName;
    public static String word;
    FirebaseUser mUser;

    private SuggestionDatabase database;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_skipped_stressed_details);

        editPerson = findViewById(R.id.edit_person);
        editPlace  = findViewById(R.id.edit_place);
        editReason = findViewById(R.id.edit_reason);
        editTime   = findViewById(R.id.locationTime);
        submit     = findViewById(R.id.submit);

        database = new SuggestionDatabase(this);
        searchView = findViewById(R.id.ac_text_view);
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        searchView.setOnSuggestionListener((SearchView.OnSuggestionListener) this);

        getAndSetIntentData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String date = simpleDateFormat.format(calendar.getTime());
                int month=calendar.get(Calendar.MONTH)+1;
                Format f = new SimpleDateFormat("EEEE");
                String day = f.format(new Date());

                editPerson.setText(searchView.getQuery());

                String person = editPerson.getText().toString();
                String place = editPlace.getText().toString();
                String reason = editReason.getText().toString();
                String time = editTime.getText().toString();

                //check if person and reason entered
                if(reason.isEmpty() || person.isEmpty()){
                    editReason.setError("Both Fields are Required");
                    editReason.requestFocus();
                    return;
                }

                Log.d("SKIP stressed date-----", date);
                Log.d("SKIP time-----", time);
                Log.d("SKIP person-----", person);
                Log.d("SKIP location-----", place);
                Log.d("SKIP reason-----", reason);

                HashMap<String, Object> Reports = new HashMap<>();
                List<Object> reportList = new ArrayList<>();
                reportList.add(date);
                reportList.add(time);
                reportList.add(person);
                reportList.add(place);
                reportList.add(reason);

                Log.d("----Array----", String.valueOf(reportList));

                //Writing stressed people data to text file
                try {
                    fileName = new File(getCacheDir() + "/stressedPeople.txt");
                    //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                    fileName.createNewFile();
                    if (!fileName.exists()) {
                        fileName.mkdirs();
                    }

                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));

                    writer.write(person);
                    writer.newLine();
                    writer.flush();

                    Log.d("TAG", "----------stressedPeople File");
                    //Toast.makeText(this, "Data has been written to stressedPeople File", Toast.LENGTH_SHORT).show();

                    writer.close();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
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
                                    Toast.makeText(GetSkippedStressedDetails.this, "Successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                    br.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //uploading reportList array values to firebase real time db-----
                Report report = new Report(date,time,person,place,reason);
                FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("reportStress").child(String.valueOf(calendar.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH))).child(day).push().setValue(report)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(GetSkippedStressedDetails.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        });

                //delete row data
                StressedLocationsDB myDB = new StressedLocationsDB(GetSkippedStressedDetails.this);
                myDB.deleteOneRow(id);

                startActivity(new Intent(GetSkippedStressedDetails.this,StressedLocationsActivity.class));
            }
        });
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("address") &&
                getIntent().hasExtra("time")) {

            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            address = getIntent().getStringExtra("address");
            time = getIntent().getStringExtra("time");

            Log.d("TAG", "getAndSetIntentData: " + time);

            //Setting Intent Data
            editPlace.setText(address);
            editTime.setText(time);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        long result = database.insertSuggestion(query);
        //editPerson.setText(searchView.getQuery());
        return result != -1;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Cursor cursor = database.getSuggestions(newText);
        if(cursor.getCount() != 0)
        {
            String[] columns = new String[] {SuggestionDatabase.FIELD_SUGGESTION };
            int[] columnTextId = new int[] { android.R.id.text1};

            SuggestionSimpleCursorAdapter simple = new SuggestionSimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1, cursor,
                    columns , columnTextId, 0);

            searchView.setSuggestionsAdapter(simple);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean onSuggestionSelect(int i) {

        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {

        SQLiteCursor cursor = (SQLiteCursor) searchView.getSuggestionsAdapter().getItem(position);
        int indexColumnSuggestion = cursor.getColumnIndex( SuggestionDatabase.FIELD_SUGGESTION);

        searchView.setQuery(cursor.getString(indexColumnSuggestion), false);
//        editPerson.setText(searchView.getQuery());
        return true;
    }
}
