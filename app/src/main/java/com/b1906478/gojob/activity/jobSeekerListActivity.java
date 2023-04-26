package com.b1906478.gojob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.CardAdapter;
import com.b1906478.gojob.adapter.JobSeekerAdapter;
import com.b1906478.gojob.databinding.ActivityJobSeekerListBinding;
import com.b1906478.gojob.model.Company;
import com.b1906478.gojob.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class jobSeekerListActivity extends AppCompatActivity {

    ActivityJobSeekerListBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<UserModel> userModels;
    JobSeekerAdapter jobSeekerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobSeekerListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userModels = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.ListJob);
        jobSeekerAdapter = new JobSeekerAdapter(userModels);
        recyclerView.setAdapter(jobSeekerAdapter);
        setup();
        getJobSeekerList();
    }

    private void getJobSeekerList() {
        String jobid = getIntent().getStringExtra("jobId");
        firebaseFirestore.collection("Job")
                .document(jobid)
                .collection("AcceptList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            getUser(documentSnapshot.getId(),documentSnapshot.getTimestamp("applyTime"));
                        }
                    }
                });
    }

    private void getUser(String userId, Timestamp applyTime) {
        firebaseFirestore.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel userModel = new UserModel();
                        userModel.setUsername(documentSnapshot.getString("Name"));
                        userModel.setUserEmail(documentSnapshot.getString("Email"));
                        userModel.setUserPhone(documentSnapshot.getString("Phone"));
                        userModel.setUserAvatar(documentSnapshot.getString("imageUrl"));
                        userModel.setUserId(userId);
                        userModel.setUserDOB(applyTime.toDate());
                        userModels.add(userModel);
                        Collections.sort(userModels, new Comparator<UserModel>() {
                            @Override
                            public int compare(UserModel c1, UserModel c2) {
                                return c2.getUserDOB().compareTo(c1.getUserDOB());
                            }
                        });
                        jobSeekerAdapter.notifyDataSetChanged();
                    }
                });
        binding.txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String searchText = textView.getText().toString().trim();
                    List<UserModel> filtereduserModels = new ArrayList<>();
                    for (UserModel userModel : userModels) {
                        if (userModel.getUsername().toLowerCase().contains(searchText.toLowerCase())) {
                            filtereduserModels.add(userModel);
                        }
                    }
                    jobSeekerAdapter.setCompanys(filtereduserModels);
                    jobSeekerAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
    }

    private void setup() {
        ImageView leftArrow = findViewById(R.id.leftArrow);
        TextView txttoolbar = findViewById(R.id.txtToolbar);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txttoolbar.setText(getString(R.string.accept_list));
    }
}