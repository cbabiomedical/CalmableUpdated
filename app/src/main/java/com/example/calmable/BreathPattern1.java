package com.example.calmable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;

public class BreathPattern1 extends AppCompatActivity {

        public static int x;

        //adding the audio
        MediaPlayer mysong, mysong2;

        private ImageView imageView;
        public int counter, counter2;
        private TextView breathsTxt, timerseconds, info;
        private Button startButton, backButton2;
        public static Prefs prefs;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_breath_pattern1);


            //sound for inhale
            mysong = MediaPlayer.create(this,R.raw.audiomass);
            //sound for exhale
            mysong2 = MediaPlayer.create(this,R.raw.beach_housetr);


            imageView = findViewById(R.id.imageView2);
            timerseconds= (TextView) findViewById(R.id.timerseconds);
            info = (TextView) findViewById(R.id.info);

            /////////////////////////////////////////////////////////////////
            breathsTxt = findViewById(R.id.breathsTakenTxt);

            prefs = new Prefs(this);

            breathsTxt.setText(MessageFormat.format("You have completed {0} breaths",prefs.getBreaths()));
            Log.d("---get breaths value---", String.valueOf(prefs.getBreaths()));

            x = prefs.getBreaths();


            //button to go back
            backButton2 = findViewById(R.id.backbutton2);
            backButton2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    onPause();
                    mysong2.pause();
                    mysong.pause();

                    Intent intent = new Intent(BreathPattern1.this, BreathPatterns.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                }
            });


            startButton = findViewById(R.id.startbutton);
            startButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    startAnimation();
                    startButton.setVisibility(View.GONE);
                    //backButton2.setVisibility(View.GONE);

                    new CountDownTimer(121000, 1000){
                        public void onTick(long millisUntilFinished){
                            timerseconds.setText(String.valueOf(counter));
                            counter++;

                        }
                        public  void onFinish(){
                            timerseconds.setText(" Done !");
                        }
                    }.start();

                }
            });
        }



        //to got to info of the breath pattern 1 page
        public void btnInfo (View view){
            startActivity(new Intent(getApplicationContext(), BreathPattern1Info.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        /*private void startIntroAnimation(){
            ViewAnimator
                    .animate(guideTxt)
                    .scale(0, 1)
                    .duration(1500)
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            guideTxt.setText("Breathe");
                        }
                    })
                    .start();
        }*/
        private void startAnimation(){
            ViewAnimator
                    .animate(imageView)
                    .alpha(0,1)

                    ///////////////////// 1 //////////////////////
                    .decelerate()
                    .duration(1)
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 2 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 3 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 4 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 5 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 6 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 7 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 8 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 9 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 10 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 11 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 12 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 13 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 14 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })
                    ///////////////////// 15 //////////////////////
                    .thenAnimate(imageView)
                    .scale(0.002f, 1.5f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to inhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    .thenAnimate(imageView)
                    .scale(1.5f, 0.002f)
                    .rotation(360)
                    .accelerate()
                    .duration(4000)

                    //adding a music sound to exhale part only
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            mysong2.start();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mysong2.pause();
                                }
                            }, 4 * 1000);
                        }
                    })

                    ////////////////////////////////////

                    .onStop(new AnimationListener.Stop() {
                        @Override
                        public void onStop() {
                            //backButton2.setVisibility(View.VISIBLE);
                            //guideTxt.setText("Good Job");
                            imageView.setScaleX(1.0f);
                            imageView.setScaleY(1.0f);

                            prefs.setBreaths(prefs.getBreaths() + 1);

//                            //getting number of interventions for the relax average
//                            BreathingReport.numOfInterventions = BreathingReport.numOfInterventions + 1;
//                            Log.d("NoInterv---------------", String.valueOf(BreathingReport.numOfInterventions));

                            //Log.d(TAG, "-----------------x-----------------");
                            prefs.setDate(SystemClock.currentThreadTimeMillis());
                            Intent intent = new Intent(getApplicationContext(), BreathingRate.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        }
                    })
                    .start();
        }
        @Override
        protected void onPause(){
            super.onPause();
            mysong.release();
            mysong2.release();
        }


        // for go back
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), BreathPatterns.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}