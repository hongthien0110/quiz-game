package com.example.quizgame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends AppCompatActivity {
    EditText email,pass;
    Button signIn;
    View googleSignIn;
    TextView signUp,forgotPass;
    ProgressBar progressBar;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    GoogleSignInClient signInClient;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email=findViewById(R.id.LoginEmailAddress);
        pass=findViewById(R.id.LoginPassword);
        signIn=findViewById(R.id.buttonSignIn);
        googleSignIn=findViewById(R.id.signInButtongoogle);
        signUp=findViewById(R.id.SignUpText);
        forgotPass=findViewById(R.id.forgotPass);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        registerActivityForgoogleSignIn();


        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginPage.this, Sign_up_page.class);
                startActivity(i);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail=email.getText().toString();
                String userPass=pass.getText().toString();
                if(userPass==null||userEmail==null){
                    Toast.makeText(LoginPage.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
                else
                    signInwithFirebase(userEmail,userPass);

            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i=new Intent(LoginPage.this,ForgotPassword.class);
            startActivity(i);
            }
        });
    }
    public void registerActivityForgoogleSignIn(){

    activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {

                    int resultCode=o.getResultCode();
                    Intent data=o.getData();
                if(resultCode==RESULT_OK && data!=null){
                    Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                    firebaseSignInWithGoogle(task);
                }
                }
            });
    }

    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task){
        try {
            GoogleSignInAccount account=task.getResult(ApiException.class);
            Toast.makeText(this, "Signed In successfully", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(LoginPage.this,MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        } catch (ApiException e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void firebaseGoogleAccount(GoogleSignInAccount account){
        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=auth.getCurrentUser();
                        }
                        else{
                            Toast.makeText(LoginPage.this, "Sign in failed!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void signInGoogle(){
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("794154863495-ipumuoqp3uetokr4t4gfcmhvm6vt91ko.apps.googleusercontent.com")
                .requestEmail().build();

        signInClient= GoogleSignIn.getClient(this,gso);

        signIN();


    }

    public void signIN(){
        Intent signINintent=signInClient.getSignInIntent();
        activityResultLauncher.launch(signINintent);
    }

    public void signInwithFirebase(String userEmail,String userPass){
        progressBar.setVisibility(View.VISIBLE);
        signIn.setClickable(false);
        auth.signInWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginPage.this,"Login successful",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(LoginPage.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginPage.this,"Login Failed",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            Intent i=new Intent(LoginPage.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}