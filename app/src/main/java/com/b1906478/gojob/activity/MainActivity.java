package com.b1906478.gojob.activity;

import static com.b1906478.gojob.activity.LoginActivity.signInStart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityMainBinding;

import java.util.Locale;

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
            SharedPreferences sharedPreferences = this.getSharedPreferences("language", Context.MODE_PRIVATE);
            String language =  sharedPreferences.getString("language","en");
            if(language.equals("vi")){
                Vietnam.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                Vietnam.setTextColor(getResources().getColor(R.color.Mint));
                English.setTextColor(getResources().getColor(R.color.darkmodedfont));
                English.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
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
                    String language = "vi";
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("language", language);
                    editor.apply();
                    setLocale("vi"); // đổi ngôn ngữ hiển thị sang Tiếng Việt
                    recreate(); // khởi động lại activity để áp dụng ngôn ngữ mới
                }
            });
        English.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    English.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    English.setTextColor(view.getResources().getColor(R.color.Mint));
                    Vietnam.setTextColor(view.getResources().getColor(R.color.darkmodedfont));
                    Vietnam.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    String language = "en";
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("language", language);
                    editor.apply();
                    setLocale("en"); // đổi ngôn ngữ hiển thị sang Tiếng Anh
                    recreate(); // khởi động lại activity để áp dụng ngôn ngữ mới
                }
            });
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