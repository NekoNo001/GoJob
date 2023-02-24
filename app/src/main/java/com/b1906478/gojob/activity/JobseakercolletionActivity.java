package com.b1906478.gojob.activity;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityJobseekercolletionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class JobseakercolletionActivity extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    ActivityJobseekercolletionBinding binding;
    AutoCompleteTextView autoCompleteTextView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    ArrayAdapter<String> adapterItem;
    EditText edt;
    Uri imageUrl;
    String ImageUrl= "";
    Timestamp timestamp;
    String[] items = {"An Giang", "Ba Ria-Vung Tau", "Bac Lieu", "Bac Kan", "Bac Giang", "Bac Ninh", "Ben Tre", "Binh Duong", "Binh Dinh", "Binh Phuoc", "Binh Thuan", "Ca Mau", "Cao Bang", "Can Tho", "Da Nang", "Dak Lak", "Dak Nong", "Dien Bien", "Dong Nai", "Dong Thap", "Gia Lai", "Ha Giang", "Henan", "Hanoi", "Ha Tay", "Ha Tinh", "Hai Duong", "Hai Phong", "Peace", "Ho Chi Minh", "Hau Giang", "Hung Yen", "Khanh Hoa", "Kien Giang", "Kon Tum", "Lai Chau", "Lao Cai", "Lang Son", "Lam Dong", "Long An", "Nam Dinh", "Nghe An", "Ninh Binh", "Ninh Thuan", "Phu Tho", "Phu Yen", "Quang Binh", "Quang Nam", "Quang Ngai", "Quang Ninh", "Quang Tri", "Soc Trang", "Son La", "Tay Ninh", "Thai Binh", "Thai Nguyen", "Thanh Hoa", "Thua Thien - Hue", "Tien Giang", "Tra Vinh", "Tuyen Quang", "Vinh Long", "Vinh Phuc", "Yen Bai",};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobseekercolletionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        EditText gpt = findViewById(R.id.txtGPA);
        gpt.setFilters(new InputFilter[]{ new MinMaxFilter( "1.00" , "4.00" )});
        ImageView leftArrow = findViewById(R.id.leftArrow);
        TextView txttoolbar = findViewById(R.id.txtToolbar);
        edt = findViewById(R.id.editTextDate);
        Button btn = findViewById(R.id.btnNext);
        txttoolbar.setText(R.string.Create_Your_CV);
        ImageView img = findViewById(R.id.imageView2);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_PICK);
                startActivityForResult(i, 100);
            }
        });
        setupLayout(leftArrow);
        getOnDataOnCloud();
        Onclicknext(btn, img);
        listcity();
        chooseday(edt);
    }

    private void getOnDataOnCloud() {
        firebaseFirestore.collection("User")
                .document(firebaseauth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Docuent already exists in Firestorem
                                binding.txteIntroduce.setText(document.getString("Introduce"));
                                binding.nameId.setText(document.getString("Name"));
                                binding.positionId.setText(document.getString("Position"));
                                binding.university.setText(document.getString("University"));
                                binding.txtGPA.setText(document.getString("GPA"));
                                binding.txtaddress.setText(document.getString("Address"));
                                binding.txtemail.setText(document.getString("Email"));
                                binding.txtphone.setText(document.getString("Phone"));
                                binding.txtweb.setText(document.getString("Website"));
                                timestamp = document.getTimestamp("DOB");
                                Date date = timestamp.toDate();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
                                String dateString = dateFormat.format(date);
                                binding.editTextDate.setText(dateString);
                                if(document.getString("imageUrl") != ""){
                                    StorageReference mountainImagesRef = storageRef.child("User/" + FirebaseAuth.getInstance().getUid() + ".PNG");
                                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.get()
                                                    .load(uri)
                                                    .into(binding.imageView2);
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
    }


    private void setupLayout(ImageView a) {
        listcity();
        chooseday(edt);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Onclicknext(Button a, ImageView img) {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Introduce = binding.txteIntroduce.getText().toString();
                String name = binding.nameId.getText().toString();
                String position = binding.positionId.getText().toString();
                String university = binding.university.getText().toString();
                String gpa = binding.txtGPA.getText().toString();
                String address = binding.txtaddress.getText().toString();
                String email = binding.txtemail.getText().toString();
                String phone = binding.txtphone.getText().toString();
                String website = binding.txtweb.getText().toString();
                String dob = binding.editTextDate.getText().toString();
                if (name.matches("") || position.matches("") || university.matches("") || gpa.matches("") || address.matches("") || email.matches("") || phone.matches("") || dob.matches("")) {
                    Toast.makeText(JobseakercolletionActivity.this, R.string.missing, Toast.LENGTH_LONG).show();
                } else {
                    Map<String, Object> User = new HashMap<>();
                    User.put("Introduce", Introduce);
                    User.put("Name", name);
                    User.put("Position", position);
                    User.put("University", university);
                    User.put("GPA", gpa);
                    User.put("Address", address);
                    User.put("Email", email);
                    User.put("Phone", phone);
                    User.put("Website", website);
                    User.put("DOB", timestamp);
                    User.put("imageUrl", ImageUrl);
                    firebaseFirestore.collection("User")
                            .document(FirebaseAuth.getInstance().getUid())
                            .set(User);

                    UploadImage(img);
                    Intent i = new Intent(JobseakercolletionActivity.this,JobseakercolletionActivity2.class);
                    startActivity(i);
                }

            }
        });
    }

    public void UploadImage(ImageView img) {
        if (img.getDrawable() != null) {
            StorageReference mountainImagesRef = storageRef.child("User/" + FirebaseAuth.getInstance().getUid() + ".PNG");
            img.setDrawingCacheEnabled(true);
            img.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainImagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            firebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getUid())
                                    .update("imageUrl", uri);
                        }
                    });
                }
            });
        }else{
            firebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getUid())
                    .update("imageUrl", "");
        }
    }

    private void listcity() {
        autoCompleteTextView = findViewById(R.id.txtaddress);
        adapterItem = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTextView.setAdapter(adapterItem);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });
    }

    private void chooseday(TextView DOB) {
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(JobseakercolletionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,month-1,day);
                        timestamp = new Timestamp(calendar.getTime());
                        DOB.setText(day + "/" + month + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data.getData() != null) {
            imageUrl = data.getData();
            CropImage.activity(imageUrl)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(11, 11)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.get()
                        .load(resultUri)
                        .resize(1360,1370)
                        .centerCrop()
                        .into(binding.imageView2);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
