package com.fatguy.behealthy.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fatguy.behealthy.Activities.ImagleProfile;
import com.fatguy.behealthy.Activities.LoginActivity;
import com.fatguy.behealthy.Activities.UpdatingUserProfile;
import com.fatguy.behealthy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class TopBarMainFragment extends Fragment {
    private View root;
    private ImageView Logout, Avatar;
    StorageReference storageReference;
    FirebaseAuth fAuth;

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
        Logout = root.findViewById(R.id.top_btnMore);

        Avatar = root.findViewById(R.id.btnAvatar);
        fAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        imagleAvatar(Avatar);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });

        Avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ImagleProfile.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return root;
    }

    private void ShowMenu(){
        PopupMenu popupMenu = new PopupMenu(getContext(),Logout);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuLogout:
                        startActivity(new Intent(getContext(), LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        break;
                    case R.id.menuUpdate:
                        startActivity(new Intent(getContext(), UpdatingUserProfile.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void imagleAvatar(ImageView av){
        StorageReference profileRef = storageReference.child("User/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getContext()).load(uri).into(av);
            }
        });
    }
}