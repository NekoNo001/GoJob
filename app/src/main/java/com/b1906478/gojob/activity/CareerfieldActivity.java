package com.b1906478.gojob.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.careerAdapter;
import com.b1906478.gojob.adapter.careerListener;
import com.b1906478.gojob.databinding.ActivityCareerfieldBinding;
import com.b1906478.gojob.databinding.ActivityCompanycolletionBinding;
import com.b1906478.gojob.model.Career;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;

public class CareerfieldActivity extends AppCompatActivity implements careerListener {

    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    ActivityCareerfieldBinding binding;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCareerfieldBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        btn = findViewById(R.id.btnnextstep);
        ImageView leftArrow = findViewById(R.id.leftArrow);
        TextView txttoolbar = findViewById(R.id.txtToolbar);
        txttoolbar.setText(R.string.Choose);
        backbuttob(leftArrow);
        RecyclerView careerRV = findViewById(R.id.RVcarrer);
        List<Career> careers = new ArrayList<>();
        char t=97;
        int tempId= 100;

        for(int i=1; i<=15 ; i++ ){
            String temp = String.valueOf(t);
            String tempDrawable = String.valueOf(t);
            String tempIdName= "C" + tempId;
            Career tempCareer = new Career();
            tempCareer.nameCareer = getString(getStringIDbyname(temp));
            tempCareer.image = getDrawIDbyname(tempDrawable);
            tempCareer.CareerId = tempIdName;
            careers.add(tempCareer);
            t++;
            tempId++;
        }

        final careerAdapter careeradapter = new careerAdapter(careers,this);
        careerRV.setAdapter(careeradapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Career> selectedCareer = careeradapter.getSelectedCareer();
                for(int j=1; j< careers.size(); j++){
                    firebaseFirestore.collection("User")
                            .document(FirebaseAuth.getInstance().getUid())
                            .collection("Career").document(careers.get(j).CareerId).delete();
                }
                for (int i=0; i< selectedCareer.size();i++) {
                    Map<String, Object> Career = new HashMap<>();
                    Career.put("NameCareer", selectedCareer.get(i).nameCareer);
                    Career.put("CareerID", selectedCareer.get(i).CareerId);
                    firebaseFirestore.collection("User")
                            .document(FirebaseAuth.getInstance().getUid())
                            .collection("Career").document(String.valueOf(selectedCareer.get(i).CareerId)).set(Career);
                }
                Intent i = new Intent(CareerfieldActivity.this,findActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onCareerAction(Boolean isSelected) {
        if(isSelected){
            btn.setVisibility(View.VISIBLE);
        }else{
            btn.setVisibility(View.GONE);
        }
    }

    public void backbuttob(ImageView a) {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public  int getStringIDbyname(String temp){
        int resourceID = getResources().getIdentifier(
                temp,
                "string",
                getPackageName()
        );
        return resourceID;
    }
    public  int getDrawIDbyname(String temp){
        int resourceID = getResources().getIdentifier(
                temp,
                "drawable",
                getPackageName()
        );
        return resourceID;
    }
}