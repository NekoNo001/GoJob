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
import android.widget.Button;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.CardAdapter;
import com.b1906478.gojob.databinding.ActivityFindBinding;
import com.b1906478.gojob.model.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
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
    private void getCompany(){
        firebaseFirestore.collection("Company")
                .whereEqualTo("Status", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Company c = new Company();
                                c.setJobPosition(document.getString("jobPosition"));
                                c.setNameCompany(document.getString("nameCompany"));
                                c.setSalary(document.getString("salary"));
                                c.setTypeOfWork(document.getString("typeOfWork"));
                                c.setNumberOfRecruits(document.getLong("numberOfRecruits"));
                                c.setWorkExperienceNeed(document.getLong("workExperienceNeed"));
                                c.setImageUrl(Uri.parse(document.getString("ImageUrl")));
                                c.setAdress(document.getString("address"));
                                c.setGender(document.getString("gender"));
                                c.setJobDescription(document.getString("jobDescription"));
                                c.setCandidateRequirements(document.getString("candidateRequirements"));
                                c.setBenefit(document.getString("benefit"));
                                c.setLevel(document.getString("Level"));
                                companys.add(c);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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
                            c.setNameCompany(dc.getDocument().getString("nameCompany"));
                            c.setSalary(dc.getDocument().getString("salary"));
                            c.setTypeOfWork(dc.getDocument().getString("typeOfWork"));
                            c.setNumberOfRecruits(dc.getDocument().getLong("numberOfRecruits"));
                            c.setWorkExperienceNeed(dc.getDocument().getLong("workExperienceNeed"));
                            c.setImageUrl(Uri.parse(dc.getDocument().getString("ImageUrl")));
                            companys.add(c);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
}