package com.example.calmable;

import static com.example.calmable.device.DeviceActivity.connectStatus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.chibde.visualizer.SquareBarVisualizer;
import com.example.calmable.device.DeviceActivity;
import com.example.calmable.model.FavModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends AppCompatActivity {

    static MediaPlayer mediaPlayer;

    AppCompatButton imageViewPlayPause, music_lib;
    TextView textCurrentTime, textTotalTimeDuration, music_title;
    SeekBar playSeekBar;
    Thread updateSeekBar;
    public static boolean isStarted = false;

    FirebaseFirestore database;
    boolean play = true;

    String uri;
    int a;
    String name;
    String timerString;

    int time;
    int musicCoin;
    int minutes;
    int seconds;
    int hours;
    int totalDuration;
    ArrayList<Integer> listOfCoins = new ArrayList<Integer>();

    public static final String EXTRA_NAME = "songName";
    int position;
    String sName;
    ArrayList<FavModel> songs = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageViewPlayPause = findViewById(R.id.imagePlayPause);
        playSeekBar = findViewById(R.id.playerSeekBar);
        textCurrentTime = findViewById(R.id.textCurrentTime);
        textTotalTimeDuration = findViewById(R.id.totalDuration);
        music_title = findViewById(R.id.music_title);
        //music_lib = findViewById(R.id.imagePlayIcon);
        mediaPlayer = new MediaPlayer();
        play = mediaPlayer.isPlaying();
        mediaPlayer = new MediaPlayer();
        isStarted = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
        }

        imageViewPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();

                imageViewPlayPause.setBackgroundResource(R.drawable.ic_play_circle);
            } else {
                mediaPlayer.start();
                imageViewPlayPause.setBackgroundResource(R.drawable.ic_pause_circle);
            }
        });
        SharedPreferences prefsTimeCon = getSharedPreferences("prefsMusic", MODE_PRIVATE);
        int firstStartTimeCon = prefsTimeCon.getInt("firstStartMusic", 0);

//        SharedPreferences sh = getSharedPreferences("prefsMusic", MODE_APPEND);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show

