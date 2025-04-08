package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    ImageView image;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        title=findViewById(R.id.textViewsplash);
        image=findViewById(R.id.imageViewSplash);

        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splashanim);
        animation.setDuration(4000);
        title.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScreen.this, LoginPage.class);
                startActivity(i);
                finish();

            }
        },3000);
    }
}