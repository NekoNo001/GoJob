package com.b1906478.gojob.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
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
        TextView txtNotificationTime,txtposition;
        ImageView notificationIc;

        public notificationViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutNotification = itemView.findViewById(R.id.layoutNotification);
            txtNotification = itemView.findViewById(R.id.txtNotification);
            txtposition = itemView.findViewById(R.id.txtPosition);
            txtNotificationTime = itemView.findViewById(R.id.txtNotificationTime);
            notificationIc = itemView.findViewById(R.id.nofication_ic);

        }

        void bindNotification(final Notification notification){
            if(notification.jobPosition != null){
                txtposition.setText(notification.jobPosition);
                if(notification.NotificationMessenge.equals("M102".trim())){
                    txtNotification.setText(R.string.you_are_invited_to_apply_for_the_position);
                } else if (notification.NotificationMessenge.equals("M103".trim())) {
                    txtNotification.setText(R.string.your_cv_being_refuse_for_position);
                }else if (notification.NotificationMessenge.equals("M104".trim())) {
                    txtNotification.setText(R.string.your_cv_was_accept_please_wait_for_company_contact_position);
                }
            }else{
                txtposition.setVisibility(View.GONE);
                if(notification.NotificationMessenge.equals("M100".trim())) {
                    txtNotification.setText(R.string.hello_new_user_we_wish_you_all_the_luck_in_finding_a_job);
                }else if(notification.NotificationMessenge.equals("M101".trim())){
                    txtNotification.setText(R.string.your_cv_being_refuse_many_time_we_recommend_you_edit_your_cv_or_try_different_fields);
                }
            }
            txtNotificationTime.setText(new SimpleDateFormat("d'/'MM'/'yyyy HH:mm").format(notification.NotificationTime.toDate()));
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
