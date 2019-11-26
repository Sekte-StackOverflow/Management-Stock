package com.example.management_stock_app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.example.management_stock_app.Fragments.HomeFragment;
import com.example.management_stock_app.Fragments.LoginFragment;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new LoginFragment())
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