//        a = sh.getInt("firstStartMusic", 0);

        // We can then use the data
        Log.d("A Count", String.valueOf(a));

        int y = a + 1;

        SharedPreferences prefsCount1 = getSharedPreferences("prefsMusic", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsCount1.edit();
        editor.putInt("firstStartMusic", y);
        editor.apply();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uri = extras.getString("url");
            time = extras.getInt("time");
            name = extras.getString("songName");
            //time = Integer.parseInt(time_selected);
            music_title.setText(name);
            // image = extras.getInt("image");


            prepareMediaPlayer();

            Log.d("MUSIC", uri + "");
//            Log.d("DURATION", time_selected);
//            Log.d("NAME", name);

            Log.d("TAG", "name : " + name);
            Log.d("TAG", "url : " + uri);


            //To save stressed index average
            SharedPreferences.Editor editor1 = getSharedPreferences("com.example.calmable", MODE_PRIVATE).edit();
            editor1.putString("songNameSpre", name);
            editor1.apply();

        } else {
            Log.d("ERROR", "Error in getting null value");
        }


        //textTotalTimeDuration.setText(millisecondsToTimer(time));


        ProgressDialog progressDialog = ProgressDialog.show(this,
                "Loading Music", "Please Wait");


        // MEDIA STARTS FUNCTION
        mediaPlayer.setOnPreparedListener(mp -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            mediaPlayer.start();
            reqestPermission();
            TranslateAnimation animation = new TranslateAnimation(-25, 25, -25, 25);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(688);
            animation.setFillEnabled(true);
            animation.setFillAfter(true);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(1);
            //music_lib.startAnimation(animation);

            updateSeekBar = new Thread() {
                @Override
                public void run() {

                    totalDuration = mediaPlayer.getDuration();
                    int currentPosition = 0;


                    while (currentPosition < totalDuration) {
                        try {
                            sleep(1000);

                            currentPosition = mediaPlayer.getCurrentPosition();

                            String currentTime11 = millisecondsToTimer(mediaPlayer.getCurrentPosition());

                            // music coins part
                            int y = seconds;
                            int q = minutes;
                            double x = Double.parseDouble(currentTime11);

                            Log.d("TAG", "time s : " + y);
                            //Log.d("TAG", "time min : " + q);

                            if (seconds % 60 == 0) {

                                musicCoin++;

                                Log.d("TAG", "Music Coins  " + musicCoin);

                                // to save music coins
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.calmable", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("musicCoin", musicCoin);
                                editor.commit();

                                database = FirebaseFirestore.getInstance();
                                database.collection("users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .update("coins", FieldValue.increment(musicCoin));
                            }

                            musicCoin = 0;

                            //Log.d("Current position", String.valueOf(mediaPlayer.getCurrentPosition()));
                            //Log.d("upCurrent time", String.valueOf(currentPosition));

                            playSeekBar.setProgress(currentPosition);

                        } catch (InterruptedException | IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }


                    if (DeviceActivity.connectStatus) {
                        if (connectStatus = true){


                            /**
                             * after end music directory
                             */
                            if (currentPosition == totalDuration) {
                                mediaPlayer.stop();
                                imageViewPlayPause.setBackgroundResource(R.drawable.ic_play_circle);


                                //to go to rate page
                                Intent intent = new Intent(getApplicationContext(), MusicRate.class);
                                intent.putExtra("songName", name);
                                intent.putExtra("url", uri);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                Log.d("TAG", "---------------Done----------------" );
                                Log.d("TAG", "---------------Done----------------" );
                                Log.d("TAG", "---------------Done----------------" );

                            }



                            if (currentPosition > totalDuration) {
                                mediaPlayer.stop();

                                imageViewPlayPause.setBackgroundResource(R.drawable.ic_play_circle);
                                //Intent intent = new Intent(getApplicationContext(), concentration_music.class);
                                //startActivity(intent);
                                String notification = "Media finished Playing";
                                NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.drawable.ic_notifications).setContentTitle("New Notification").setContentText(notification)
                                        .setAutoCancel(true);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0, builder.build());
                                isStarted = false;
                                //to go to rate page
                                Intent intent = new Intent(getApplicationContext(), MusicRate.class);
                                intent.putExtra("songName", name);
                                intent.putExtra("url", uri);
                                startActivity(intent);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            }



                        }

                    } else {


                        /**
                         * after end music directory
                         */
                        if (currentPosition == totalDuration) {
                            mediaPlayer.stop();
                            imageViewPlayPause.setBackgroundResource(R.drawable.ic_play_circle);


                            //to go to rate page
                            Intent intent = new Intent(getApplicationContext(), MusicRateWithOutDeviceActivity.class);
                            intent.putExtra("songName", name);
                            intent.putExtra("url", uri);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            Log.d("TAG", "---------------Done----------------" );
                            Log.d("TAG", "---------------Done----------------" );
                            Log.d("TAG", "---------------Done----------------" );

                        }



                        if (currentPosition > totalDuration) {
                            mediaPlayer.stop();

                            imageViewPlayPause.setBackgroundResource(R.drawable.ic_play_circle);
                            //Intent intent = new Intent(getApplicationContext(), concentration_music.class);
                            //startActivity(intent);
                            String notification = "Media finished Playing";
                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.ic_notifications).setContentTitle("New Notification").setContentText(notification)
                                    .setAutoCancel(true);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(0, builder.build());
                            isStarted = false;
                            //to go to rate page
                            Intent intent = new Intent(getApplicationContext(), MusicRateWithOutDeviceActivity.class);
                            intent.putExtra("songName", name);
                            intent.putExtra("url", uri);
                            startActivity(intent);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        }

                    }

                    /**
                     * after end music directory
                     */
//                    if (currentPosition == totalDuration) {
//                        mediaPlayer.stop();
//                        imageViewPlayPause.setBackgroundResource(R.drawable.ic_play_circle);
//
//
//                        //to go to rate page
//                        Intent intent = new Intent(getApplicationContext(), MusicRate.class);
//                        intent.putExtra("songName", name);
//                        intent.putExtra("url", uri);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                    }

//                    if (currentPosition > totalDuration) {
//                        mediaPlayer.stop();
//
//                        imageViewPlayPause.setBackgroundResource(R.drawable.ic_play_circle);
//                        //Intent intent = new Intent(getApplicationContext(), concentration_music.class);
//                        //startActivity(intent);
//                        String notification = "Media finished Playing";
//                        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
//                                .setSmallIcon(R.drawable.ic_notifications).setContentTitle("New Notification").setContentText(notification)
//                                .setAutoCancel(true);
//                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.notify(0, builder.build());
//                        isStarted = false;
//                        //to go to rate page
//                        Intent intent = new Intent(getApplicationContext(), MusicRate.class);
//                        intent.putExtra("songName", name);
//                        intent.putExtra("url", uri);
//                        startActivity(intent);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//
//                    }

                }
            };

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    isStarted = false;
                }
            });

            playSeekBar.setMax(mediaPlayer.getDuration());
            updateSeekBar.start();
            playSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
            playSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//            playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                    mediaPlayer.seekTo(seekBar.getProgress());
