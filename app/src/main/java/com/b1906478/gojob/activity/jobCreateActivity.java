package com.b1906478.gojob.activity;



import androidx.appcompat.app.AppCompatActivity;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityJobCreateBinding;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class jobCreateActivity extends AppCompatActivity {

    ActivityJobCreateBinding binding;
    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityJobCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView leftArrow = findViewById(R.id.leftArrow);
        TextView txttoolbar = findViewById(R.id.txtToolbar);
        Button btn = findViewById(R.id.btnNext);
        txttoolbar.setText(R.string.Addfirstjob);
        setUpJob();
        setUpTypeJob();
        setUpGender();
        backbuttob(leftArrow);
        String jobId = getIntent().getStringExtra("jobId");
        if(jobId != null){
        setupdate(jobId);}
        Onclicknext(btn);
    }

    private void setupdate(String jobId) {
        firebaseFirestore.collection("Job").document(jobId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                binding.txtlocation.setText(documentSnapshot.getString("city"));
                                binding.jobPosition.setText(documentSnapshot.getString("jobPosition"));
                                binding.txtsalary.setText(documentSnapshot.getString("salary"));
                                binding.txtnumofrecut.setText(String.valueOf(documentSnapshot.getLong("numberOfRecruits")));
                                binding.txtLevel.setText(documentSnapshot.getString("level"));
                                binding.txtexp.setText(String.valueOf(documentSnapshot.getLong("workExperienceNeed")));
                                binding.txtTypejob.setText(documentSnapshot.getString("typeOfWork"));
                                binding.txtgender.setText(documentSnapshot.getString("gender"));
                                binding.txtjobdescription.setText(documentSnapshot.getString("jobDescription"));
                                binding.txtRequirements.setText(documentSnapshot.getString("candidateRequirements"));
                                binding.txtbenefit.setText(documentSnapshot.getString("benefit"));
                                binding.txtaddress.setText(documentSnapshot.getString("address"));
                                // Assume you have a Firebase Timestamp object named "timestamp"
                                Date date = documentSnapshot.getTimestamp("dateEnd").toDate();
                                calendar.setTime(date);
                                year = calendar.get(Calendar.YEAR);
                                month = calendar.get(Calendar.MONTH) ; // Add 1 because Calendar.MONTH is zero-based
                                day = calendar.get(Calendar.DAY_OF_MONTH);
                            }
                        });

    }


    private void setUpJob(){
        EditText editText = findViewById(R.id.txtlocation);
        Spinner spinner = findViewById(R.id.my_spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.my_dropdown_items_location, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: "+ selectedItem);
                editText.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    private void setUpTypeJob() {
        EditText editText = findViewById(R.id.txtTypejob);
        Spinner spinner = findViewById(R.id.my_spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.my_dropdown_items_typejob, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: "+ selectedItem);
                editText.setText(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    private void setUpGender(){
        EditText editText = findViewById(R.id.txtgender);
        Spinner spinner = findViewById(R.id.my_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.my_dropdown_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: "+ selectedItem);
                editText.setText(selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    public void backbuttob(ImageView a) {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ;
            }
        });
    }
    public void Onclicknext(Button a) {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = binding.txtlocation.getText().toString();
                String JobPosition = binding.jobPosition.getText().toString();
                String salary = binding.txtsalary.getText().toString();
                String recut = (binding.txtnumofrecut.getText().toString());
                String level = binding.txtLevel.getText().toString();
                String exp = (binding.txtexp.getText().toString());;
                String typejob = binding.txtTypejob.getText().toString();
                String gender = binding.txtgender.getText().toString();
                String jobDescription = binding.txtjobdescription.getText().toString();
                String Requirements = binding.txtRequirements.getText().toString();
                String benefit = binding.txtbenefit.getText().toString();
                String address = binding.txtaddress.getText().toString();
                if (JobPosition.matches("") || salary.matches("") || recut.matches("")  || level.matches("") || exp.matches("") || typejob.matches("") || jobDescription.matches("") || Requirements.matches("") || benefit.matches("")) {
                    Toast.makeText(jobCreateActivity.this, R.string.missing, Toast.LENGTH_SHORT).show();
                }else if(getIntent().getBooleanExtra("edit",false)){
                        DatePickerDialog datePickerDialog = new DatePickerDialog(jobCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                month = month + 1;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month-1, day);
                                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                                    Toast.makeText(jobCreateActivity.this,"The selected date cannot before or equal to today's date", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Timestamp timestamp = new Timestamp(calendar.getTime());
                                Map<String, Object> Job = new HashMap<>();
                                Job.put("jobPosition", JobPosition);
                                Job.put("salary", salary);
                                Job.put("numberOfRecruits",  Integer.parseInt(recut));
                                Job.put("level", level);
                                Job.put("workExperienceNeed", Integer.parseInt(exp));
                                Job.put("typeOfWork", typejob);
                                Job.put("gender", gender);
                                Job.put("jobDescription", jobDescription);
                                Job.put("candidateRequirements", Requirements);
                                Job.put("benefit", benefit);
                                Job.put("CompanyId",firebaseauth.getCurrentUser().getUid());
                                Job.put("address",address);
                                Job.put("city",city);
                                Job.put("dateEnd", timestamp);
                                String jobId = getIntent().getStringExtra("jobId");
                                firebaseFirestore.collection("Job")
                                        .document(jobId)
                                        .update(Job);
                                finish();
                            }
                        }, year, month, day);
                        datePickerDialog.setTitle(getString(R.string.choice_date_end));
                        datePickerDialog.show();
                } else{
                    DatePickerDialog datePickerDialog = new DatePickerDialog(jobCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month + 1;
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month-1, day);
                            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                                Toast.makeText(jobCreateActivity.this,"The selected date cannot before or equal to today's date", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Timestamp timestamp = new Timestamp(calendar.getTime());
                            String careerId = getIntent().getStringExtra("careerId");
                            Map<String, Object> Job = new HashMap<>();
                            Job.put("careerId",careerId);
                            Job.put("jobPosition", JobPosition);
                            Job.put("salary", salary);
                            Job.put("numberOfRecruits",  Integer.parseInt(recut));
                            Job.put("level", level);
                            Job.put("workExperienceNeed", Integer.parseInt(exp));
                            Job.put("typeOfWork", typejob);
                            Job.put("gender", gender);
                            Job.put("jobDescription", jobDescription);
                            Job.put("candidateRequirements", Requirements);
                            Job.put("benefit", benefit);
                            Job.put("CompanyId",firebaseauth.getCurrentUser().getUid());
                            Job.put("address",address);
                            Job.put("city",city);
                            Job.put("dateStart", FieldValue.serverTimestamp());
                            Job.put("dateEnd", timestamp);
                            Job.put("status", true);
                            firebaseFirestore.collection("Job")
                                    .add(Job);
                            Intent i = new Intent(jobCreateActivity.this, CompanyActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }
                    }, year, month, day);
                    datePickerDialog.setTitle(getString(R.string.choice_date_end));
                    datePickerDialog.show();
                }
            }
        });
    }

}

