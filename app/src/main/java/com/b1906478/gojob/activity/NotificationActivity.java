package com.b1906478.gojob.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.adapter.NotificationAdapter;
import com.b1906478.gojob.adapter.careerAdapter;
import com.b1906478.gojob.databinding.ActivityMainBinding;
import com.b1906478.gojob.databinding.ActivityNotificationBinding;
import com.b1906478.gojob.model.Career;
import com.b1906478.gojob.model.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    ActivityNotificationBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        setUp();
        RecyclerView recyclerView = findViewById(R.id.notificationView);
        List<Notification> notifications = new ArrayList<>();
        final NotificationAdapter notificationAdapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(notificationAdapter);
        firebaseFirestore.collection("User")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Notification")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Notification notification = new Notification();
                                notification.NotificationMessenge = document.getString("message");
                                notification.NotificationTime = document.getTimestamp("notificationTime");
                                notification.imageUrl=document.getString("imageUrl");
                                notifications.add(notification);
                                notificationAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void setUp() {
        ImageView leftArrow = findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView textView = findViewById(R.id.txtToolbar);
        textView.setText(R.string.notification);
    }
}