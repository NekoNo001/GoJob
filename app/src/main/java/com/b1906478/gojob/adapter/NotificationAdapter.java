package com.b1906478.gojob.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.activity.companyRequestActivity;
import com.b1906478.gojob.model.Career;
import com.b1906478.gojob.model.Notification;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.notificationViewHolder>{
    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public notificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new notificationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_notification_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull notificationViewHolder holder, int position) {
        holder.bindNotification(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


    class notificationViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layoutNotification;
        TextView txtNotification;
        TextView txtNotificationTime;
        ImageView notificationIc;

        public notificationViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutNotification = itemView.findViewById(R.id.layoutNotification);
            txtNotification = itemView.findViewById(R.id.txtNotification);
            txtNotificationTime = itemView.findViewById(R.id.txtNotificationTime);
            notificationIc = itemView.findViewById(R.id.nofication_ic);
        }

        void bindNotification(final Notification notification){
            if(notification.jobPosition != null){
            SpannableString spannableString = new SpannableString(notification.NotificationMessenge + " " +notification.jobPosition);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), spannableString.length() - notification.jobPosition.length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtNotification.setText(spannableString);
            }else{
                txtNotification.setText(notification.NotificationMessenge);
            }
            txtNotificationTime.setText(new SimpleDateFormat("d MMM yyyy 'at' HH:mm").format(notification.NotificationTime.toDate()));
            Picasso.get().load(notification.imageUrl).resize(500, 500).centerCrop().into(notificationIc);
            layoutNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (notification.jobId != null) {
                        Intent i = new Intent(view.getContext(), companyRequestActivity.class);
                        i.putExtra("jobId",notification.jobId);
                        Log.d(TAG, "onClick:1"+ notification.jobId);
                        view.getContext().startActivity(i);
                        ((Activity) view.getContext()).finish();
                    }
                }
            });
        }
    }
}
