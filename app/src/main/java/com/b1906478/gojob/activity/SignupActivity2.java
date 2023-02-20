package com.b1906478.gojob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivitySignup2Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupActivity2 extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    ActivitySignup2Binding binding;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignup2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView leftArrow = findViewById(R.id.leftArrow);
        TextView txttoolbar = findViewById(R.id.txtToolbar);
        radioGroup = findViewById(R.id.radioGroup);
        Button btn = findViewById(R.id.btnsignIn);
        RadioButton btnseeker = findViewById(R.id.Job_seeker);
        RadioButton btnRecruiter = findViewById(R.id.Recruiter);
        txttoolbar.setText(R.string.Youare);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.Job_seeker:
                        btnseeker.setTextColor(getResources().getColor(R.color.Mint));
                        btnRecruiter.setTextColor(getResources().getColor(R.color.darkmodedfont));
                        break;
                    case R.id.Recruiter:
                        btnseeker.setTextColor(getResources().getColor(R.color.darkmodedfont));
                        btnRecruiter.setTextColor(getResources().getColor(R.color.Mint));
                        break;
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnseeker.isChecked()){
                    Intent i =new Intent(SignupActivity2.this,JobseakercolletionActivity.class);
                    startActivity(i);
                    finish();
                }if (btnRecruiter.isChecked()){
                    Intent i =new Intent(SignupActivity2.this,CompanycolletionActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });

    }
}
