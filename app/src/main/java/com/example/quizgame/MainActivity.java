package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button start,exitMain;
    TextView signOut,highScoreCount;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference().child("scores");
    String highestScore="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signOut=findViewById(R.id.tview);
        start=findViewById(R.id.buttonStart);
        exitMain=findViewById(R.id.buttonStart2ExitMain);
        highScoreCount=findViewById(R.id.textViewHighestscoreMain);



        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i=new Intent(MainActivity.this, LoginPage.class);
                startActivity(i);
                finish();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, Quiz_Page.class);
                i.putExtra("highestScore",highestScore);
                startActivity(i);
                finish();

            }
        });

        exitMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String UID=auth.getUid();
                Log.d("Test main","High score update");
                try{highestScore = dataSnapshot.child(UID).child("highest score").getValue().toString();}
                catch (Exception e){
                    Log.d("main", String.valueOf(e));
                }
                highScoreCount.setText(highestScore);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read valur
                Toast.makeText(MainActivity.this,"Failed to get high score",Toast.LENGTH_SHORT).show();
            }
        });

    }
}