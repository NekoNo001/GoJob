package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.b1906478.gojob.activity.LoginActivity.signInStart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.b1906478.gojob.R;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences sharedPreferences2 = getSharedPreferences("Setting", MODE_PRIVATE);
        boolean darkMode = sharedPreferences2.getBoolean("darkMode",false);
        if(darkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                if (sharedPreferences.contains("loginEmail")) {
                    String email = sharedPreferences.getString("loginEmail", "");
                    String password = sharedPreferences.getString("loginPassword", "");
                    signInStart(splashScreen.this,email,password);
                } else {
                    Intent i = new Intent(splashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        },4000);
    }
}