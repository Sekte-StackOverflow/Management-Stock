package com.example.management_stock_app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.management_stock_app.Fragments.HomeFragment;
import com.example.management_stock_app.Fragments.LoginFragment;
import com.example.management_stock_app.Fragments.ProductInFragment;
import com.example.management_stock_app.Fragments.ProductOutFragment;
import com.example.management_stock_app.Fragments.ProductsFragment;
import com.example.management_stock_app.Fragments.StocksFragment;
import com.example.management_stock_app.Fragments.TransactionFragment;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        ProductsFragment.OnFragmentInteractionListener,
        ProductInFragment.OnFragmentInteractionListener,
        ProductOutFragment.OnFragmentInteractionListener,
        StocksFragment.OnFragmentInteractionListener,
        TransactionFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                //.replace(R.id.main_container, new LoginFragment())
                .replace(R.id.main_container, new HomeFragment())
                .commit();
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
    public void buttonInput() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new ProductInFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void buttongOutput() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new ProductOutFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void buttonStocks() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new StocksFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void buttonTransaction() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new TransactionFragment())
                .addToBackStack(null)
                .commit();
    }
}
