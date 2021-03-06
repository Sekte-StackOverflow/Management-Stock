package com.example.management_stock_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.management_stock_app.Fragments.AllTransactionRecordFragment;
import com.example.management_stock_app.Fragments.DetailProductFragment;
import com.example.management_stock_app.Fragments.HomeFragment;
import com.example.management_stock_app.Fragments.LowStocksFragment;
import com.example.management_stock_app.Fragments.ProductInFragment;
import com.example.management_stock_app.Fragments.ProductOutFragment;
import com.example.management_stock_app.Fragments.ProductsFragment;
import com.example.management_stock_app.Fragments.StocksFragment;
import com.example.management_stock_app.Fragments.TransactionChartFragment;
import com.example.management_stock_app.Fragments.TransactionFragment;
import com.example.management_stock_app.Fragments.TransactionViewFragment;
import com.example.management_stock_app.Models.Barang;
import com.example.management_stock_app.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        ProductsFragment.OnFragmentInteractionListener,
        ProductInFragment.OnFragmentInteractionListener,
        ProductOutFragment.OnFragmentInteractionListener,
        StocksFragment.OnFragmentInteractionListener,
        TransactionFragment.OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        TransactionChartFragment.OnFragmentInteractionListener,
        TransactionViewFragment.OnFragmentInteractionListener,
        LowStocksFragment.OnFragmentInteractionListener,
        DetailProductFragment.OnFragmentInteractionListener,
        AllTransactionRecordFragment.OnFragmentInteractionListener
        {

    private List<Barang> barangList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firestore = FirebaseFirestore.getInstance();

        loadFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flcontent, fragment)
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
                        fragment = new LowStocksFragment();
                        break;
                    case R.id.action_transaction:
                        fragment = new AllTransactionRecordFragment();
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
                .replace(R.id.flcontent, new ProductsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addNewProduct(User user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flcontent, new TransactionChartFragment())
                .commit();
    }

    @Override
    public void setDataBarang(List<Barang> dataBarang, User user) {
        if (!dataBarang.isEmpty()&& user != null) {
            barangList = dataBarang;
            this.user = user;
        } else {
            System.out.println("Data is Empty");
        }
    }

    @Override
    public void detailBarang(Barang barang) {
        DetailProductFragment fragment = new DetailProductFragment();
        fragment.setInformation(barang);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flcontent, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoProductIn() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flcontent, new ProductInFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void buttonInput() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flcontent, new TransactionFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void buttonOutput() {

    }
    @Override
    public void buttonStocks() {

    }

    @Override
    public void buttonTransaction() {

    }

    @Override
    public void goToProductList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flcontent, new ProductsFragment())
                .commit();
    }

    @Override
    public void newBarangAdded(Barang barang) {
        if (barang != null) {
            barangList.add(barang);
            firestore.collection("Users").document(user.getId())
                    .update("Barang", barangList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Update data Product success!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Update Failed!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
