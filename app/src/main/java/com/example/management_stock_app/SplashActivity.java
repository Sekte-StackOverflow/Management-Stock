package com.example.management_stock_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.management_stock_app.Actitvity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = "SPLASH_ACTIVITY";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private int waktu_loading=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //setelah loading maka akan langsung berpindah ke home activity
//                FirebaseUser user = auth.getCurrentUser();
//                if (user != null) {
//                    Log.d(TAG, "Auth Checked: " + user.getUid());
//                    Intent home =new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(home);
//                    finish();
//                } else {
//                    Log.d(TAG, "Auth Checked: " + false);
//                    Intent login =new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(login);
//                    finish();
//                }
                Log.d(TAG, "Auth Checked: " + false);
                Intent login =new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
                finish();
            }
        },waktu_loading);
    }
}
