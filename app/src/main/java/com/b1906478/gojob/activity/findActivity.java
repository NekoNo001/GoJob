package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.CardAdapter;
import com.b1906478.gojob.databinding.ActivityFindBinding;
import com.b1906478.gojob.model.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;

public class findActivity extends AppCompatActivity {
    ActivityFindBinding binding;
    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    CardStackLayoutManager manager;
    ArrayList<Company> companys;
    CardAdapter adapter;
    CardStackView cardStackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        companys =new ArrayList<>();
        setUpManager();
        setUpCardStackView();
        getCompany();
    }

    private void setUpManager(){
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {
                if(direction == Direction.Right){
                    Toast.makeText(findActivity.this, "Apply",Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Card: " + companys.get(manager.getTopPosition()-1).getJobPosition());
                }
                if(direction == Direction.Left){
                    Toast.makeText(findActivity.this, "Refuse",Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Card: " + companys.get(manager.getTopPosition()-1).getJobPosition());
                }
            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {
            }

            @Override
            public void onCardDisappeared(View view, int position) {
            }
        });
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        manager.setStackFrom(StackFrom.None);
        manager.setSwipeThreshold(0.2f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setOverlayInterpolator(new LinearInterpolator());
    }
    private void setUpCardStackView() {
        cardStackView = findViewById(R.id.cardStack);
        Button btnApply = binding.btnApply;
        Button btnRefuse = binding.btnrefuse;
        adapter = new CardAdapter(companys);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeAnimationSetting swipeAnimationSetting= new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(swipeAnimationSetting);
                cardStackView.swipe();;
            }
        });
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeAnimationSetting swipeAnimationSetting= new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(swipeAnimationSetting);
                cardStackView.swipe();
            }
        });
    }
    private void getCompany(){
        firebaseFirestore.collection("User")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Career")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firebaseFirestore.collection("Job")
                                        .whereEqualTo("careerId",document.getId())
                                        .whereEqualTo("status",true)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        areadyApply(document.getId());
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
    private void areadyApply(String JobId) {
        firebaseFirestore.collection("Job")
                .document(JobId)
                .collection("Application")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<String> documentId = new ArrayList<String>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documentId.add(document.getId());
                            }
                            Log.d(TAG, "onComplete: "+ documentId);
                            if (documentId.contains((firebaseauth.getCurrentUser().getUid())) != true) {
                                addJobToList(JobId);
                            }
                        }
                    }
                });
    }
    private void addJobToList(String JobId) {
        firebaseFirestore.collection("Job")
                .document(JobId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Company company = createCompanyFromDocument(documentSnapshot);
                        getNameAndAvatar(company);
                    }
                });
    }
    private Company createCompanyFromDocument(DocumentSnapshot documentSnapshot) {
        Company company = new Company();
        company.setJobPosition(documentSnapshot.getString("jobPosition"));
        company.setCompanySalary(documentSnapshot.getString("salary"));
        company.setCompanyTypeOfWork(documentSnapshot.getString("typeOfWork"));
        company.setCompanyNumberOfRecruits(documentSnapshot.getLong("numberOfRecruits"));
        company.setCompanyWorkExperience(documentSnapshot.getLong("workExperienceNeed"));
        company.setCompanyAdress(documentSnapshot.getString("address"));
        company.setCompanyGender(documentSnapshot.getString("gender"));
        company.setCompanyJobDescription(documentSnapshot.getString("jobDescription"));
        company.setCompanyCandidateRequirements(documentSnapshot.getString("candidateRequirements"));
        company.setCompanyBenefit(documentSnapshot.getString("benefit"));
        company.setCompanyLevel(documentSnapshot.getString("level"));
        company.setCompanyName(documentSnapshot.getString("CompanyId"));
        company.setCompanyCity(documentSnapshot.getString("city"));
        return company;
    }

    private void getNameAndAvatar(Company c) {
        Log.d(TAG, "onComplete: "+ c.getCompanyName());
        firebaseFirestore.collection("Company")
                .document(c.getCompanyName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        c.setCompanyName(documentSnapshot.getString("Name"));
                        if("".equals(documentSnapshot.getString("imageUrl"))){
                            c.setCompanyAvatar(null);
                        }else{
                            c.setCompanyAvatar(Uri.parse(documentSnapshot.getString("imageUrl")));
                        }
                        companys.add(c);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}