package com.fatguy.behealthy.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fatguy.behealthy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class UpdatingUserProfile extends Activity {

    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private Button update;
    private EditText name, date, height, weight, Email;


    public UpdatingUserProfile(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public UpdatingUserProfile() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating_user_profile);

        name = findViewById(R.id.reg_txtNameUp);
        update = findViewById(R.id.reg_btnUpdate);
        date = findViewById(R.id.reg_txtDateUp);
        height = findViewById(R.id.reg_txtHeightUp);
        weight = findViewById(R.id.reg_txtWeightUp);
        Email = findViewById(R.id.reg_txtEmailUp);

        reference = FirebaseDatabase.getInstance().getReference().child("User");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("email").getValue().equals(MainActivity.email)) {
                        name.setText(ds.child("name").getValue(String.class));
                        Email.setText(MainActivity.email);
                        date.setText(ds.child("date").getValue(String.class));
                        height.setText(ds.child("height").getValue(float.class).toString());
                        weight.setText(ds.child("weight").getValue(float.class).toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void getData() {
        reference = FirebaseDatabase.getInstance().getReference().child("User");
        Query query = reference.orderByChild("email").equalTo(MainActivity.email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    reference.child(key).child("name").setValue(name.getText().toString().trim());
                    reference.child(key).child("date").setValue(date.getText().toString().trim());
                    reference.child(key).child("height").setValue(Float.valueOf(height.getText().toString().trim()));
                    reference.child(key).child("weight").setValue(Float.valueOf(weight.getText().toString().trim()));
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}
