package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.CardJobSeekerAdapter;
import com.b1906478.gojob.databinding.ActivityViewJobSeekerBinding;
import com.b1906478.gojob.model.Company;
import com.b1906478.gojob.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class viewJobSeekerActivity extends AppCompatActivity {

    CardStackLayoutManager manager;
    ActivityViewJobSeekerBinding binding;
    ArrayList<UserModel> userModels;
    CardJobSeekerAdapter adapter;
    CardStackView cardStackView;
    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViewJobSeekerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        cardStackView = findViewById(R.id.cardStack);
        userModels = new ArrayList<>();
        setUpManager();
        setup();
        getUserList();
    }

    private void getUserList() {
        String careerId = getIntent().getStringExtra("careerId");
        if(getIntent().getBooleanExtra("viewApply",false)){
            binding.btnReset.setText(getString(R.string.view_apply));
            firebaseFirestore.collection("User")
                    .orderBy("Priority", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                checkApply(documentSnapshot);
                            }
                        }
                    });
        }else{
        firebaseFirestore.collection("User")
                .whereEqualTo("status",true)
                .orderBy("Priority", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            checkCareer(documentSnapshot,careerId);
                        }
                    }
                });}
    }

    private void checkCareer(DocumentSnapshot documentSnapshot, String careerId) {
        firebaseFirestore.collection("User")
                .document(documentSnapshot.getId())
                .collection("Career")
                .document(careerId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        if(d.exists()){
                            checkApply(documentSnapshot);
                        }
                    }
                });
    }

    private void checkApply(DocumentSnapshot documentSnapshot) {
        String jobid = getIntent().getStringExtra("jobId");
        if(getIntent().getBooleanExtra("viewApply",false)){
            firebaseFirestore.collection("Job")
                    .document(jobid)
                    .collection("Application")
                    .document(documentSnapshot.getId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot d) {
                            if(d.exists()){
                                UserModel um = new UserModel();
                                if(documentSnapshot.getString("imageUrl").equals("")){
                                    um.setUserAvatar("None".trim());
                                }else{
                                    um.setUserAvatar(documentSnapshot.getString("imageUrl"));
                                }
                                if(documentSnapshot.getString("Introduce").equals("")){
                                    um.setUserIntroduce("None".trim());
                                }else{
                                    um.setUserIntroduce(documentSnapshot.getString("Introduce"));
                                }
                                if(documentSnapshot.getString("Website").equals("")){
                                    um.setUserWeb("None".trim());
                                }else{
                                    um.setUserWeb(documentSnapshot.getString("Website"));
                                }
                                if(documentSnapshot.getString("Skill").equals("")){
                                    um.setUserSkill("None".trim());
                                }else{
                                    um.setUserSkill(documentSnapshot.getString("Skill"));
                                }
                                if(documentSnapshot.getString("Certificate").equals("")){
                                    um.setUserCer("None".trim());
                                }else{
                                    um.setUserCer(documentSnapshot.getString("Certificate"));
                                }
                                if(documentSnapshot.getString("Interest").equals("")){
                                    um.setUserInterest("None".trim());
                                }else{
                                    um.setUserInterest(documentSnapshot.getString("Interest"));
                                }
                                if(documentSnapshot.getString("Work Experience").equals("")){
                                    um.setUserExperience("None".trim());
                                }else{
                                    um.setUserExperience(documentSnapshot.getString("Work Experience"));
                                }
                                um.setUsername(documentSnapshot.getString("Name"));
                                um.setUserPosition(documentSnapshot.getString("Position"));
                                um.setUserUniversity(documentSnapshot.getString("University"));
                                um.setUserGPA((documentSnapshot.getDouble("GPA")));
                                um.setUserEmail(documentSnapshot.getString("Email"));
                                um.setUserPhone(documentSnapshot.getString("Phone"));
                                um.setUserDOB(documentSnapshot.getTimestamp("DOB").toDate());
                                um.setUserId(documentSnapshot.getId());
                                um.setUserAddress(documentSnapshot.getString("Address"));
                                um.setPriority(documentSnapshot.getLong("Priority"));
                                Log.d(TAG, "onComplete: " + um.getUserPosition());
                                userModels.add(um);
                                Collections.sort(userModels, new Comparator<UserModel>() {
                                    @Override
                                    public int compare(UserModel c1, UserModel c2) {
                                        return Integer.compare(c1.getPriority().intValue(), c2.getPriority().intValue());
                                    }
                                });
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }else{
        firebaseFirestore.collection("Job")
                .document(jobid)
                .collection("Application")
                .document(documentSnapshot.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        if(!d.exists()){
                            CheckAccept(documentSnapshot);
                        }
                    }
                });}
    }

    private void CheckAccept(DocumentSnapshot documentSnapshot) {
        String jobid = getIntent().getStringExtra("jobId");
        firebaseFirestore.collection("Job")
                .document(jobid)
                .collection("AcceptList")
                .document(documentSnapshot.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        if(!d.exists()){
                            {
                                UserModel um = new UserModel();
                                if(documentSnapshot.getString("imageUrl").equals("")){
                                    um.setUserAvatar("None".trim());
                                }else{
                                    um.setUserAvatar(documentSnapshot.getString("imageUrl"));
                                }
                                if(documentSnapshot.getString("Introduce").equals("")){
                                    um.setUserIntroduce("None".trim());
                                }else{
                                    um.setUserIntroduce(documentSnapshot.getString("Introduce"));
                                }
                                if(documentSnapshot.getString("Website").equals("")){
                                    um.setUserWeb("None".trim());
                                }else{
                                    um.setUserWeb(documentSnapshot.getString("Website"));
                                }
                                if(documentSnapshot.getString("Skill").equals("")){
                                    um.setUserSkill("None".trim());
                                }else{
                                    um.setUserSkill(documentSnapshot.getString("Skill"));
                                }
                                if(documentSnapshot.getString("Certificate").equals("")){
                                    um.setUserCer("None".trim());
                                }else{
                                    um.setUserCer(documentSnapshot.getString("Certificate"));
                                }
                                if(documentSnapshot.getString("Interest").equals("")){
                                    um.setUserInterest("None".trim());
                                }else{
                                    um.setUserInterest(documentSnapshot.getString("Interest"));
                                }
                                if(documentSnapshot.getString("Work Experience").equals("")){
                                    um.setUserExperience("None".trim());
                                }else{
                                    um.setUserExperience(documentSnapshot.getString("Work Experience"));
                                }
                                Log.d(TAG, "on1234: "+um.getUserAvatar());
                                um.setUsername(documentSnapshot.getString("Name"));
                                um.setUserPosition(documentSnapshot.getString("Position"));
                                um.setUserUniversity(documentSnapshot.getString("University"));
                                um.setUserGPA((documentSnapshot.getDouble("GPA")));
                                um.setUserEmail(documentSnapshot.getString("Email"));
                                um.setUserPhone(documentSnapshot.getString("Phone"));
                                um.setUserDOB(documentSnapshot.getTimestamp("DOB").toDate());
                                um.setUserId(documentSnapshot.getId());
                                um.setUserAddress(documentSnapshot.getString("Address"));
                                um.setPriority(documentSnapshot.getLong("Priority"));
                                Log.d(TAG, "onComplete: " + um.getUserPosition());
                                userModels.add(um);
                                Collections.sort(userModels, new Comparator<UserModel>() {
                                    @Override
                                    public int compare(UserModel c1, UserModel c2) {
                                        return Integer.compare(c1.getPriority().intValue(), c2.getPriority().intValue());
                                    }
                                });
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });}

    private void setup() {
        Button backBtn = binding.btnMenu;
        binding.btnReset.setText(getString(R.string.find_candidates));
        MaterialButton btnApply = binding.btnApply;
        MaterialButton btnRefuse = binding.btnrefuse;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeAnimationSetting swipeAnimationSetting= new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(swipeAnimationSetting);
                cardStackView.swipe();
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
        adapter = new CardJobSeekerAdapter(userModels);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }

    private void setUpManager(){
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
            }
            @Override
            public void onCardSwiped(Direction direction) {
                String jobid = getIntent().getStringExtra("jobId");
                if(direction == Direction.Right){
                    if(getIntent().getBooleanExtra("viewApply",false)){
                        Map<String, Object> data = new HashMap<>();
                        data.put("status", "Apply");
                        data.put("jobId",jobid);
                        data.put("applyTime", FieldValue.serverTimestamp());
                        firebaseFirestore.collection("Job")
                                .document(jobid)
                                .collection("AcceptList")
                                .document(userModels.get(manager.getTopPosition()-1).getUserId())
                                .set(data);
                        firebaseFirestore.collection("User")
                                .document(userModels.get(manager.getTopPosition()-1).getUserId())
                                .update("Priority",15);
                        firebaseFirestore.collection("Job")
                                .document(jobid)
                                .collection("Application")
                                .document(userModels.get(manager.getTopPosition()-1).getUserId())
                                .delete();
                        firebaseFirestore.collection("Job").document(jobid).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        getNotificationAccept(documentSnapshot);
                                    }
                                });
                    }
                    else{
                    firebaseFirestore.collection("Job").document(jobid).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            getImage(documentSnapshot);
                                        }
                                    });}
                }
                if(direction == Direction.Left){
                    if(getIntent().getBooleanExtra("viewApply",false)){
                    String userId = userModels.get(manager.getTopPosition()-1).getUserId();
                    firebaseFirestore.collection("User")
                            .document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                // Get the current priority value
                                int currentPriority = documentSnapshot.getLong("Priority").intValue();
                                // Update the "Priority" field to the new value
                                int newPriority = currentPriority - 1;
                                if(newPriority == 0){
                                    firebaseFirestore.collection("User")
                                            .document(userId)
                                            .update("Priority", 15,
                                                    "status",false)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Map<String, Object> Notification = new HashMap<>();
                                                    Notification.put("message", "M101".trim());
                                                    Notification.put("notificationTime", FieldValue.serverTimestamp());
                                                    Notification.put("status",false);
                                                    Notification.put("imageUrl", "https://firebasestorage.googleapis.com/v0/b/gojob-f9aa4.appspot.com/o/Company%2Flogo.png?alt=media&token=e205e05d-271b-47d6-9cb0-604e89fc8cfe");
                                                    firebaseFirestore.collection("User")
                                                            .document(userModels.get(manager.getTopPosition()-1).getUserId())
                                                            .collection("Notification")
                                                            .document()
                                                            .set(Notification);
                                                }
                                            });
                                }else {
                                    firebaseFirestore.collection("User")
                                            .document(userId)
                                            .update("Priority", newPriority);
                                    firebaseFirestore.collection("Job").document(jobid).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    getNotificationRefuse(documentSnapshot);
                                                }
                                            });
                                }
                            });
                    firebaseFirestore.collection("Job")
                            .document(jobid)
                            .collection("Application")
                            .document(userModels.get(manager.getTopPosition()-1).getUserId())
                            .delete();
                    }
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

    private void getImage(DocumentSnapshot documentSnapshot) {
        firebaseFirestore.collection("Company")
                .document(firebaseauth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        Map<String, Object> Notification = new HashMap<>();
                        Notification.put("message","M102".trim());
                        Notification.put("jobId", documentSnapshot.getId());
                        Notification.put("jobPosition", documentSnapshot.getString("jobPosition"));
                        Notification.put("notificationTime", FieldValue.serverTimestamp());
                        Notification.put("status",false);
                        Notification.put("imageUrl", d.getString("imageUrl"));
                        firebaseFirestore.collection("User")
                                .document(userModels.get(manager.getTopPosition()-1).getUserId())
                                .collection("Notification")
                                .document()
                                .set(Notification);
                    }
                });

    }
    private void getNotificationRefuse(DocumentSnapshot documentSnapshot) {
        firebaseFirestore.collection("Company")
                .document(firebaseauth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        Map<String, Object> Notification = new HashMap<>();
                        Notification.put("message", "M103".trim());
                        Notification.put("jobPosition", documentSnapshot.getString("jobPosition"));
                        Notification.put("notificationTime", FieldValue.serverTimestamp());
                        Notification.put("status",false);
                        Notification.put("imageUrl", d.getString("imageUrl"));
                        firebaseFirestore.collection("User")
                                .document(userModels.get(manager.getTopPosition()-1).getUserId())
                                .collection("Notification")
                                .document()
                                .set(Notification);
                    }
                });

    }
    private void getNotificationAccept(DocumentSnapshot documentSnapshot) {
        firebaseFirestore.collection("Company")
                .document(firebaseauth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        Map<String, Object> Notification = new HashMap<>();
                        Notification.put("message", "M104".trim());;
                        Notification.put("jobPosition", documentSnapshot.getString("jobPosition"));
                        Notification.put("notificationTime", FieldValue.serverTimestamp());
                        Notification.put("status",false);
                        Notification.put("imageUrl", d.getString("imageUrl"));
                        firebaseFirestore.collection("User")
                                .document(userModels.get(manager.getTopPosition()-1).getUserId())
                                .collection("Notification")
                                .document()
                                .set(Notification);
                    }
                });

    }
}