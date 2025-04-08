package com.example.quizgame;


import static com.example.quizgame.R.drawable.timeout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Result_page extends AppCompatActivity {
    TextView prizescore;
    Button playagain,exit;
    ImageView resultimg;
    String userPrize="0";
    int winCheck;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference().child("scores");
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.result);
        animation.setDuration(4000);
        resultimg=findViewById(R.id.imageViewResult);
        resultimg.setAnimation(animation);
        winCheck=Integer.valueOf(getIntent().getStringExtra("winCheck"));

        Log.d("test intent","test 1");
            if(winCheck==1){
               resultimg.setImageResource(R.drawable.betterluck);
                Log.d("image test","case 1");

            }
            else if(winCheck==2){
                Log.d("image test","case 2");
                       resultimg.setImageResource(R.drawable.timeout);
                       resultimg.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            else if(winCheck==3){
                Log.d("image test","case 3");
                resultimg.setImageResource(R.drawable.win);
            }
            Log.d("test","test");
        prizescore=findViewById(R.id.textViewFinalprize);
       playagain=findViewById(R.id.buttonAgain);
       exit=findViewById(R.id.buttonExit);


        reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String userUID=user.getUid();
               Log.d("result","prize score");
               userPrize=snapshot.child(userUID).child("current score").getValue().toString();
               prizescore.setText(userPrize);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(Result_page.this, "Error!!1", Toast.LENGTH_SHORT).show();
           }
       });

       playagain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i2=new Intent(Result_page.this,MainActivity.class);
               startActivity(i2);
               finish();

           }
       });

       exit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               resultimg.setImageResource(R.drawable.bye);
               animation.setDuration(1500);
               resultimg.setAnimation(animation);
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       android.os.Process.killProcess(android.os.Process.myPid());
                     System.exit(0);


                   }
               },1500);


           }
       });
    }
}