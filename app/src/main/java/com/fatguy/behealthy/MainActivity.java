package com.fatguy.behealthy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {

    private FragmentManager mng = getSupportFragmentManager();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initial_Main();

    }

    public void Initial_Main()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            LoginActivity login = new LoginActivity(mAuth);
            startActivity(new Intent(MainActivity.this, login.getClass()));
        }
        render_main();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    protected void render_main(){
        MainFragment mf = new MainFragment(mng);
        View view = mf.getRoot();
        TopBarMainFragment tf = new TopBarMainFragment();
        mng.beginTransaction().replace(R.id.layoutMain,mf,mf.getTag()).commit();
        mng.beginTransaction().replace(R.id.layout_top_nav,tf,tf.getTag()).commit();
    }


}