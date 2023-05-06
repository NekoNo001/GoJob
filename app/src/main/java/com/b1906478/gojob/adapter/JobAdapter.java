package com.b1906478.gojob.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.b1906478.gojob.activity.JobManageActivity;
import com.b1906478.gojob.R;
import com.b1906478.gojob.activity.jobCreateActivity;
import com.b1906478.gojob.model.Company;

import java.text.SimpleDateFormat;
import java.util.List;

public class JobAdapter extends  RecyclerView.Adapter<JobAdapter.CompanyViewHolder>{
    private List<Company> Companys;
    private String careerId;

    public JobAdapter(List<Company> Companys, String careerId) {
        this.Companys = Companys;
        this.careerId = careerId;
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
        ConstraintLayout LayoutJob;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPosition = itemView.findViewById(R.id.txtPosition);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtDateStart = itemView.findViewById(R.id.timeStart);
            txtDateEnd = itemView.findViewById(R.id.timeEnd);
            LayoutJob = itemView.findViewById(R.id.LayoutJob);
        }

        void bindCompany(final Company company){
            Log.d(TAG, "bindCompany: " + company.getCompanyJobPosition());
            txtPosition.setText(company.getCompanyJobPosition());
            txtCity.setText(company.getCompanyCity());
            txtDateStart.setText(new SimpleDateFormat("d'/'MM'/'yyyy").format(company.getDateStart()));
            txtDateEnd.setText(new SimpleDateFormat("d'/'MM'/'yyyy").format(company.getDateEnd()));
            LayoutJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(careerId != null){
                        Intent i = new Intent(view.getContext(), jobCreateActivity.class);
                        i.putExtra("jobId",company.getJobId());
                        i.putExtra("careerId",careerId);
                        Log.d(TAG, "onClick:1"+ company.getJobId()+ company.getCompanyJobPosition());
                        view.getContext().startActivity(i);
                    }else{
                        Intent i = new Intent(view.getContext(), JobManageActivity.class);
                        i.putExtra("jobId",company.getJobId());
                        Log.d(TAG, "onClick:2"+ company.getJobId()+ company.getCompanyJobPosition());
                        view.getContext().startActivity(i);
                        ((Activity) view.getContext()).finish();
                    }
                }
            });
        }
    }
}
