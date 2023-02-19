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
import android.widget.Button;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.CardAdapter;
import com.b1906478.gojob.databinding.ActivityFindBinding;
import com.b1906478.gojob.model.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
        manager.setSwipeThreshold(0.5f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setOverlayInterpolator(new LinearInterpolator());
    }
    private void EventChangeListener(){
            firebaseFirestore.collection("Company").whereEqualTo("Status",true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(error !=null){
                        Log.e("firestore error",error.getMessage());
                        return;
                    }
                    for (DocumentChange dc : value.getDocumentChanges()){
                        if (dc.getType() == DocumentChange.Type.ADDED){
                            Company c = new Company();
                            c.setJobPosition(dc.getDocument().getString("jobPosition"));
                            c.setCompanyName(dc.getDocument().getString("nameCompany"));
                            c.setCompanySalary(dc.getDocument().getString("salary"));
                            c.setCompanyTypeOfWork(dc.getDocument().getString("typeOfWork"));
                            c.setCompanyNumberOfRecruits(dc.getDocument().getLong("numberOfRecruits"));
                            c.setCompanyWorkExperience(dc.getDocument().getLong("workExperienceNeed"));
                            c.setCompanyAvatar(Uri.parse(dc.getDocument().getString("ImageUrl")));
                            companys.add(c);
                        }
                        adapter.notifyDataSetChanged();
                    }
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
                                firebaseFirestore.collection("Company")
                                        .whereEqualTo("CareerId",document.getId())
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
    private void areadyApply(String CompanyID) {
        firebaseFirestore.collection("Company")
                .document(CompanyID)
                .collection("Application")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "onComplete: "+ CompanyID);
                        ArrayList<String> documentId = new ArrayList<String>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documentId.add(document.getId());
                            }
                            Log.d(TAG, "onComplete: "+ documentId);
                            Log.d(TAG, "onComplete: "+ firebaseauth.getCurrentUser().getUid());
                            if (documentId.contains((firebaseauth.getCurrentUser().getUid())) != true) {
                                addCompanyToList(CompanyID);
                            }
                        }
                    }
                });
    }
    private void addCompanyToList(String CompanyID) {
        firebaseFirestore.collection("Company")
                .document(CompanyID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Company c = new Company();
                        c.setJobPosition(documentSnapshot.getString("jobPosition"));
                        c.setCompanyName(documentSnapshot.getString("nameCompany"));
                        c.setCompanySalary(documentSnapshot.getString("salary"));
                        c.setCompanyTypeOfWork(documentSnapshot.getString("typeOfWork"));
                        c.setCompanyNumberOfRecruits(documentSnapshot.getLong("numberOfRecruits"));
                        c.setCompanyWorkExperience(documentSnapshot.getLong("workExperienceNeed"));
                        c.setCompanyAvatar(Uri.parse(documentSnapshot.getString("ImageUrl")));
                        c.setCompanyAdress(documentSnapshot.getString("address"));
                        c.setCompanyGender(documentSnapshot.getString("gender"));
                        c.setCompanyJobDescription(documentSnapshot.getString("jobDescription"));
                        c.setCompanyCandidateRequirements(documentSnapshot.getString("candidateRequirements"));
                        c.setCompanyBenefit(documentSnapshot.getString("benefit"));
                        c.setCompanyLevel(documentSnapshot.getString("Level"));
                        companys.add(c);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}