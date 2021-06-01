package com.fatguy.behealthy.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fatguy.behealthy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class Forgot_password_Activity extends Activity {
    private  EditText gmail;
    private  Button ResetPass;
    private  ProgressBar progressBar;
    private FirebaseAuth mAuth;


    public Forgot_password_Activity(){}
    public Forgot_password_Activity(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        gmail = (EditText) findViewById(R.id.login_txtEmailPass);
        ResetPass = (Button) findViewById(R.id.btnForgot);

        mAuth = FirebaseAuth.getInstance();

        ResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resetPassword();
                String email = gmail.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Forgot_password_Activity.this,"Please enter your correct email here",Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Forgot_password_Activity.this,"Please visit your email to reset your password.",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Forgot_password_Activity.this,LoginActivity.class));
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Forgot_password_Activity.this,"Error"+message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
