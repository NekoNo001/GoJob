package com.b1906478.gojob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button btnsignIn = (Button) findViewById(R.id.btnsignIn);
        Button btnsignUp = (Button) findViewById(R.id.btnsignUp);
        TextView Vietnam = findViewById(R.id.btnVietnamese);
        TextView English = findViewById(R.id.btnEnglish);
        btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });

        Vietnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vietnam.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                Vietnam.setTextColor(view.getResources().getColor(R.color.Mint));
                English.setTextColor(view.getResources().getColor(R.color.darkmodedfont));
                English.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        });
        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                English.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                Vietnam.setTextColor(view.getResources().getColor(R.color.darkmodedfont));
                English.setTextColor(view.getResources().getColor(R.color.Mint));
                Vietnam.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        });
    }

}