package com.fatguy.behealthy.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.bumptech.glide.Glide;
import com.fatguy.behealthy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImagleProfile extends Activity {

    private Button save;
    private ImageView profileImage;
    private StorageReference storageReference;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private Button update;
    private EditText name, date, height, weight, Email;

    AnstronCoreHelper coreHelper;
    private DatabaseReference reference;

    public ImagleProfile() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        save = findViewById(R.id.btnChangeAvatar);
        profileImage = findViewById(R.id.profile_image);
        coreHelper = new AnstronCoreHelper(this);

        name = findViewById(R.id.update_txtNameUp);
        update = findViewById(R.id.update_btnUpdate);
        date = findViewById(R.id.update_txtDateUp);
        height = findViewById(R.id.update_txtHeightUp);
        weight = findViewById(R.id.update_txtWeightUp);
        Email = findViewById(R.id.update_txtEmailUp);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        imagleAvatar(profileImage);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("User").child(fAuth.getUid());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

                name.setText(snapshot.child("name").getValue(String.class));
                Email.setText(snapshot.child("email").getValue(String.class));
                date.setText(snapshot.child("date").getValue(String.class));
                height.setText(snapshot.child("height").getValue(float.class).toString());
                weight.setText(snapshot.child("weight").getValue(float.class).toString());

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }

    private void getData() {
        reference = FirebaseDatabase.getInstance().getReference().child("User").child(fAuth.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                reference.child("name").setValue(name.getText().toString().trim());
                reference.child("date").setValue(date.getText().toString().trim());
                reference.child("height").setValue(Float.valueOf(height.getText().toString().trim()));
                reference.child("weight").setValue(Float.valueOf(weight.getText().toString().trim()));
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                //   uploadImageToFirebase(imageUri);
                try {
                    compressAndUploaded(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // hàm nén và update avatar lên storage.
    private void compressAndUploaded(Uri pickedImageUri) throws IOException {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference fileRef = storageReference.child("User/" + fAuth.getCurrentUser().getUid() + "profile.jpg");

        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), pickedImageUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask2 = fileRef.putBytes(data);
        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Picasso.get().load(uri).into(profileImage);
                        Glide.with(getApplicationContext()).load(uri).into(profileImage);
                        progressDialog.dismiss();
                        save.setBackgroundColor(getResources().getColor(R.color.md_teal_300, null));
                        Toast.makeText(ImagleProfile.this, "Change Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ImagleProfile.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
            }
        });
    }

    // hàm không nén khi update hình lên storage.
    private void uploadImageToFirebase(Uri imageUri) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference fileRef = storageReference.child("User/" + fAuth.getCurrentUser().getUid() + "profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressDialog.dismiss();
                        Picasso.get().load(uri).into(profileImage);
                        Toast.makeText(ImagleProfile.this, "Change Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ImagleProfile.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void imagleAvatar(ImageView av) {
        StorageReference profileRef = storageReference.child("User/" + fAuth.getCurrentUser().getUid() + "profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //  Picasso.get().load(uri).into(av);
                Glide.with(getApplicationContext()).load(uri).into(av);
            }
        });
    }
}
