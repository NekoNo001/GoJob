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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.CardAdapter;
import com.b1906478.gojob.databinding.ActivityFindBinding;
import com.b1906478.gojob.model.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
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

import java.util.ArrayList;

public class findActivity extends AppCompatActivity {
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
        super.onCreate(savedInstanceState);
        binding=ActivityFindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView reddot = binding.reddot;
        companys = new ArrayList<>();
        setUpManager();
        setUpCardStackView();
        getCompany();
        setNotificationChange(reddot);
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
        //set up button
        Button btnApply = binding.btnApply;
        Button btnRefuse = binding.btnrefuse;
        Button btnSort = binding.btnsort;
        Button btnNofication = binding.btnNofication;
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
        setProfile(nameTxt,jobPositiontxt,avatarImg);
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
                                startActivity(i);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
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
    }

    private void setProfile(TextView nameTxt,TextView jobPositiontxt, ImageView avatarImg) {
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
                            if(snapshot.getString("imageUrl") != "") {
                                Picasso.get().load(snapshot.getString("imageUrl")).resize(500, 500).centerCrop().into(avatarImg);
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    firebaseFirestore.collection("Job")
                                            .whereEqualTo("careerId", document.getId())
                                            .whereEqualTo("status", true)
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
                        String SearchString = getIntent().getStringExtra("SearchString");
                        if(SearchString != null){
                            if(documentSnapshot.getString("jobPosition").toLowerCase().contains(SearchString.toLowerCase())) {
                                Company company = createCompanyFromDocument(documentSnapshot);
                                getNameAndAvatar(company);
                            } else {
                                searchCompanyName(documentSnapshot,SearchString);
                            }
                        }else{
                            Company company = createCompanyFromDocument(documentSnapshot);
                            getNameAndAvatar(company);
                        }
                    }
                });
    }

    private void searchCompanyName(DocumentSnapshot documentSnapshot1,String SearchString) {
        firebaseFirestore.collection("Company")
                .document(documentSnapshot1.getString("CompanyId"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("Name").toLowerCase().contains(SearchString)){
                            Company company = createCompanyFromDocument(documentSnapshot1);
                            getNameAndAvatar(company);
                        }
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}