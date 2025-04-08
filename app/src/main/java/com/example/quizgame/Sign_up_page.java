package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up_page extends AppCompatActivity {
    EditText email,pass;
    Button signUp;
    ProgressBar progressBar;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference().child("scores");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        email=findViewById(R.id.signUpEmail);
        pass=findViewById(R.id.signUpPass);
        signUp=findViewById(R.id.buttonSignUp);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp.setClickable(false);
                String userEmail=email.getText().toString();
                String userPass=pass.getText().toString();
                if(userPass==null||userEmail==null){
                    Toast.makeText(Sign_up_page.this,"Please fill both fields",Toast.LENGTH_SHORT).show();
                }
                else
                    signUpFirebase(userEmail,userPass);

            }
        });
    }

    public void signUpFirebase(String userEmail,String userPass){
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Sign_up_page.this, "Your account has been created", Toast.LENGTH_SHORT).show();
                            String uid=auth.getUid();
                            ref.child(uid).child("highest score").setValue("0");
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                            
                        }
                        else{
                            Toast.makeText(Sign_up_page.this, "Problem!!!!!!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}