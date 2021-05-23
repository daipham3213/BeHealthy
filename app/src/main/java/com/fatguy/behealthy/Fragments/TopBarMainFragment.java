package com.fatguy.behealthy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.fatguy.behealthy.Activities.LoginActivity;
import com.fatguy.behealthy.R;
import com.google.firebase.auth.FirebaseAuth;

public class TopBarMainFragment extends Fragment {
    private View root;
    private ImageView Logout;

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
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return root;
    }
}