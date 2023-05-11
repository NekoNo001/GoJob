package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityCompanyRequestBinding;
import com.b1906478.gojob.databinding.ActivityFindBinding;
import com.b1906478.gojob.model.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class companyRequestActivity extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    ActivityCompanyRequestBinding binding;
    TextView JobPosition,NameCompany,Salary,TypeOfWork,WorkExperience,NumberOfRecruits, Level, gender, address, jobDescription, candidateRequirements, benefit;
    ImageView Avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCompanyRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        JobPosition = findViewById(R.id.textView1);
        NameCompany = findViewById(R.id.textView2);
        Salary = findViewById(R.id.txtsalary);
        TypeOfWork = findViewById(R.id.txtLevel);
        WorkExperience = findViewById(R.id.txtexp);
        NumberOfRecruits = findViewById(R.id.txtnumofrecut);
        Avatar = findViewById(R.id.imageView);
        Level = findViewById(R.id.txtTypejob);
        gender = findViewById(R.id.txtgender);
        address = findViewById(R.id.txtaddress);
        jobDescription = findViewById(R.id.txtjobdescription);
        candidateRequirements = findViewById(R.id.txtcandidateRequirements);
        benefit = findViewById(R.id.txtbenefit);
        setUpData();
        setupButton();
    }

    private void setupButton() {
        Button btnApply = findViewById(R.id.btnApply);
        Button btnrefuse = findViewById(R.id.btnrefuse);
        String jobId = getIntent().getStringExtra("jobId");
        btnrefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> data = new HashMap<>();
                data.put("status", "Apply");
                data.put("applyTime", FieldValue.serverTimestamp());
                data.put("jobId",jobId);
                firebaseFirestore.collection("Job")
                        .document(jobId.trim())
                        .collection("AcceptList")
                        .document(firebaseauth.getCurrentUser().getUid())
                        .set(data);
                Intent i = new Intent(companyRequestActivity.this,findActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void setUpData() {
        String jobId = getIntent().getStringExtra("jobId");
        Log.d(TAG, "new:"+ jobId);
        firebaseFirestore.collection("Job")
                .document(jobId.trim())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        JobPosition.setText(documentSnapshot.getString("jobPosition"));
                        NameCompany.setText(documentSnapshot.getString("CompanyId"));
                        Salary.setText(documentSnapshot.getString("salary"));
                        NumberOfRecruits.setText(String.valueOf(documentSnapshot.getLong("numberOfRecruits")) + getString(R.string.people));
                        if(documentSnapshot.getString("typeOfWork").equals("Full-Time")){
                            TypeOfWork.setText(R.string.full_time);
                        } else if (documentSnapshot.getString("typeOfWork").equals("Part-Time")) {
                            TypeOfWork.setText(R.string.part_time);
                        }else {
                            TypeOfWork.setText(R.string.intern);
                        }
                        WorkExperience.setText(String.valueOf(documentSnapshot.getLong("workExperienceNeed")) + getString(R.string.year));
                        Level.setText(documentSnapshot.getString("level"));
                        if(documentSnapshot.getString("gender").equals("Male")){
                            gender.setText(R.string.male);
                        } else if (documentSnapshot.getString("gender").equals("Female")) {
                            gender.setText(R.string.female);
                        }else {
                            gender.setText(R.string.not_required);
                        }
                        address.setText(documentSnapshot.getString("address"));
                        jobDescription.setText(documentSnapshot.getString("jobDescription"));
                        candidateRequirements.setText(documentSnapshot.getString("candidateRequirements"));
                        benefit.setText(documentSnapshot.getString("benefit"));
                        getNameAndAvatar(documentSnapshot.getString("CompanyId"));
                    }
                });
    }
    private void getNameAndAvatar(String CompanyId) {
        firebaseFirestore.collection("Company")
                .document(CompanyId.trim())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        NameCompany.setText(documentSnapshot.getString("Name"));
                        if(documentSnapshot.getString("imageUrl") != ""){
                            Picasso.get().load(documentSnapshot.getString("imageUrl")).resize(1360,1370).centerCrop().into(Avatar);
                        }
                    }
                });
    }
}