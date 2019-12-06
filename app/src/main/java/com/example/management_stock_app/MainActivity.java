package com.example.management_stock_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.management_stock_app.Fragments.HomeFragment;
import com.example.management_stock_app.Fragments.HomeFragment.OnFragmentInteractionListener;
import com.example.management_stock_app.Fragments.LoginFragment;
import com.example.management_stock_app.Fragments.ProductInFragment;
import com.example.management_stock_app.Fragments.ProductOutFragment;
import com.example.management_stock_app.Fragments.ProductsFragment;
import com.example.management_stock_app.Fragments.StocksFragment;
import com.example.management_stock_app.Fragments.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        ProductsFragment.OnFragmentInteractionListener,
        ProductInFragment.OnFragmentInteractionListener,
        ProductOutFragment.OnFragmentInteractionListener,
        StocksFragment.OnFragmentInteractionListener,
        TransactionFragment.OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //getSupportFragmentManager().beginTransaction()
        //        .replace(R.id.main_container, new ProductsFragment())
        //        .commit();

        loadFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.action_home:
                fragment = new HomeFragment();
                break;
            case R.id.action_product:
                fragment = new ProductsFragment();
                break;
            case R.id.action_low_stock:
                fragment = new StocksFragment();
                break;
            case R.id.action_transaction:
                fragment = new TransactionFragment();
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void buttonProduct() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new ProductsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public String imageFromGallery() {
        return "Its Work Boy";
    }

    @Override
    public void buttonInput() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new ProductInFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void buttonOutput() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new ProductOutFragment())
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void buttonStocks() {

    }

    @Override
    public void buttonTransaction() {

    }
}
