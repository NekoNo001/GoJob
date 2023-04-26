package com.b1906478.gojob.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.activity.JobManageActivity;
import com.b1906478.gojob.activity.companyRequestActivity;
import com.b1906478.gojob.activity.jobCreateActivity;
import com.b1906478.gojob.activity.viewJobSeekerInformationActivity;
import com.b1906478.gojob.model.Company;
import com.b1906478.gojob.model.UserModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class JobSeekerAdapter extends  RecyclerView.Adapter<JobSeekerAdapter.CompanyViewHolder>{
    private List<UserModel> userModels;


    public JobSeekerAdapter(List<UserModel> userModels) {
        this.userModels = userModels;
    }

    public void setCompanys(List<UserModel> filtereduserModels) {
        this.userModels = filtereduserModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_jobseeker_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        holder.bindCompany(userModels.get(position));
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, TxtEmail, TxtPhone, TxtDateApply;
        ImageView avatarImg;
        View LayoutJob;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPosition);
            TxtEmail = itemView.findViewById(R.id.txtCity);
            TxtPhone = itemView.findViewById(R.id.sdtTxt);
            TxtDateApply = itemView.findViewById(R.id.timeStart);
            avatarImg = itemView.findViewById(R.id.avatarImg);
            LayoutJob = itemView.findViewById(R.id.LayoutJob);
        }
        void bindCompany(final UserModel userModel) {
            txtName.setText(userModel.getUsername());
            TxtEmail.setText(userModel.getUserEmail());
            TxtEmail.setPaintFlags(TxtEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            TxtPhone.setText(userModel.getUserPhone());
            TxtPhone.setPaintFlags(TxtEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            TxtDateApply.setText(new SimpleDateFormat("d MMM yyyy").format(userModel.getUserDOB()));
            if(!userModel.getUserAvatar().equals("")){
                Picasso.get().load(userModel.getUserAvatar()).resize(500,500).centerCrop().into(avatarImg);
            }
            TxtEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Email", TxtEmail.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(txtName.getContext(), R.string.email_copied_to_clipboard, Toast.LENGTH_SHORT).show();
                }
            });
            TxtPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Phone", TxtPhone.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(txtName.getContext(), R.string.phone_number_copied_to_clipboard, Toast.LENGTH_SHORT).show();
                }
            });
            LayoutJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), viewJobSeekerInformationActivity.class);
                    i.putExtra("userId",userModel.getUserId());
                    view.getContext().startActivity(i);
                    ((Activity) view.getContext()).finish();
                }
            });
        }
    }
}
