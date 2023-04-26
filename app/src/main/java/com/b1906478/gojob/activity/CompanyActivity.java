package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityCompanyBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

public class CompanyActivity extends AppCompatActivity {

    ActivityCompanyBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        setup();
    }

    private void setup() {
        firebaseFirestore.collection("Company")
                .document(firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }
                        if (value != null && value.exists()) {
                            // Docuent already exists in Firestore
                            binding.btnname.setText(value.getString("Name"));
                            if(value.getString("imageUrl") != ""){
                                StorageReference mountainImagesRef = storageRef.child("Company/" + FirebaseAuth.getInstance().getUid() + ".PNG");
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get()
                                                .load(uri)
                                                .into(binding.imageView);
                                    }
                                });
                            }
                        }
                    }
                });
        binding.cardCurrentJobId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompanyActivity.this, jobListActivity.class);
                startActivity(i);
            }
        });
        binding.cardOldJobId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompanyActivity.this, jobListActivity.class);
                i.putExtra("old",true);
                startActivity(i);
            }
        });
        binding.DarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE);
                boolean darkMode = sharedPreferences.getBoolean("darkMode",false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyActivity.this);
                builder.setMessage(getString(R.string.do_you_want_to_turn_on_off_dark_mode_you_will_need_to_restart_the_app_for_the_change_to_take_effect))
                        .setTitle(R.string.confirm_change)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editor.putBoolean("darkMode", !darkMode);
                                editor.apply();
                                Log.d(TAG, "onMenuItemClick: "+ sharedPreferences.getBoolean("darkMode",false));
                                Intent i = new Intent(getApplicationContext(), splashScreen.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyActivity.this);
                builder.setMessage(getString(R.string.your_want_to_logout))
                        .setTitle(R.string.confirm)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                editor.remove("loginEmail");
                                editor.remove("loginPassword");
                                editor.apply();
                                Intent i = new Intent(getApplicationContext(), splashScreen.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                startActivity(i);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        binding.btnname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompanyActivity.this, CompanycolletionActivity.class);
                i.putExtra("edit",true);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        String ApplyView = getIntent().getStringExtra("ApplyView");
        if(ApplyView == null){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.please_click_back_again_to_exit, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);}else{
            finish();
        }
    }
}