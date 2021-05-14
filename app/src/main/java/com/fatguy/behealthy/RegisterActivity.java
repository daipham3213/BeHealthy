package com.fatguy.behealthy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends Activity {
    private EditText edtDate;
    private Spinner spnSex;
    private EditText Name;
    private EditText Email;
    private EditText Pass;
    private int Age;
    private EditText weight;
    private EditText height;
    private FirebaseAuth mAuth;
    private Button Register;
    private String TAG ="Register Activity";
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private String UID;

    public RegisterActivity(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public RegisterActivity() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Initial_Register();
    }
    public void Initial_Register()
    {
        edtDate = findViewById(R.id.reg_txtDate);

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickADate();
            }
        });
        spnSex = (Spinner) findViewById(R.id.reg_spn_sex);
        LoadSexList();
        Name = findViewById(R.id.reg_txtName);
        Email = findViewById(R.id.reg_txtEmail);
        Pass = findViewById(R.id.reg_txtPassword);
        weight = findViewById(R.id.reg_txtWeight);
        height = findViewById(R.id.reg_txtHeight);

        Register = findViewById(R.id.reg_btnRegister);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String email = Email.getText().toString();
                String pass = Pass.getText().toString();
                String sex = spnSex.getSelectedItem().toString();
                float w = Float.parseFloat(weight.getText().toString());
                float h = Float.parseFloat(height.getText().toString());

                String date = "";
                date = edtDate.getText().toString();
                String year = edtDate.getText().toString();
                year = year.substring(year.lastIndexOf("-"));
                Age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(year);
                signUp(name,email,pass,sex,date,w,h,Age);

            }
        });
    }

    private void LoadSexList() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSex.setAdapter(adapter);
    }

    private void PickADate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year =  calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                edtDate.setText(dateFormat.format(calendar.getTime()));
            }
        },year,month,day);
        datePickerDialog.show();
    }

    public void signUp(String name, String email, String password,String sex, String date, float weight, float height, int age)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UID = user.getUid();
                            add(name, email, sex, date, weight, height, age);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Authentication failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }
    public void add(String name, String email, String sex, String date, float weight, float height, int age)
    {
        User user= new User(name, email, sex, date, weight, height, age);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("User").child(UID);
        ref.setValue(user);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null)
        {
            LoginActivity login = new LoginActivity(mAuth);
            startActivity(new Intent(RegisterActivity.this, login.getClass()));
        }
    }
}
