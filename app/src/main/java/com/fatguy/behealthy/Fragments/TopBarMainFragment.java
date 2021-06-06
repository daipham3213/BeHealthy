package com.fatguy.behealthy.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fatguy.behealthy.Activities.ImagleProfile;
import com.fatguy.behealthy.Activities.LoginActivity;
import com.fatguy.behealthy.Models.Utils;
import com.fatguy.behealthy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class TopBarMainFragment extends Fragment {
    private View root;
    private ImageView btnMore, Avatar;
    private TextView name, greeting;
    private StorageReference storageReference;
    private FirebaseAuth fAuth;
    private DatabaseReference mRef;

    public TopBarMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_top_bar_main, container, false);
        btnMore = root.findViewById(R.id.top_btnMore);
        name = root.findViewById(R.id.nav_Name);
        greeting = root.findViewById(R.id.nav_welcome);

        Avatar = root.findViewById(R.id.btnAvatar);
        fAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("User").child(fAuth.getUid()).child("name");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                name.setText("Welcome, " + snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        mRef = FirebaseDatabase.getInstance().getReference().child("Welcome");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String i = String.valueOf(Utils.RandomInt(1, 14));
                greeting.setText(snapshot.child(i).getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();

        imagleAvatar(Avatar);

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });
        return root;
    }

    private void ShowMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), btnMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuLogout:
                        fAuth.signOut();
                        startActivity(new Intent(getContext(), LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        break;
                    case R.id.menuUpdate:
                        startActivity(new Intent(getContext(), ImagleProfile.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void imagleAvatar(ImageView av) {
        StorageReference profileRef = storageReference.child("User/" + fAuth.getCurrentUser().getUid() + "profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(requireContext())
                        .load(uri)
                        .apply(new RequestOptions().override(90, 90))
                        .into(av);
            }
        });
    }
}