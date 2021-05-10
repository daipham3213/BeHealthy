package com.fatguy.behealthy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {
    private TextView btnRegister;
    private Button Login;
    private EditText Email;
    private EditText Pass;
    private FirebaseAuth mAuth;
    private final String TAG = "LoginActivity";

    public LoginActivity(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public LoginActivity() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegister = findViewById(R.id.login_btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity registerActivity = new RegisterActivity(mAuth);
                startActivity(new Intent(LoginActivity.this, registerActivity.getClass()));
            }
        });
        Login = findViewById(R.id.login_btnLogin);
        Email = findViewById(R.id.login_txtEmail);
        Pass = findViewById(R.id.login_txtPassword);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(Email.getText().toString(),Pass.getText().toString());
            }
        });
    }

    public void signIn(String Email, String Password)
    {
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "loginUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "loginUserWithEmail:failure", task.getException());

                            Toast.makeText(LoginActivity.this,
                                    "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null)
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
