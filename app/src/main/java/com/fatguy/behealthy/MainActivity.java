package com.fatguy.behealthy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity  {

    private FragmentManager mng = getSupportFragmentManager();
    private ImageView Diagnose;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
        render_main();


    }

    protected void render_main(){
        MainFragment mf = new MainFragment(mng);
        view = mf.getRoot();
        TopBarMainFragment tf = new TopBarMainFragment();
        mng.beginTransaction().replace(R.id.layoutMain,mf,mf.getTag()).commit();
        mng.beginTransaction().replace(R.id.layout_top_nav,tf,tf.getTag()).commit();
    }


}