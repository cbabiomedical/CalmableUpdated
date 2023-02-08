package com.example.calmable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileMain extends AppCompatActivity {

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        NavigationBar();

        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Button settings = (Button) findViewById(R.id.settings);
        Button signOutBtn = (Button) findViewById(R.id.signOut);
        Button btnHelp = (Button) findViewById(R.id.btnHelp);
        Button myFavouritesBtn = (Button) findViewById(R.id.myFavouritesBtn);
        Button remindersBtn = (Button) findViewById(R.id.remindersBtn);
        Button calenderBtn = (Button) findViewById(R.id.calenderBtn);
        Button premiumBtn = (Button) findViewById(R.id.premiumBtn);
        Button rateUsBtn = (Button) findViewById(R.id.rateUsBtn);
        Button aboutAppBtn = (Button) findViewById(R.id.aboutAppBtn);

        //load profile picture to the imageview
        ImageView profileImage = (ImageView) findViewById(R.id.profileImage);

        storageReference = FirebaseStorage.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        StorageReference profileRef = storageReference.child("users/" + userid + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        getData();


        //go to edit profile page
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(in);
            }

        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), RoadMapVideoActivity.class);
                startActivity(in);
            }

        });

        myFavouritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MyFavouritesActivity.class);
                startActivity(in);
            }

        });

        remindersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), LocationActivity.class);
                startActivity(in);
            }

        });

        calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), CalenderActivity.class);
                startActivity(in);
            }

        });

        premiumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), ComingSoonActivity.class);
                startActivity(in);
            }

        });

        rateUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), RateUsActivity.class);
                startActivity(in);
            }

        });

        aboutAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), AboutAppActivity.class);
                startActivity(in);
            }

        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), LoginUserActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(in);
                finish();

            }

        });

        //openDialog();
    }
    //for testing stressed locations
    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMain.this);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Stress Alert!");
        builder.setMessage("We noticed that you've been stressed today!");

        builder.setNegativeButton("VIEW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in = new Intent(getApplicationContext(), StressedLocationsActivity.class);
                startActivity(in);
            }
        });
//
//        //display getting user inputs popup
//        builder.setPositiveButton("NO, I'm Stressed", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                PopUpOne popUpOne = new PopUpOne();
//                popUpOne.show(getSupportFragmentManager(), "popup one");
//            }
//        });
        builder.show();
    }

    private void getData() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("fullName").getValue(String.class);

                Log.d("TAG", "edit profile: name-------------" + name);

                TextView textView = (TextView) findViewById(R.id.userName);
                textView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    private void NavigationBar() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.relax:
                        startActivity(new Intent(getApplicationContext(), Relax.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.journal:
                        startActivity(new Intent(getApplicationContext(), Journal.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.challenge:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.profile:
                        return true;
                }

                return false;
            }
        });

    }

    public void BtnLocationReminder(View view){
        startActivity(new Intent(getApplicationContext() , LocationReminderActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}