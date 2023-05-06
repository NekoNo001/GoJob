package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityJobManageBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

public class JobManageActivity extends AppCompatActivity {

    ActivityJobManageBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityJobManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = firebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        setup();
        backbuttob();
    }

    private void setup() {
        firebaseFirestore.collection("Company")
                .document(firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            if(!snapshot.getString("imageUrl").equals("")) {
                                Picasso.get().load(snapshot.getString("imageUrl")).centerCrop()
                                        .resize(500, 500)
                                        .into(binding.avatarImg);
                            }
                        }
                    }
                });
        String jobid = getIntent().getStringExtra("jobId");
        firebaseFirestore.collection("Job")
                .document(jobid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            binding.findCadi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(JobManageActivity.this, viewJobSeekerActivity.class);
                                    String jobid = getIntent().getStringExtra("jobId");
                                    i.putExtra("careerId",snapshot.getString("careerId"));
                                    i.putExtra("jobId",jobid);
                                    startActivity(i);
                                }
                            });
                            binding.viewApply.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(JobManageActivity.this, viewJobSeekerActivity.class);
                                    String jobid = getIntent().getStringExtra("jobId");
                                    i.putExtra("careerId",snapshot.getString("careerId"));
                                    i.putExtra("jobId",jobid);
                                    i.putExtra("viewApply",true);
                                    startActivity(i);
                                }
                            });
                            binding.Accep.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(JobManageActivity.this, jobSeekerListActivity.class);
                                    String jobid = getIntent().getStringExtra("jobId");
                                    i.putExtra("jobId",jobid);
                                    startActivity(i);
                                }
                            });
                            binding.txtPosition.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(JobManageActivity.this, jobCreateActivity.class);
                                    i.putExtra("edit",true);
                                    String jobid = getIntent().getStringExtra("jobId");
                                    i.putExtra("jobId",jobid);
                                    startActivity(i);
                                }
                            });
                            binding.txtPosition.setText(snapshot.getString("jobPosition"));
                            binding.txtCity.setText(snapshot.getString("city"));
                            binding.txtLevel.setText(snapshot.getString("typeOfWork"));
                            binding.txtexp.setText(String.valueOf(snapshot.getLong("workExperienceNeed")) + getString(R.string.year));
                            ImageView statusimg = findViewById(R.id.statusimg);
                            if (snapshot.getBoolean("status")) {
                                statusimg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Mint, null)));
                                statusimg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(JobManageActivity.this);
                                        builder.setMessage("you will not be able to continue searching candidates for this job")
                                                .setTitle("Turn off finding?")
                                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                })
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        String jobid = getIntent().getStringExtra("jobId");
                                                        firebaseFirestore.collection("Job")
                                                                .document(jobid)
                                                                .update("status", false);
                                                        statusimg.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });
                            } else {
                                binding.txtPosition.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(JobManageActivity.this, R.string.you_cant_edit_this_job_anymore_please_create_new_job,Toast.LENGTH_SHORT).show();
                                    }
                                });
                                statusimg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(JobManageActivity.this, R.string.you_cant_turn_on_this_job_please_create_new_job ,Toast.LENGTH_SHORT).show();
                                    }
                                });
                                binding.findCadi.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(JobManageActivity.this, R.string.you_cant_find_candidates_to_this_job_please_create_new_job ,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        };
                    }
                });
        firebaseFirestore.collection("Job")
                .document(jobid)
                .collection("Application")
                .whereEqualTo("jobId",jobid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Error querying Firestore: ", error);
                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            binding.numberView.setText(String.valueOf(value.size()));
                        }else {
                            binding.numberView.setText("0");
                        }
                    }
                });
        firebaseFirestore.collection("Job")
                .document(jobid)
                .collection("AcceptList")
                .whereEqualTo("jobId",jobid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Error querying Firestore: ", error);
                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            binding.numberapply.setText(String.valueOf(value.size()));
                        }else {
                            binding.numberapply.setText("0");
                        }
                    }
                });
    }
    public void backbuttob() {
        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
