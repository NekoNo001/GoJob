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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.activity.companyRequestActivity;
import com.b1906478.gojob.model.Company;
import com.b1906478.gojob.model.Notification;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class JobAdapter extends  RecyclerView.Adapter<JobAdapter.CompanyViewHolder>{
    private List<Company> Companys;

    public JobAdapter(List<Company> Companys) {
        this.Companys = Companys;
    }

    public void setCompanys(List<Company> filteredCompanys) {
        this.Companys = filteredCompanys;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_job_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        holder.bindCompany(Companys.get(position));
    }

    @Override
    public int getItemCount() {
        return Companys.size();
    }


    class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView txtPosition;
        TextView txtCity;
        TextView txtDateStart;
        TextView txtDateEnd;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPosition = itemView.findViewById(R.id.txtPosition);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtDateStart = itemView.findViewById(R.id.timeStart);
            txtDateEnd = itemView.findViewById(R.id.timeEnd);
        }

        void bindCompany(final Company company){
            Log.d(TAG, "bindCompany: " + company.getCompanyJobPosition());
            txtPosition.setText(company.getCompanyJobPosition());
            txtCity.setText(company.getCompanyCity());
            txtDateStart.setText(new SimpleDateFormat("d MMM yyyy").format(company.getDateStart()));
            txtDateEnd.setText(new SimpleDateFormat("d MMM yyyy").format(company.getDateEnd()));
        }
    }
}
