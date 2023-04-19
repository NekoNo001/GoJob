package com.b1906478.gojob.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

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
                .whereEqualTo("status", false) // only update documents with status = false
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        WriteBatch batch = firebaseFirestore.batch();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            DocumentReference documentRef = documentSnapshot.getReference();
                            batch.update(documentRef, "status", true);
                        }
                        batch.commit();
                        firebaseFirestore.collection("User")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .collection("Notification")
                                .orderBy("notificationTime", Query.Direction.ASCENDING)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if (value != null && !value.isEmpty()){
                                            for (DocumentChange dc : value.getDocumentChanges()) {
                                                Notification notification = new Notification();
                                                notification.NotificationMessenge = dc.getDocument().getString("message");
                                                notification.NotificationTime = dc.getDocument().getTimestamp("notificationTime");
                                                notification.imageUrl = dc.getDocument().getString("imageUrl");
                                                notification.jobId = dc.getDocument().getString("jobId");
                                                notification.jobPosition = dc.getDocument().getString("jobPosition");
                                                notifications.add(0, notification);
                                                notificationAdapter.notifyItemInserted(0);
                                            }
                                        }

                                    }
                                });}});
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