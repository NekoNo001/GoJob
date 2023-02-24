package com.b1906478.gojob.activity;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityJobseekercolletion2Binding;
import com.b1906478.gojob.databinding.ActivityJobseekercolletionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class JobseakercolletionActivity2 extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    ActivityJobseekercolletion2Binding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobseekercolletion2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        ImageView leftArrow = findViewById(R.id.leftArrow);
        TextView txttoolbar = findViewById(R.id.txtToolbar);
        Button btn = findViewById(R.id.btnNext);
        txttoolbar.setText(R.string.Create_Your_CV);

        backbuttob(leftArrow);
        Onclicknext(btn);
    }

    public void backbuttob(ImageView a) {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void Onclicknext(Button a) {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String skill = binding.txtSkill.getText().toString();
                String cer = binding.txtcer.getText().toString();
                String interest = binding.txtinterest.getText().toString();
                String experience = binding.txtworkExperian.getText().toString();
                firebaseFirestore.collection("User")
                        .document(FirebaseAuth.getInstance().getUid())
                        .update("Skill", skill,
                    "Certificate", cer,
                                     "Interest", interest,
                                     "Work Experience", experience);
                Intent i = new Intent(JobseakercolletionActivity2.this,CareerfieldActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}

