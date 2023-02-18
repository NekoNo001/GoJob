package com.b1906478.gojob.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            JobPosition = itemView.findViewById(R.id.textView1);
            NameCompany = itemView.findViewById(R.id.textView2);
            Salary = itemView.findViewById(R.id.txtsalary);
            TypeOfWork = itemView.findViewById(R.id.txttypejob);
            WorkExperience = itemView.findViewById(R.id.txtexp);
            NumberOfRecruits = itemView.findViewById(R.id.txtnumofrecut);
            Avatar = itemView.findViewById(R.id.imageView);
            Level = itemView.findViewById(R.id.txtlevel);
            gender = itemView.findViewById(R.id.txtgender);
            address = itemView.findViewById(R.id.txtaddress);
            jobDescription = itemView.findViewById(R.id.txtjobdescription);
            candidateRequirements = itemView.findViewById(R.id.txtcandidateRequirements);
            benefit = itemView.findViewById(R.id.txtbenefit);
        }
        void bindCard(myViewHolder holder, final Company company){
            Picasso.get().load(company.getImageUrl()).resize(5000,5000).centerCrop().into(Avatar);
            JobPosition.setText(company.getJobPosition());
            NameCompany.setText(company.getNameCompany());
            Salary.setText(company.getSalary());
            NumberOfRecruits.setText(String.valueOf(company.getNumberOfRecruits()) + holder.itemView.getContext().getResources().getString(R.string.people));
            TypeOfWork.setText(company.getTypeOfWork());
            WorkExperience.setText(String.valueOf(company.getWorkExperienceNeed()) + holder.itemView.getContext().getResources().getString(R.string.year));
            Level.setText(company.getLevel());
            gender.setText(company.getGender());
            address.setText(company.getAdress().replace("\\n","\n"));
            jobDescription.setText(company.getJobDescription().replace("\\n","\n"));
            candidateRequirements.setText(company.getCandidateRequirements().replace("\\n","\n"));
            benefit.setText(company.getBenefit().replace("\\n","\n"));
        }
    }
}
