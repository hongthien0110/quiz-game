package com.example.quizgame;

import static com.example.quizgame.R.color.selected;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quiz_Page extends AppCompatActivity {
    Button lockans,exitgame;
    TextView optionA,optionB,optionC,optionD;
    TextView question,timer,currentprize;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference().child("Question");
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    DatabaseReference databaseReferenceSecond=database.getReference();

    String quizQuestion,quizOptionA,quizOptionB,quizOptionC,quizOptionD,correctAns;
    int questionCount,questionNum=1,userCorrect=0;
    String winCheck;
    String userAns,highestScore;
    boolean colorA=false,colorB=false,colorC=false,colorD=false;
    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME=30000;
    Boolean timerContninue;
    long left_time=TOTAL_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);
        lockans=findViewById(R.id.buttonLockAns);
        exitgame=findViewById(R.id.buttonExitGame);
        optionA=findViewById(R.id.textViewOptionA);
        optionB=findViewById(R.id.textViewOptionB);
        optionC=findViewById(R.id.textViewOptionC);
        optionD=findViewById(R.id.textViewOptionD);

        question=findViewById(R.id.textViewQuestion);
        timer=findViewById(R.id.textViewTimer);
        currentprize=findViewById(R.id.textViewCurrentPrize);

        Intent igp=new Intent(Quiz_Page.this, Result_page.class);

        Intent i=new Intent();
        highestScore=i.getStringExtra("highestScore");
        if(highestScore==null)
            highestScore="0";

        game();



        lockans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                lockAns();
                lockans.setClickable(false);

            }
        });

        exitgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendScore();
                Intent intent=new Intent(Quiz_Page.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        optionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAns = "c";
                colorClick();
                colorC=true;
                optionC.setBackgroundColor(getResources().getColor(R.color.selected));
                lockans.setClickable(true);

            }

        });

        optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            userAns="b";
            colorClick();
            colorB=true;
                optionB.setBackgroundColor(getResources().getColor(R.color.selected));
                lockans.setClickable(true);
            }
        });

        optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            userAns="a";
            colorClick();
            colorA=true;
            optionA.setBackgroundColor(getResources().getColor(R.color.selected));
                lockans.setClickable(true);
            }
        });

        optionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            userAns="d";
            colorClick();
            colorD=true;
                optionD.setBackgroundColor(getResources().getColor(R.color.selected));
                lockans.setClickable(true);
            }
        });
    }

    public void game(){
        resetTimer();
        startTimer();
        colorClick();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                questionCount= (int) dataSnapshot.getChildrenCount();

                quizQuestion=dataSnapshot.child(String.valueOf(questionNum)).child("q").getValue().toString();
                quizOptionA=dataSnapshot.child(String.valueOf(questionNum)).child("a").getValue().toString();
                quizOptionB=dataSnapshot.child(String.valueOf(questionNum)).child("b").getValue().toString();
                quizOptionC=dataSnapshot.child(String.valueOf(questionNum)).child("c").getValue().toString();
                quizOptionD=dataSnapshot.child(String.valueOf(questionNum)).child("d").getValue().toString();
                correctAns=dataSnapshot.child(String.valueOf(questionNum)).child("ans").getValue().toString();

                question.setText(quizQuestion);
                optionA.setText(quizOptionA);
                optionB.setText(quizOptionB);
                optionC.setText(quizOptionC);
                optionD.setText(quizOptionD);

                if(questionNum<questionCount){
                    questionNum++;
                }
                else{
                    Intent igp=new Intent(Quiz_Page.this, Result_page.class);
                    winCheck="3";
                    igp.putExtra("winCheck",winCheck);
                    startActivity(igp);
                    Toast.makeText(Quiz_Page.this, "All questions have been answered", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Quiz_Page.this, "Error!!!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void colorClick(){
        optionD.setBackgroundResource(R.drawable.button_background);
        optionA.setBackgroundResource(R.drawable.button_background);
        optionB.setBackgroundResource(R.drawable.button_background);
        optionC.setBackgroundResource(R.drawable.button_background);
        colorA=false;colorB=false;colorC=false;colorD=false;
    }

    public void startTimer(){
        countDownTimer=new CountDownTimer(left_time,1000) {
            @Override
            public void onTick(long l) {
            left_time=l;
            updateCountDownText();
            }

            @Override
            public void onFinish() {
            timerContninue=false;
            pauseTimer();
            question.setText("Waqt Poora hua");
            winCheck="2";
            Intent igp=new Intent(Quiz_Page.this, Result_page.class);
            igp.putExtra("winCheck",winCheck);
            Toast.makeText(Quiz_Page.this, "Time Up", Toast.LENGTH_LONG).show();
            Log.d("Test 2","timer finish");

            startActivity(igp);
            finish();

            }
        }.start();
        timerContninue=true;
    }
    public void resetTimer(){

        left_time=TOTAL_TIME;
        updateCountDownText();
    }

    public void updateCountDownText(){

        int second= (int) ((left_time/1000)%60);
        timer.setText(""+second);
    }
    public void pauseTimer(){
        countDownTimer.cancel();
        timerContninue=false;
    }

    public void sendScore(){

        String userUID=user.getUid();
        if(userCorrect>Integer.valueOf(highestScore))
            databaseReferenceSecond.child("scores").child(userUID).child("highest score").setValue(userCorrect);
        databaseReferenceSecond.child("scores").child(userUID).child("current score").setValue(userCorrect);
        Log.d("score","sent");
    }

    public void lockAns(){
        if(correctAns.equals(userAns)){
            userCorrect=questionNum*4*1000;
            currentprize.setText("Current Prize: "+userCorrect);
            if(colorA)
                optionA.setBackgroundResource(R.drawable.correct_option);
            if(colorB)
                optionB.setBackgroundResource(R.drawable.correct_option);
            if(colorC)
                optionC.setBackgroundResource(R.drawable.correct_option);
            if(colorD)
                optionD.setBackgroundResource(R.drawable.correct_option);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    game();
                }
            },1000);



        }
        else {
            if (colorA)
                optionA.setBackgroundResource(R.drawable.wrong_option);
            if (colorB)
                optionB.setBackgroundResource(R.drawable.wrong_option);
            if (colorC)
                optionC.setBackgroundResource(R.drawable.wrong_option);
            if (colorD)
                optionD.setBackgroundResource(R.drawable.wrong_option);

            Toast.makeText(Quiz_Page.this, R.string.lost, Toast.LENGTH_SHORT).show();
            sendScore();
            Log.d("wrong","score sent");
            userAns="";

            Intent igp=new Intent(Quiz_Page.this,Result_page.class);
            if(userCorrect==0){
                winCheck="0";
                igp.putExtra("winCheck",winCheck);
            }
            else
                winCheck="1";
            igp.putExtra("winCheck",winCheck);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(igp);
                    finish();
                }
            },2000);
            

        }


    }


}