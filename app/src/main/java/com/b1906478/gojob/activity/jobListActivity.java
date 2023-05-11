package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.JobAdapter;
import com.b1906478.gojob.adapter.NotificationAdapter;
import com.b1906478.gojob.databinding.ActivityJobListBinding;
import com.b1906478.gojob.model.Company;
import com.b1906478.gojob.model.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class jobListActivity extends AppCompatActivity {


    ActivityJobListBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("language", Context.MODE_PRIVATE);
        String language =  sharedPreferences.getString("language","en");
        setLocale(language);
        super.onCreate(savedInstanceState);
        binding=ActivityJobListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        setup();
        getJobToList();
    }

    private void getJobToList() {
        RecyclerView recyclerView = findViewById(R.id.ListJob);
        List<Company> Companys = new ArrayList<>();
        String careerId = getIntent().getStringExtra("careerId");
        Log.d(TAG, "getJobToList: " + careerId);
        final JobAdapter jobAdapter = new JobAdapter(Companys,careerId);
        recyclerView.setAdapter(jobAdapter);
        Boolean old = getIntent().getBooleanExtra("old",false);
        if(careerId != null){
            Log.d(TAG, "getJobToList: " + careerId);
            firebaseFirestore.collection("Job")
                    .whereEqualTo("CompanyId",firebaseAuth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                Company c = new Company();
                                c.setCompanyJobPosition(documentSnapshot.getString("jobPosition"));
                                c.setCompanyCity(documentSnapshot.getString("city"));
                                Timestamp dateStart = documentSnapshot.getTimestamp("dateStart");
                                Date date = new Date(dateStart.toDate().getTime());
                                c.setDateStart(date);
                                Timestamp timestamp = documentSnapshot.getTimestamp("dateEnd");
                                Date dateEnd = new Date(timestamp.toDate().getTime());
                                c.setDateEnd(dateEnd);
                                c.setJobId(documentSnapshot.getId());
                                Companys.add(c);
                                Log.d(TAG, "onSuccess: " + c.getDateStart() + c.getDateEnd() + Companys.get(0).getCompanyJobPosition() + c.getCompanyCity());
                                Collections.sort(Companys, new Comparator<Company>() {
                                    @Override
                                    public int compare(Company c1, Company c2) {
                                        return c2.getDateStart().compareTo(c1.getDateStart());
                                    }
                                });
                                jobAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        else if(old.equals(true)){
            firebaseFirestore.collection("Job")
                    .whereEqualTo("CompanyId",firebaseAuth.getCurrentUser().getUid())
                    .whereEqualTo("status",false)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                Company c = new Company();
                                c.setCompanyJobPosition(documentSnapshot.getString("jobPosition"));
                                c.setCompanyCity(documentSnapshot.getString("city"));
                                Timestamp dateStart = documentSnapshot.getTimestamp("dateStart");
                                Date date = new Date(dateStart.toDate().getTime());
                                c.setDateStart(date);
                                Timestamp timestamp = documentSnapshot.getTimestamp("dateEnd");
                                Date dateEnd = new Date(timestamp.toDate().getTime());
                                c.setDateEnd(dateEnd);
                                c.setJobId(documentSnapshot.getId());
                                Companys.add(c);
                                Log.d(TAG, "onSuccess: " + c.getDateStart() + c.getDateEnd() + Companys.get(0).getCompanyJobPosition() + c.getCompanyCity());
                                Collections.sort(Companys, new Comparator<Company>() {
                                    @Override
                                    public int compare(Company c1, Company c2) {
                                        return c2.getDateStart().compareTo(c1.getDateStart());
                                    }
                                });
                                jobAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        else{
        firebaseFirestore.collection("Job")
                .whereEqualTo("CompanyId",firebaseAuth.getCurrentUser().getUid())
                .whereEqualTo("status",true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Company c = new Company();
                            c.setCompanyJobPosition(documentSnapshot.getString("jobPosition"));
                            c.setCompanyCity(documentSnapshot.getString("city"));
                            Timestamp dateStart = documentSnapshot.getTimestamp("dateStart");
                            Date date = new Date(dateStart.toDate().getTime());
                            c.setDateStart(date);
                            Timestamp timestamp = documentSnapshot.getTimestamp("dateEnd");
                            Date dateEnd = new Date(timestamp.toDate().getTime());
                            c.setDateEnd(dateEnd);
                            c.setJobId(documentSnapshot.getId());
                            Companys.add(c);
                            Log.d(TAG, "onSuccess: " + c.getDateStart() + c.getDateEnd() + Companys.get(0).getCompanyJobPosition() + c.getCompanyCity());
                            Collections.sort(Companys, new Comparator<Company>() {
                                @Override
                                public int compare(Company c1, Company c2) {
                                    return c2.getDateStart().compareTo(c1.getDateStart());
                                }
                            });
                            jobAdapter.notifyDataSetChanged();
                        }
                    }
                });}
        binding.txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String searchText = textView.getText().toString().trim();
                    List<Company> filteredCompanys = new ArrayList<>();
                    for (Company company : Companys) {
                        if (company.getCompanyJobPosition().toLowerCase().contains(searchText.toLowerCase())) {
                            filteredCompanys.add(company);
                        }
                    }
                    jobAdapter.setCompanys(filteredCompanys);
                    jobAdapter.notifyDataSetChanged();
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
        Boolean old = getIntent().getBooleanExtra("old",false);
        String careerId = getIntent().getStringExtra("careerId");
        if(old.equals(true)){
            txttoolbar.setText(getString(R.string.previous_jobs));
            binding.btnNewJob.setVisibility(View.GONE);
        }else if(careerId != null){
            txttoolbar.setText(R.string.choice_pattern);
            binding.btnNewJob.setVisibility(View.GONE);
            binding.LayoutCreate.setVisibility(View.VISIBLE);
            binding.LayoutCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(jobListActivity.this,jobCreateActivity.class);
                    i.putExtra("careerId",careerId);
                    startActivity(i);
                }
            });
        }
        else {txttoolbar.setText(getString(R.string.job_on_active));
        binding.btnNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(jobListActivity.this,CareerfieldActivity.class);
                String UserType = "Company";
                i.putExtra("Key",UserType);
                startActivity(i);}
        });}
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
