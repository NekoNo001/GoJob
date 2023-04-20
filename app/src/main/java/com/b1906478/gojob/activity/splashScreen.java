package com.b1906478.gojob.activity;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.b1906478.gojob.activity.LoginActivity.checkAccStatus;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.b1906478.gojob.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

public class splashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences2 = getSharedPreferences("Setting", MODE_PRIVATE);
        boolean darkMode = sharedPreferences2.getBoolean("darkMode",false);
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
}