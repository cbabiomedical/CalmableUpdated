package com.example.calmable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.security.Permission;

public class LoginUserActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn , home;

    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        // for request location
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginUserActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(LoginUserActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(LoginUserActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        // for request location
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginUserActivity.this,
                    Manifest.permission.BLUETOOTH_CONNECT)){
                ActivityCompat.requestPermissions(LoginUserActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
            }else{
                ActivityCompat.requestPermissions(LoginUserActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
            }
        }

        // for request location
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginUserActivity.this,
                    Manifest.permission.BLUETOOTH_SCAN)){
                ActivityCompat.requestPermissions(LoginUserActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
            }else{
                ActivityCompat.requestPermissions(LoginUserActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
            }
        }

//        if (Build.VERSION.SdkInt >= Android.OS.BuildVersionCodes.Tiramisu)
//        {
//            if (ContextCompat.CheckSelfPermission(this, Manifest.Permission.PostNotifications) != (int) Permission.Granted)
//            {
//                ActivityCompat.RequestPermissions(this, new String[] { Manifest.Permission.PostNotifications }, 1);
//            }
//        }

        // for request location
//        if (ContextCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginUserActivity.this,
//                    Manifest.permission.ACCESS_NOTIFICATION_POLICY)){
//                ActivityCompat.requestPermissions(LoginUserActivity.this,
//                        new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 1);
//            }else{
//                ActivityCompat.requestPermissions(LoginUserActivity.this,
//                        new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 1);
//            }
//        }


        //set onclick listener for signin button
        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        //firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        //set onclick listener for forgot password button
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        home = (Button) findViewById(R.id.homebtn);
        home.setOnClickListener(this);

    }

    // main btns actions
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //go to sign in page
            case R.id.signIn:
                userLogin();
                break;
            //go to forget password page
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.homebtn:
                startActivity(new Intent(this, Home.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
        }
    }

    //    //get & display current user's profile
    @Override
    protected void onStart() {
        super.onStart();

        //signin authentication
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    //get user credentials & convert it back to string
    private void userLogin() {
        //get user email and password
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //check email is entered
        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        //check whether email is valid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }
        //check password is entered
        if(password.isEmpty()){
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }
        //check password length is more than 6 characters
        if(password.length() < 6){
            editTextPassword.setError("Minimum Password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.GONE);
        //signin with email and password
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    startActivity(new Intent(LoginUserActivity.this, RoadMapVideoActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }else {
                    Toast.makeText(LoginUserActivity.this,"LogIn Failed! Please check your username or password again",Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }


    public void btnUserRegister (View view) {
        startActivity(new Intent(getApplicationContext(), RegisterUser.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }



    // for request location
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(LoginUserActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}