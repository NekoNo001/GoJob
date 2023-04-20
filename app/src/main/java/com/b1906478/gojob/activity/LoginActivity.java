package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView leftArrow = findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.txtemail.getText().toString().trim();
                String password = binding.txtpw.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, R.string.enter_email_and_password,Toast.LENGTH_SHORT).show();
                } else{
                    signInStart(LoginActivity.this,email,password);
                }

            }
        });
        binding.btnResetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.txtemail.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, R.string.enter_email,Toast.LENGTH_SHORT).show();
                }else{
                    firebaseauth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(LoginActivity.this, R.string.EmailSend,Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, R.string.email_not_registered,Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    public static void signInStart(Context context, String email, String password) {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        firebaseauth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(context, R.string.LoginSuccess,Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loginEmail", email);
                        editor.putString("loginPassword", password);
                        editor.apply();
                        checkAccStatus(context);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, R.string.Wrong,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void checkAccStatus(Context context) {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("User").document(firebaseauth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            continueCheckUser(context);
                        }else {
                            firebaseFirestore.collection("Company").document(firebaseauth.getCurrentUser().getUid()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists()){
                                                continueCheckCompany(context);
                                            }else{
                                                // Collection does not exist
                                                Intent I = new Intent(context, SignupActivity2.class);
                                                I.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                Toast.makeText(context, "You need to complete register", Toast.LENGTH_LONG).show();
                                                context.startActivity(I);
                                                ((Activity) context).finish();
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private static void continueCheckUser(Context context) {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("User").document(firebaseauth.getCurrentUser().getUid()).collection("Career").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        Intent I = new Intent(context, findActivity.class);
                        I.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(I);
                        ((Activity) context).finish();
                    } else {
                        // Collection does not exist
                        Intent I = new Intent(context, CareerfieldActivity.class);
                        I.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Toast.makeText(context, "You need to complete register",Toast.LENGTH_LONG).show();
                        context.startActivity(I);
                        ((Activity) context).finish();
                    }
                });
    }
    private static void continueCheckCompany(Context context) {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Job")
                .whereEqualTo("CompanyId",firebaseauth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        Intent I = new Intent(context, CompanyActivity.class);
                        I.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(I);
                        ((Activity) context).finish();
                    } else {
                        // Collection does not exist
                        Intent I = new Intent(context, CareerfieldActivity.class);
                        String UserType = "Company";
                        I.putExtra("Key",UserType);
                        I.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Toast.makeText(context, "You need to complete register",Toast.LENGTH_LONG).show();
                        context.startActivity(I);
                        ((Activity) context).finish();
                    }
                });
    }
}
