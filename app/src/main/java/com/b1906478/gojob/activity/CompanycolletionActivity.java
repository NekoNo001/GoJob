package com.b1906478.gojob.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityCompanycolletionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class CompanycolletionActivity extends AppCompatActivity {

    FirebaseAuth firebaseauth;
    FirebaseFirestore firebaseFirestore;
    ActivityCompanycolletionBinding binding;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Uri imageUrl;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanycolletionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseauth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView leftArrow = findViewById(R.id.leftArrow);
        TextView txttoolbar = findViewById(R.id.txtToolbar);
        Button btn = findViewById(R.id.btnNext);
        txttoolbar.setText("We need Infomation of your Company");
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
        getOnDataOnCloud();
        backbuttob(leftArrow);
        Onclicknext(btn, img);
    }

    public void backbuttob(ImageView a) {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void Onclicknext(Button a, ImageView img) {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.txtcompanyName.getText().toString();
                if (name.matches("") ){
                    Toast.makeText(CompanycolletionActivity.this, R.string.missing, Toast.LENGTH_LONG).show();
                } else {
                    Map<String, Object> Company = new HashMap<>();
                    Company.put("Name", name);
                    Company.put("imageUrl", "");
                    firebaseFirestore.collection("Company")
                            .document(firebaseauth.getCurrentUser().getUid())
                            .set(Company);
                    UploadImage(img);
                    Boolean edit = getIntent().getBooleanExtra("edit",false);
                    if(edit.equals(true)){
                        finish();
                    }else{
                    Intent i = new Intent(CompanycolletionActivity.this,CareerfieldActivity.class);
                    String UserType = "Company";
                    i.putExtra("Key",UserType);
                    startActivity(i);}
                }

            }
        });
    }

    public void UploadImage(ImageView img) {
        if (img.getDrawable() != null) {
            StorageReference mountainImagesRef = storageRef.child("Company/" + firebaseauth.getCurrentUser().getUid() + ".PNG");
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
                            firebaseFirestore.collection("Company").document(firebaseauth.getCurrentUser().getUid())
                                    .update("imageUrl", uri);
                        }
                    });
                }
            });
        }else{
            firebaseFirestore.collection("Company").document(firebaseauth.getCurrentUser().getUid())
                    .update("imageUrl", "");
        }
    }
    private void getOnDataOnCloud() {
        firebaseFirestore.collection("Company")
                .document(firebaseauth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Docuent already exists in Firestore
                                binding.txtcompanyName.setText(document.getString("Name"));
                                if(document.getString("imageUrl") != ""){
                                    StorageReference mountainImagesRef = storageRef.child("Company/" + FirebaseAuth.getInstance().getUid() + ".PNG");
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
