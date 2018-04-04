package com.example.ianalonzo.tourup;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            //Make the app fullscreen
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.openingpage);

        //Animation
        /*Animation splash = AnimationUtils.loadAnimation(this, R.anim.splashscreenanimation);
        iv.startAnimation(splash);*/

        //Intent for navigation drawer activity
        final Intent navigation = new Intent(this, Navigation.class);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finally {
                    //Starts the navigation drawer activity
                    startActivity(navigation);
                    finish();
                }
            }
        };

        timer.start();
    }
}
