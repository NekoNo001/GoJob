package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.CardAdapter;
import com.b1906478.gojob.databinding.ActivityFindBinding;
import com.b1906478.gojob.model.Company;
import com.b1906478.gojob.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class findActivity extends AppCompatActivity {
    int slectionItemByCity =0;
    int slectionItemByType =0;
    int slectionItemByExp =0;
    int choice =0;
    private View dialogView;
    ActivityFindBinding binding;
    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    CardStackLayoutManager manager;
    ArrayList<Company> companys;
    CardAdapter adapter;
    CardStackView cardStackView;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("language", Context.MODE_PRIVATE);
        String language =  sharedPreferences.getString("language","en");
        setLocale(language);
        super.onCreate(savedInstanceState);
        binding=ActivityFindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView reddot = binding.reddot;
        companys = new ArrayList<>();
        setNotificationChange(reddot);
        String ApplyView = getIntent().getStringExtra("ApplyView");
        if(ApplyView != null){
            setUpManager();
            setUpApplyView();
        }else{
        setUpManager();
        setUpCardStackView();
        getCompany();}
    }

    private void setUpApplyView() {
        cardStackView = findViewById(R.id.cardStack);
        MaterialButton btnApply = binding.btnApply;
        MaterialButton btnRefuse = binding.btnrefuse;
        Button btnSort = binding.btnMenu;
        Button btnNofication = binding.btnNofication;
        btnSort.setVisibility(View.INVISIBLE);
        btnNofication.setVisibility(View.INVISIBLE);
        btnRefuse.setIcon(getResources().getDrawable(R.drawable.backarrow_ic));
        btnApply.setIcon(getResources().getDrawable(R.drawable.rightarrow_ic));
        btnRefuse.setOnClickListener(new View.OnClickListener() {
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
        adapter = new CardAdapter(companys);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        firebaseFirestore.collection("Job").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            Log.d(TAG, "test: "+documentSnapshot.getId());
                            setUpApplyViewCompany(documentSnapshot);
                        }
                    }
                });

        }

    private void setUpApplyViewCompany(QueryDocumentSnapshot documentSnapshot) {
            firebaseFirestore.collection("Job").document(documentSnapshot.getId())
                    .collection("Application")
                    .document(firebaseauth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Company company = createCompanyFromDocumentView(documentSnapshot,documentSnapshot.getId(),document.getTimestamp("applyTime"));
                                    Log.d(TAG, "test apply: "+documentSnapshot.getId() +documentSnapshot.getString("jobPosition")+ documentSnapshot.getTimestamp("applyTime"));
                                    getNameAndAvatar(company);
                                }else{
                                    firebaseFirestore.collection("Job").document(documentSnapshot.getId())
                                            .collection("AcceptList")
                                            .document(firebaseauth.getCurrentUser().getUid())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            Company company = createCompanyFromDocumentView(documentSnapshot, documentSnapshot.getId(), document.getTimestamp("applyTime"));
                                                            Log.d(TAG, "test apply: " + documentSnapshot.getId() + documentSnapshot.getString("jobPosition") + documentSnapshot.getTimestamp("applyTime"));
                                                            getNameAndAvatar(company);
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
    }

    private void setNotificationChange(ImageView reddot) {
        firebaseFirestore.collection("User")
                .document(firebaseauth.getCurrentUser().getUid())
                .collection("Notification")
                .whereEqualTo("status", false)
                .limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Error querying Firestore: ", error);
                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (!dc.getDocument().getBoolean("status")) {
                                    reddot.setVisibility(View.VISIBLE);
                                } else {
                                    reddot.setVisibility(View.INVISIBLE);
                                }
                            }
                        } else {
                            reddot.setVisibility(View.INVISIBLE);
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
                String ApplyView = getIntent().getStringExtra("ApplyView");
                if(direction == Direction.Right && ApplyView == null){
                    Log.d(TAG,"Card: " + companys.get(manager.getTopPosition()-1).getCompanyJobPosition());
                    Map<String, Object> data = new HashMap<>();
                    data.put("status", "Apply");
                    data.put("jobId",companys.get(manager.getTopPosition()-1).getJobId());
                    data.put("applyTime", FieldValue.serverTimestamp());
                    firebaseFirestore.collection("Job")
                            .document(companys.get(manager.getTopPosition()-1).getJobId())
                            .collection("Application")
                            .document(firebaseauth.getCurrentUser().getUid())
                            .set(data);
                }
                if(direction == Direction.Left){
                    Log.d(TAG,"Card: " + companys.get(manager.getTopPosition()-1).getCompanyJobPosition());
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
        //set up button
        Button btnReset =  binding.btnReset;
        Button btnApply = binding.btnApply;
        Button btnRefuse = binding.btnrefuse;
        Button btnSort = binding.btnMenu;
        Button btnNofication = binding.btnNofication;
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
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
        btnNofication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(findActivity.this, NotificationActivity.class);
                startActivity(i);
            }
        });

        //set up cardview
        adapter = new CardAdapter(companys);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        //set up drawlayout
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView nameTxt = headerView.findViewById(R.id.nameTxt);
        TextView jobPositiontxt = headerView.findViewById(R.id.jobPositionTxt);
        ImageView avatarImg = headerView.findViewById(R.id.avatarImg);
        nameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(findActivity.this, JobseakercolletionActivity.class);
                String Edit = "Edit";
                i.putExtra("Key",Edit);
                startActivity(i);
            }
        });
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        MenuItem btnDarkMode = navigationView.getMenu().findItem(R.id.nav_mode);
        MenuItem btnLogout = navigationView.getMenu().findItem(R.id.nav_logout);
        MenuItem btnCareers = navigationView.getMenu().findItem(R.id.nav_Career);
        MenuItem btnSearch = navigationView.getMenu().findItem(R.id.nav_searching);
        MenuItem btnfilter = navigationView.getMenu().findItem(R.id.nav_filter);
        MenuItem btnApplyView = navigationView.getMenu().findItem(R.id.nav_applyView);
        MenuItem btnLanguge = navigationView.getMenu().findItem(R.id.nav_language);
        Switch btnStatus = headerView.findViewById(R.id.StatusBtn);
        setProfile(nameTxt,jobPositiontxt,avatarImg,btnStatus);
        btnApplyView.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(findActivity.this,findActivity.class);
                intent.putExtra("ApplyView","ApplyView");
                startActivity(intent);
                drawerLayout.close();
                return false;
            }
        });
        btnDarkMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                SharedPreferences sharedPreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE);
                boolean darkMode = sharedPreferences.getBoolean("darkMode",false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                AlertDialog.Builder builder = new AlertDialog.Builder(findActivity.this);
                builder.setMessage(getString(R.string.do_you_want_to_turn_on_off_dark_mode_you_will_need_to_restart_the_app_for_the_change_to_take_effect))
                        .setTitle(R.string.confirm_change)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editor.putBoolean("darkMode", !darkMode);
                                editor.apply();
                                Log.d(TAG, "onMenuItemClick: "+ sharedPreferences.getBoolean("darkMode",false));
                                Intent i = new Intent(getApplicationContext(), splashScreen.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                startActivity(i);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        btnLanguge.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // create an array of options
                final String[] options = {"English", "Tiếng việt"};
                // create an instance of AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(findActivity.this);
                // set the title of the dialog
                builder.setTitle(getString(R.string.choice_city_to_sort))
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });

                SharedPreferences sharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE);
                String language = sharedPreferences.getString("language", "en");
                if(language.equals("vi")){
                    choice = 1;
                }
                builder.setSingleChoiceItems(options, choice , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                        }
                });

                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(choice == 0 ){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("language", "en");
                            editor.apply();
                            setLocale("en"); // đổi ngôn ngữ hiển thị
                            recreate();
                        }else{
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("language", "vi");
                            editor.apply();
                            setLocale("vi"); // đổi ngôn ngữ hiển thị
                            recreate();
                        }// khởi động lại activity để áp dụng ngôn ngữ mới
                    }
                });

                // create the dialog and show it
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        btnLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                AlertDialog.Builder builder = new AlertDialog.Builder(findActivity.this);
                builder.setMessage(getString(R.string.your_want_to_logout))
                        .setTitle(R.string.confirm)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                editor.remove("loginEmail");
                                editor.remove("loginPassword");
                                editor.apply();
                                Intent i = new Intent(getApplicationContext(), splashScreen.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        btnCareers.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(findActivity.this, CareerfieldActivity.class);
                i.putExtra("Edit","true");
                startActivity(i);
                return false;
        }
    });
        btnSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(findActivity.this, SearchActivity.class);
                i.putExtra("Edit","true");
                startActivity(i);
                return false;
            }});

        btnfilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showDialog();
                return false;
            }
        });
        btnStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Do something when switch is on/checked
                    firebaseFirestore.collection("User")
                            .document(firebaseauth.getCurrentUser().getUid())
                            .update("status",true);
                } else {
                    // Do something when switch is off/unchecked
                    firebaseFirestore.collection("User")
                            .document(firebaseauth.getCurrentUser().getUid())
                            .update("status",false);
                }
            }
        });
    }

    private void setProfile(TextView nameTxt, TextView jobPositiontxt, ImageView avatarImg, Switch btnStatus) {
        avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(findActivity.this, JobseakercolletionActivity.class);
                String Edit = "Edit";
                i.putExtra("Key",Edit);
                startActivity(i);
            }
        });
        firebaseFirestore.collection("User").document(firebaseauth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            nameTxt.setText(snapshot.getString("Name"));
                            jobPositiontxt.setText(snapshot.getString("Position"));
                            btnStatus.setChecked(snapshot.getBoolean("status"));
                            if(snapshot.getString("imageUrl") != "") {
                                Picasso.get().load(snapshot.getString("imageUrl")).resize(1360, 1360).centerCrop().into(avatarImg);
                        }
                    }
                };

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
                            slectionItemByType = getIntent().getIntExtra("slectionItemByType",0);
                            slectionItemByCity = getIntent().getIntExtra("slectionItemByCity",0);
                            slectionItemByExp = getIntent().getIntExtra("slectionItemByExp",0);
                            String typeString = null;
                            String CityString = null;
                            if(slectionItemByType==1){
                                typeString = "Full-Time";
                            }else
                                if(slectionItemByType==2){
                                typeString = "Part-Time";
                            }else
                                if(slectionItemByType==3){
                                typeString = "Intern";
                            }
                            if(slectionItemByCity==1){
                                CityString = "Ho Chi Minh";
                            } else
                                if (slectionItemByCity==2){
                                CityString = "Ha Noi";
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //loc theo loai cv
                                //loai
                                if(typeString != null && CityString == null && slectionItemByExp ==0 ){
                                    Button btnReset = findViewById(R.id.btnReset);
                                    btnReset.setText(typeString);
                                    btnReset.setVisibility(View.VISIBLE);
                                    firebaseFirestore.collection("Job")
                                            .whereEqualTo("careerId", document.getId())
                                            .whereEqualTo("status", true)
                                            .whereEqualTo("typeOfWork",typeString)
                                            .orderBy("dateStart", Query.Direction.DESCENDING)
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
                                }else
                                //loai + tp
                                if(typeString != null && CityString != null && slectionItemByExp ==0){
                                        slectionItemByType = getIntent().getIntExtra("slectionItemByType",0);
                                        Button btnReset = findViewById(R.id.btnReset);
                                        btnReset.setText(typeString +", "+ CityString);
                                        btnReset.setVisibility(View.VISIBLE);
                                        firebaseFirestore.collection("Job")
                                                .whereEqualTo("careerId", document.getId())
                                                .whereEqualTo("status", true)
                                                .whereEqualTo("typeOfWork",typeString)
                                                .whereEqualTo("city",CityString)
                                                .orderBy("dateStart", Query.Direction.DESCENDING)
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
                                    }else
                                //loai + tp + year
                                if(typeString != null && CityString != null && slectionItemByExp <4 && slectionItemByExp != 0){
                                        slectionItemByType = getIntent().getIntExtra("slectionItemByType",0);
                                        Button btnReset = findViewById(R.id.btnReset);
                                        String temp  = String.valueOf(slectionItemByExp-1);
                                        btnReset.setText(typeString +", "+ CityString +", "+ temp + getString(R.string.year));
                                        btnReset.setVisibility(View.VISIBLE);
                                        firebaseFirestore.collection("Job")
                                                .whereEqualTo("careerId", document.getId())
                                                .whereEqualTo("status", true)
                                                .whereEqualTo("typeOfWork",typeString)
                                                .whereEqualTo("city",CityString)
                                                .whereEqualTo("workExperienceNeed",slectionItemByExp-1)
                                                .orderBy("dateStart", Query.Direction.DESCENDING)
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
                                    }else
                                //loai + tp + year >= 3
                                if(typeString != null && CityString != null && slectionItemByExp == 4 && slectionItemByExp != 0){
                                    slectionItemByType = getIntent().getIntExtra("slectionItemByType",0);
                                    Button btnReset = findViewById(R.id.btnReset);
                                    btnReset.setText(typeString +", "+ CityString +", 3+" + getString(R.string.year) );
                                    btnReset.setVisibility(View.VISIBLE);
                                    firebaseFirestore.collection("Job")
                                            .whereEqualTo("careerId", document.getId())
                                            .whereEqualTo("status", true)
                                            .whereEqualTo("typeOfWork",typeString)
                                            .whereEqualTo("city",CityString)
                                            .orderBy("dateStart", Query.Direction.DESCENDING)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if(document.getLong("workExperienceNeed") >=3){
                                                                areadyApply(document.getId());
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                }else
                                //loai + year
                                if(typeString != null && CityString == null && slectionItemByExp <4 && slectionItemByExp != 0){
                                            slectionItemByType = getIntent().getIntExtra("slectionItemByType",0);
                                            Button btnReset = findViewById(R.id.btnReset);
                                            String temp  = String.valueOf(slectionItemByExp-1);
                                            btnReset.setText(typeString +", "+ temp + getString(R.string.year));
                                            btnReset.setVisibility(View.VISIBLE);
                                            firebaseFirestore.collection("Job")
                                                    .whereEqualTo("careerId", document.getId())
                                                    .whereEqualTo("status", true)
                                                    .whereEqualTo("typeOfWork",typeString)
                                                    .whereEqualTo("workExperienceNeed",slectionItemByExp-1)
                                                    .orderBy("dateStart", Query.Direction.DESCENDING)
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
                                        }else
                                //loai + year >=3
                                if(typeString != null && CityString == null && slectionItemByExp == 4 && slectionItemByExp != 0){
                                            slectionItemByType = getIntent().getIntExtra("slectionItemByType",0);
                                            Button btnReset = findViewById(R.id.btnReset);
                                            btnReset.setText(typeString +", 3+" + getString(R.string.year) );
                                            btnReset.setVisibility(View.VISIBLE);
                                            firebaseFirestore.collection("Job")
                                                    .whereEqualTo("careerId", document.getId())
                                                    .whereEqualTo("status", true)
                                                    .whereEqualTo("typeOfWork",typeString)
                                                    .orderBy("dateStart", Query.Direction.DESCENDING)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    if(document.getLong("workExperienceNeed") >=3){
                                                                        areadyApply(document.getId());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                        }else
                                //thanh pho
                                if(typeString == null && CityString != null && slectionItemByExp ==0 ){
                                        Button btnReset = findViewById(R.id.btnReset);
                                        btnReset.setText(CityString);
                                        btnReset.setVisibility(View.VISIBLE);
                                        firebaseFirestore.collection("Job")
                                                .whereEqualTo("careerId", document.getId())
                                                .whereEqualTo("status", true)
                                                .whereEqualTo("city",CityString)
                                                .orderBy("dateStart", Query.Direction.DESCENDING)
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
                                    }else
                                //tp + yeah
                                if(typeString == null && CityString != null && slectionItemByExp <4 && slectionItemByExp != 0){
                                            slectionItemByType = getIntent().getIntExtra("slectionItemByType",0);
                                            Button btnReset = findViewById(R.id.btnReset);
                                            String temp  = String.valueOf(slectionItemByExp-1);
                                            btnReset.setText(CityString +", "+ temp + getString(R.string.year));
                                            btnReset.setVisibility(View.VISIBLE);
                                            firebaseFirestore.collection("Job")
                                                    .whereEqualTo("careerId", document.getId())
                                                    .whereEqualTo("status", true)
                                                    .whereEqualTo("city",CityString)
                                                    .whereEqualTo("workExperienceNeed",slectionItemByExp-1)
                                                    .orderBy("dateStart", Query.Direction.DESCENDING)
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
                                        }else
                                //tp + yeah >=3
                                if(typeString == null && CityString != null && slectionItemByExp == 4 && slectionItemByExp != 0){
                                            slectionItemByType = getIntent().getIntExtra("slectionItemByType",0);
                                            Button btnReset = findViewById(R.id.btnReset);
                                            btnReset.setText(CityString +", 3+" + getString(R.string.year) );
                                            btnReset.setVisibility(View.VISIBLE);
                                            firebaseFirestore.collection("Job")
                                                    .whereEqualTo("careerId", document.getId())
                                                    .whereEqualTo("status", true)
                                                    .whereEqualTo("city",CityString)
                                                    .orderBy("dateStart", Query.Direction.DESCENDING)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    if(document.getLong("workExperienceNeed") >=3){
                                                                        areadyApply(document.getId());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                        }else

                                //yeah
                                if(typeString == null && CityString == null && slectionItemByExp <4 && slectionItemByExp != 0){
                                            slectionItemByExp = getIntent().getIntExtra("slectionItemByExp",0);
                                            Button btnReset = findViewById(R.id.btnReset);
                                            String temp  = String.valueOf(slectionItemByExp-1);
                                            btnReset.setText(temp + getString(R.string.year));
                                            btnReset.setVisibility(View.VISIBLE);
                                            firebaseFirestore.collection("Job")
                                                    .whereEqualTo("careerId", document.getId())
                                                    .whereEqualTo("status", true)
                                                    .whereEqualTo("workExperienceNeed",slectionItemByExp-1)
                                                    .orderBy("dateStart", Query.Direction.DESCENDING)
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
                                        }else
                                //yeah >=3
                                if(typeString == null && CityString == null && slectionItemByExp == 4 && slectionItemByExp != 0){
                                            slectionItemByExp = getIntent().getIntExtra("slectionItemByExp",0);
                                            Button btnReset = findViewById(R.id.btnReset);
                                            btnReset.setText("3+ " + getString(R.string.year) );
                                            btnReset.setVisibility(View.VISIBLE);
                                            firebaseFirestore.collection("Job")
                                                    .whereEqualTo("careerId", document.getId())
                                                    .whereEqualTo("status", true)
                                                    .orderBy("dateStart", Query.Direction.DESCENDING)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    if(document.getLong("workExperienceNeed") >=3){
                                                                        areadyApply(document.getId());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                        }

                                else{
                                        firebaseFirestore.collection("Job")
                                                .whereEqualTo("careerId", document.getId())
                                                .whereEqualTo("status", true)
                                                .orderBy("dateStart", Query.Direction.DESCENDING)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                String jobId = document.getId();
                                                                Log.d(TAG, "test:" + document.getString("jobPosition") + jobId + document.getTimestamp("dateStart"));
                                                                areadyApply(jobId);
                                                            }
                                                        }
                                                    }
                                                });

                                }
                            }
                        }
                    }
                });
    }
    private void areadyApply(String JobId) {
        firebaseFirestore.collection("Job")
                .document(JobId)
                .collection("Application")
                .document(firebaseauth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                Log.d(TAG, "test 1: "+ JobId);
                                checkAccept(JobId);
                            }
                        }
                    }
                });

    }

    private void checkAccept(String jobId) {
        firebaseFirestore.collection("Job")
                .document(jobId)
                .collection("AcceptList")
                .document(firebaseauth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        if(!d.exists()){
                            {
                                addJobToList(jobId);
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
                        String SearchString = getIntent().getStringExtra("SearchString");
                        if(SearchString != null){
                            Button btnReset = findViewById(R.id.btnReset);
                            btnReset.setVisibility(View.VISIBLE);
                            btnReset.setText(getString(R.string.search) + getString(R.string.by) + SearchString);
                            SearchString = SearchString.toLowerCase();
                            if(documentSnapshot.getString("jobPosition").toLowerCase().contains(SearchString.toLowerCase())) {
                                Company company = createCompanyFromDocument(documentSnapshot,JobId);
                                getNameAndAvatar(company);
                            } else {
                                searchCompanyName(documentSnapshot,SearchString,JobId);
                            }
                        }else{
                            Company company = createCompanyFromDocument(documentSnapshot,JobId);
                            getNameAndAvatar(company);
                        }
                    }
                });
    }

    private void searchCompanyName(DocumentSnapshot documentSnapshot1,String SearchString,String JobId) {
        firebaseFirestore.collection("Company")
                .document(documentSnapshot1.getString("CompanyId"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("Name").toLowerCase().contains(SearchString)){
                            Company company = createCompanyFromDocument(documentSnapshot1,JobId);
                            getNameAndAvatar(company);
                        }
                    }
                });
    }


    private Company createCompanyFromDocument(DocumentSnapshot documentSnapshot,String JobId) {
        Company company = new Company();
        company.setCompanyJobPosition(documentSnapshot.getString("jobPosition"));
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
        company.setJobId(JobId);
        Timestamp timestamp = documentSnapshot.getTimestamp("dateStart");
        Date date = new Date(timestamp.toDate().getTime());
        company.setDateStart(date);
        Log.d(TAG, "createCompanyFromDocument: "+ company.getDateStart());
        return company;
    }

    private Company createCompanyFromDocumentView(DocumentSnapshot documentSnapshot,String JobId,Timestamp time){
        Company company = new Company();
        company.setCompanyJobPosition(documentSnapshot.getString("jobPosition"));
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
        company.setJobId(JobId);
        Timestamp timestamp = time;
        Date date = new Date(timestamp.toDate().getTime());
        company.setDateStart(date);
        Log.d(TAG, "createCompanyFromDocument: "+ company.getDateStart());
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
                        Collections.sort(companys, new Comparator<Company>() {
                            @Override
                            public int compare(Company c1, Company c2) {
                                return c2.getDateStart().compareTo(c1.getDateStart());
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        if(slectionItemByCity==1){
            RadioButton hoChiMinhRadioButton = dialogView.findViewById(R.id.hochiminh);
            hoChiMinhRadioButton.setChecked(true);
        } else if (slectionItemByCity==2) {
            RadioButton HaNoiRadioButton = dialogView.findViewById(R.id.HaNoi);
            HaNoiRadioButton.setChecked(true);
        }
        if(slectionItemByType==1){
            RadioButton fulltimeRadioButton = dialogView.findViewById(R.id.fulltime);
            fulltimeRadioButton.setChecked(true);
        } else if (slectionItemByType==2) {
            RadioButton parttimeRadioButton = dialogView.findViewById(R.id.parttime);
            parttimeRadioButton.setChecked(true);
        }else if (slectionItemByType==3) {
            RadioButton internRadioButton = dialogView.findViewById(R.id.intern);
            internRadioButton.setChecked(true);
        }
        if(slectionItemByExp==1){
            RadioButton year0timeRadioButton = dialogView.findViewById(R.id.year0);
            year0timeRadioButton.setChecked(true);
        } else if (slectionItemByExp==2) {
            RadioButton year1RadioButton = dialogView.findViewById(R.id.year1);
            year1RadioButton.setChecked(true);
        }else if (slectionItemByExp==3) {
            RadioButton year2RadioButton = dialogView.findViewById(R.id.year2);
            year2RadioButton.setChecked(true);
        }else if (slectionItemByExp==4) {
            RadioButton year3 = dialogView.findViewById(R.id.year3);
            year3.setChecked(true);
        }
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(findActivity.this,findActivity.class);
                i.putExtra("slectionItemByCity",slectionItemByCity);
                i.putExtra("slectionItemByType",slectionItemByType);
                i.putExtra("slectionItemByExp",slectionItemByExp);
                String SearchString = getIntent().getStringExtra("SearchString");
                i.putExtra("SearchString",SearchString);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finish();
            }
        });
        builder.setNegativeButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(findActivity.this,findActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finish();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                slectionItemByType = 0;
                slectionItemByExp = 0;
                slectionItemByCity = 0;
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void onRadioButtonClickedCity(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.hochiminh:
                if (checked) {
                    if (slectionItemByCity == 1) {
                        // the button is already checked, uncheck it
                        slectionItemByCity = 0;
                        ((RadioButton) view).setChecked(false);
                    } else {
                        // the button was unchecked, check it and uncheck the other button
                        slectionItemByCity = 1;
                        RadioButton haNoiRadioButton = dialogView.findViewById(R.id.HaNoi);
                        haNoiRadioButton.setChecked(false);
                    }
                }
                break;
            case R.id.HaNoi:
                if (checked) {
                    if (slectionItemByCity == 2) {
                        // the button is already checked, uncheck it
                        slectionItemByCity = 0;
                        ((RadioButton) view).setChecked(false);
                    } else {
                        // the button was unchecked, check it and uncheck the other button
                        slectionItemByCity = 2;
                        RadioButton hoChiMinhRadioButton = dialogView.findViewById(R.id.hochiminh);
                        hoChiMinhRadioButton.setChecked(false);
                    }
                }
                break;
        }
    }

    public void onRadioButtonClickedType(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.fulltime:
                if (checked) {
                    if (slectionItemByType == 1) {
                        // the button is already checked, uncheck it
                        slectionItemByType = 0;
                        ((RadioButton) view).setChecked(false);
                    } else {
                        // the button was unchecked, check it and uncheck the other button
                        slectionItemByType = 1;
                        RadioButton parttimeRadioButton = dialogView.findViewById(R.id.parttime);
                        parttimeRadioButton.setChecked(false);
                        RadioButton internRadioButton = dialogView.findViewById(R.id.intern);
                        internRadioButton.setChecked(false);
                    }
                }
                break;
            case R.id.parttime:
                if (checked) {
                    if (slectionItemByType == 2) {
                        // the button is already checked, uncheck it
                        slectionItemByType = 0;
                        ((RadioButton) view).setChecked(false);
                    } else {
                        // the button was unchecked, check it and uncheck the other button
                        slectionItemByType = 2;
                        RadioButton fulltimeRadioButton = dialogView.findViewById(R.id.fulltime);
                        fulltimeRadioButton.setChecked(false);
                        RadioButton internRadioButton = dialogView.findViewById(R.id.intern);
                        internRadioButton.setChecked(false);
                    }
                }
                break;
            case R.id.intern:
                if (checked) {
                    if (slectionItemByType == 3) {
                        // the button is already checked, uncheck it
                        slectionItemByType = 0;
                        ((RadioButton) view).setChecked(false);
                    } else {
                        // the button was unchecked, check it and uncheck the other button
                        slectionItemByType = 3;
                        RadioButton fulltimeRadioButton = dialogView.findViewById(R.id.fulltime);
                        fulltimeRadioButton.setChecked(false);
                        RadioButton parttimeRadioButton = dialogView.findViewById(R.id.parttime);
                        parttimeRadioButton.setChecked(false);
                    }
                }
                break;
        }
    }
    public void onRadioButtonClickedExp(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.year0:
                if (checked) {
                    if (slectionItemByExp == 1) {
                        // the button is already checked, uncheck it
                        slectionItemByExp = 0;
                        ((RadioButton) view).setChecked(false);
                    } else {
                        // the button was unchecked, check it and uncheck the other buttons
                        slectionItemByExp = 1;
                        RadioButton year1RadioButton = dialogView.findViewById(R.id.year1);
                        year1RadioButton.setChecked(false);
                        RadioButton year2RadioButton = dialogView.findViewById(R.id.year2);
                        year2RadioButton.setChecked(false);
                        RadioButton year3RadioButton = dialogView.findViewById(R.id.year3);
                        year3RadioButton.setChecked(false);
                    }
                }
                break;
            case R.id.year1:
                if (checked) {
                    if (slectionItemByExp == 2) {
                        // the button is already checked, uncheck it
                        slectionItemByExp = 0;
                        ((RadioButton) view).setChecked(false);
                    } else {
                        // the button was unchecked, check it and uncheck the other buttons
                        slectionItemByExp = 2;
                        RadioButton year0RadioButton = dialogView.findViewById(R.id.year0);
                        year0RadioButton.setChecked(false);
                        RadioButton year2RadioButton = dialogView.findViewById(R.id.year2);
                        year2RadioButton.setChecked(false);
                        RadioButton year3RadioButton = dialogView.findViewById(R.id.year3);
                        year3RadioButton.setChecked(false);
                    }
                }
                break;
            case R.id.year2:
                if (checked) {
                    if (slectionItemByExp == 3) {
                        // the button is already checked, uncheck it
                        slectionItemByExp = 0;
                        ((RadioButton) view).setChecked(false);
                    } else {
                        // the button was unchecked, check it and uncheck the other buttons
                        slectionItemByExp = 3;
                        RadioButton year0RadioButton = dialogView.findViewById(R.id.year0);
                        year0RadioButton.setChecked(false);
                        RadioButton year1RadioButton = dialogView.findViewById(R.id.year1);
                        year1RadioButton.setChecked(false);
                        RadioButton year3RadioButton = dialogView.findViewById(R.id.year3);
                        year3RadioButton.setChecked(false);
                    }
                }
                break;
            case R.id.year3:
                if (checked) {
                    if (slectionItemByExp == 4) {
                        // the button is already checked, uncheck it
                        slectionItemByExp = 0;
                        ((RadioButton) view).setChecked(false);
                    } else {
                        // the button was unchecked, check it and uncheck the other buttons
                        slectionItemByExp = 4;
                        RadioButton year0RadioButton = dialogView.findViewById(R.id.year0);
                        year0RadioButton.setChecked(false);
                        RadioButton year1RadioButton = dialogView.findViewById(R.id.year1);
                        year1RadioButton.setChecked(false);
                        RadioButton year2RadioButton = dialogView.findViewById(R.id.year2);
                        year2RadioButton.setChecked(false);
                    }
                }
                break;
        }
    }

            @Override
    public void onBackPressed() {
        String ApplyView = getIntent().getStringExtra("ApplyView");
        if(ApplyView == null){
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.please_click_back_again_to_exit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);}else{
            finish();
        }
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