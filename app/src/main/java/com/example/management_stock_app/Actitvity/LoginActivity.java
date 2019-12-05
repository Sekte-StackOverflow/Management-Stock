package com.example.management_stock_app.Actitvity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.management_stock_app.Fragments.LoginFragment;
import com.example.management_stock_app.MainActivity;
import com.example.management_stock_app.R;

public class LoginActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_activity, new LoginFragment())
                .commit();
    }

    @Override
    public void onLoginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
