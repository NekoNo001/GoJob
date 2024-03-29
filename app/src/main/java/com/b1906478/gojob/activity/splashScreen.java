package com.b1906478.gojob.activity;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.b1906478.gojob.activity.LoginActivity.checkAccStatus;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.b1906478.gojob.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class splashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences2 = getSharedPreferences("Setting", MODE_PRIVATE);
        boolean darkMode = sharedPreferences2.getBoolean("darkMode",false);
        SharedPreferences sharedPreferences = this.getSharedPreferences("language", Context.MODE_PRIVATE);
        String language =  sharedPreferences.getString("language","en");
        setLocale(language);
        if(darkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        Binding= ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());
        Log.d(TAG, "onSuccess: 1" );
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null) {
                    checkAccStatus(splashScreen.this);
                } else {
                    Intent i = new Intent(splashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        },4000);
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}