//                }
//            });


            //set end time to textview
            String endTime = millisecondsToTimer(mediaPlayer.getDuration());
            textTotalTimeDuration.setText(endTime);
            //int noOfRuns = time / mediaPlayer.getDuration();

            final Handler handler = new Handler();
            final int delay = 1000;


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String currentTime = millisecondsToTimer(mediaPlayer.getCurrentPosition());
                    textCurrentTime.setText(currentTime);
                    playSeekBar.setProgress(mp.getCurrentPosition());
                    handler.postDelayed(this, delay);

                }
            }, delay);

//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    String currentTime = millisecondsToTimer(mediaPlayer.getCurrentPosition());
//                    textCurrentTime.setText(currentTime);
//                    handler.postDelayed(this, delay);
//                    if (mediaPlayer.getDuration() < time) {
//
//                        Log.d("RUNS", String.valueOf(noOfRuns));
//                        int remain = time % mediaPlayer.getDuration();
//                        Log.d("REMAIN", String.valueOf(remain));
//                        int playNo = noOfRuns + 1;
//                        Log.d("play", String.valueOf(playNo));
//
//                    }
//
//                }
//            }, delay);

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void prepareMediaPlayer() {
        try {
            mediaPlayer.setDataSource(uri.toString());
            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void reqestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.RECORD_AUDIO
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now

                            startAudioVisulizer();

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> list, PermissionToken permissionToken) {

                    }


                })
                .onSameThread()
                .check();

    }

    //Audio Visualizer
    private void startAudioVisulizer() {

        SquareBarVisualizer squareBarVisualizer = findViewById(R.id.visualizer);
        squareBarVisualizer.setColor(ContextCompat.getColor(this, R.color.dark_blue_100));
        squareBarVisualizer.setDensity(65);
        squareBarVisualizer.setGap(2);
        squareBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());

    }

    @Override
    protected void onStart() {
        super.onStart();
        imageViewPlayPause.setBackgroundResource(R.drawable.ic_pause_circle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
//        imageViewPlayPause.setImageResource(R.drawable.ic_play_circle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        isStarted = false;
    }


    private String millisecondsToTimer(long milliSeconds) {
        timerString = "";
        String secondsString;

        hours = (int) (milliSeconds / (1000 * 60 * 60));
        minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        timerString = timerString + minutes + "." + secondsString;

        return timerString;
    }
}
