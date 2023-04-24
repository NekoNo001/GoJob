package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupActivity extends AppCompatActivity {


    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView leftArrow = findViewById(R.id.leftArrow);
        TextView txttoolbar = findViewById(R.id.txtToolbar);
        Button btn = findViewById(R.id.btnsignIn);
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        txttoolbar.setText(R.string.register);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.txtemail.getText().toString().trim();
                String pass = binding.txtpw.getText().toString().trim();
                Log.d(TAG, "onClick: " + email);

                // Regular expression pattern for validating email addresses
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                // Check if email and password are not empty, if email is valid, and if password is at least 6 characters long
                if(email.length() > 0 && pass.length() > 0 && email.matches(emailPattern) && pass.length() >= 6){
                    firebaseauth.createUserWithEmailAndPassword(email,pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(SignupActivity.this, R.string.CreateAccout ,Toast.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("loginEmail", email);
                                    editor.putString("loginPassword", pass);
                                    editor.apply();
                                    Intent i =new Intent(SignupActivity.this,SignupActivity2.class);
                                    startActivity(i);
                                };
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignupActivity.this,e.getMessage() ,Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else{
                    Toast.makeText(SignupActivity.this, R.string.enter_email_and_password,Toast.LENGTH_LONG).show();
                }
            }

        });

    }
}
