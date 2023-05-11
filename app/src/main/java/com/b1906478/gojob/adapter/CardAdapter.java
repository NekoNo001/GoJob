package com.b1906478.gojob.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.model.Company;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.myViewHolder> {
    private List<Company> companyList;
    public CardAdapter(List<Company> companyList) {
        this.companyList = companyList;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bindCard(holder,companyList.get(position));
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView JobPosition,NameCompany,Salary,TypeOfWork,WorkExperience,NumberOfRecruits, Level, gender, address, jobDescription, candidateRequirements, benefit;
        ImageView Avatar;
        FrameLayout left,right;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            left = itemView.findViewById(R.id.left_overlay);
            right = itemView.findViewById(R.id.right_overlay);
            JobPosition = itemView.findViewById(R.id.textView1);
            NameCompany = itemView.findViewById(R.id.textView2);
            Salary = itemView.findViewById(R.id.txtsalary);
            TypeOfWork = itemView.findViewById(R.id.txtLevel);
            WorkExperience = itemView.findViewById(R.id.txtexp);
            NumberOfRecruits = itemView.findViewById(R.id.txtnumofrecut);
            Avatar = itemView.findViewById(R.id.imageView);
            Level = itemView.findViewById(R.id.txtTypejob);
            gender = itemView.findViewById(R.id.txtgender);
            address = itemView.findViewById(R.id.txtaddress);
            jobDescription = itemView.findViewById(R.id.txtjobdescription);
            candidateRequirements = itemView.findViewById(R.id.txtcandidateRequirements);
            benefit = itemView.findViewById(R.id.txtbenefit);
        }
        void bindCard(myViewHolder holder, final Company company){
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
            if(company.getCompanyAvatar() != null){
                Picasso.get().load(company.getCompanyAvatar()).resize(1360,1370).centerCrop().into(Avatar);
            }
            JobPosition.setText(company.getCompanyJobPosition());
            NameCompany.setText(company.getCompanyName());
            Salary.setText(company.getCompanySalary());
            NumberOfRecruits.setText(String.valueOf(company.getCompanyNumberOfRecruits()) + holder.itemView.getContext().getResources().getString(R.string.people));
            TypeOfWork.setText(company.getCompanyTypeOfWork());
            if(company.getCompanyTypeOfWork().equals("Full-Time")){
                TypeOfWork.setText(R.string.full_time);
            } else if (company.getCompanyTypeOfWork().equals("Part-Time")) {
                TypeOfWork.setText(R.string.part_time);
            }else {
                TypeOfWork.setText(R.string.intern);
            }
            WorkExperience.setText(String.valueOf(company.getCompanyWorkExperience()) + holder.itemView.getContext().getResources().getString(R.string.year));
            Level.setText(company.getCompanyLevel());
            if(company.getCompanyGender().equals("Male")){
                gender.setText(R.string.male);
            } else if (company.getCompanyGender().equals("Male")) {
                gender.setText(R.string.female);
            }else {
                gender.setText(R.string.not_required);
            }
            address.setText(company.getCompanyAdress().replace("\\n","\n"));
            jobDescription.setText(company.getCompanyJobDescription().replace("\\n","\n"));
            candidateRequirements.setText(company.getCompanyCandidateRequirements().replace("\\n","\n"));
            benefit.setText(company.getCompanyBenefit().replace("\\n","\n"));
        }
    }
}
