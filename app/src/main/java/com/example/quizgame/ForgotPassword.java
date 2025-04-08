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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText email;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    Button forgetpass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email=findViewById(R.id.forgotPassEmail);
        forgetpass=findViewById(R.id.buttonContinue);
        progressBar=findViewById(R.id.progressBarforgetpass);

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetpass.setClickable(false);
                String userEmail=email.getText().toString();
                resetPass(userEmail);
            }
        });
    }

    public void resetPass(String userEmail){
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "Reset password is sent to email address", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        }
                        else{
                            Toast.makeText(ForgotPassword.this, "Problem!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}