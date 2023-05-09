package com.b1906478.gojob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityViewJobSeekerInformationBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class viewJobSeekerInformationActivity extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    ActivityViewJobSeekerInformationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityViewJobSeekerInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseauth = FirebaseAuth.getInstance();
        String userId = getIntent().getStringExtra("userId");
        firebaseFirestore.collection("User").document(userId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                binding.addresstxt.setText(documentSnapshot.getString("Address"));
                                binding.textView1.setText(documentSnapshot.getString("Name"));
                                binding.textView2.setText(documentSnapshot.getString("Position"));
                                binding.txtaddress.setText(documentSnapshot.getString("Introduce"));
                                binding.universitytxt.setText(documentSnapshot.getString("University"));
                                binding.gpatxt.setText(documentSnapshot.getDouble("GPA").toString() + "/4.0");
                                binding.emailtxt.setText(documentSnapshot.getString("Email"));
                                binding.sdttxt.setText(documentSnapshot.getString("Phone"));
                                binding.dobtxt.setText(new SimpleDateFormat("d'/'MM'/'yyyy").format(documentSnapshot.getTimestamp("DOB").toDate()));
                                binding.webtxt.setText(documentSnapshot.getString("Website"));
                                binding.txtjobdescription.setText(documentSnapshot.getString("Skill"));
                                binding.txtcandidateRequirements.setText(documentSnapshot.getString("Certificate"));
                                binding.txtbenefit.setText(documentSnapshot.getString("Work Experience"));
                                binding.worktxt.setText(documentSnapshot.getString("Interest"));
                                if(!documentSnapshot.getString("imageUrl").equals("")){
                                    Picasso.get().load(documentSnapshot.getString("imageUrl")).resize(1360,1360).centerCrop().into(binding.imageView);
                                }
                                if(documentSnapshot.getString("Introduce").equals("")){
                                    binding.txtaddress.setVisibility(View.GONE);
                                    binding.line3.setVisibility(View.GONE);
                                }
                                if(documentSnapshot.getString("Website").equals("")){
                                    binding.webtxt.setVisibility(View.GONE);
                                    binding.webic.setVisibility(View.GONE);
                                }
                                if(documentSnapshot.getString("Skill").equals("")){
                                    binding.jobdescription.setVisibility(View.GONE);
                                    binding.txtjobdescription.setVisibility(View.GONE);
                                }
                                if(documentSnapshot.getString("Certificate").equals("")){
                                    binding.txtcandidateRequirements.setVisibility(View.GONE);
                                    binding.candidateRequirements.setVisibility(View.GONE);
                                }
                                if(documentSnapshot.getString("Work Experience").equals("")){
                                    binding.benefit.setVisibility(View.GONE);
                                    binding.txtbenefit.setVisibility(View.GONE);
                                }
                                if(documentSnapshot.getString("Interest").equals("")){
                                    binding.workexp.setVisibility(View.GONE);
                                    binding.worktxt.setVisibility(View.GONE);
                                }
                            }
                        });
        binding.btnrefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}