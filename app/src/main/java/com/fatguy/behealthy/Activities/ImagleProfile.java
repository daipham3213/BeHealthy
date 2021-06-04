package com.fatguy.behealthy.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.fatguy.behealthy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImagleProfile extends Activity {

    Button save, close;
    ImageView profileImage;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    AnstronCoreHelper coreHelper;

    public ImagleProfile(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        save = findViewById(R.id.btnSave);
        close = findViewById(R.id.btnClose);
        profileImage = findViewById(R.id.profile_image);
        coreHelper = new AnstronCoreHelper(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        imagleAvatar(profileImage);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImagleProfile.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
             //   uploadImageToFirebase(imageUri);
                try {
                    compressAndUplaod(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
// hàm nén và update avatar lên storage.
    private void compressAndUplaod(Uri pickedImageUri) throws IOException {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference fileRef = storageReference.child("User/"+fAuth.getCurrentUser().getUid()+"profile.jpg");

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
                       Picasso.get().load(uri).into(profileImage);
                        progressDialog.dismiss();
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

        StorageReference fileRef = storageReference.child("User/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
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

    public void imagleAvatar(ImageView av){
        StorageReference profileRef = storageReference.child("User/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(av);
            }
        });
    }
}
