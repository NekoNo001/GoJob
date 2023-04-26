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
import com.b1906478.gojob.model.UserModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;


public class CardJobSeekerAdapter extends RecyclerView.Adapter<CardJobSeekerAdapter.myViewHolder> {
    private List<UserModel> userModels;
    public CardJobSeekerAdapter(List<UserModel> userModels) {
        this.userModels = userModels;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_job_seeker,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bindCard(holder,userModels.get(position));
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView addresstxt,nametxt, jobtxt, introtxt, universitytxt, gpatxt, emailtxt, sdttxt, dobtxt, webtxt, skill, skilltxt, cer, certxt, exp, exptxt, interet, interettxt;
        ImageView avatarImg;
        View line3;
        FrameLayout left,right;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            left = itemView.findViewById(R.id.left_overlay);
            right = itemView.findViewById(R.id.right_overlay);
            nametxt = itemView.findViewById(R.id.textView1);
            jobtxt = itemView.findViewById(R.id.textView2);
            addresstxt= itemView.findViewById(R.id.addresstxt);

            //intro part
            introtxt = itemView.findViewById(R.id.txtaddress);
            line3 = itemView.findViewById(R.id.line3);

            universitytxt= itemView.findViewById(R.id.universitytxt);
            gpatxt= itemView.findViewById(R.id.gpatxt);
            emailtxt= itemView.findViewById(R.id.emailtxt);
            sdttxt= itemView.findViewById(R.id.sdttxt);
            dobtxt= itemView.findViewById(R.id.dobtxt);
            webtxt = itemView.findViewById(R.id.webtxt);

            //skill part
            skill= itemView.findViewById(R.id.jobdescription);
            skilltxt= itemView.findViewById(R.id.txtjobdescription);

            //cer part
            cer= itemView.findViewById(R.id.candidateRequirements);
            certxt= itemView.findViewById(R.id.txtcandidateRequirements);

            //exp part
            exp= itemView.findViewById(R.id.benefit);
            exptxt= itemView.findViewById(R.id.txtbenefit);

            //interet part
            interet= itemView.findViewById(R.id.workexp);
            interettxt= itemView.findViewById(R.id.worktxt);

            avatarImg = itemView.findViewById(R.id.imageView);
        }
        void bindCard(myViewHolder holder, final UserModel userModels){
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
            addresstxt.setText(userModels.getUserAddress());
            nametxt.setText(userModels.getUsername());
            jobtxt.setText(userModels.getUserPosition());
            introtxt.setText(userModels.getUserIntroduce());
            universitytxt.setText(userModels.getUserUniversity());
            gpatxt.setText(userModels.getUsergpa().toString() + "/4");
            emailtxt.setText(userModels.getUserEmail());
            sdttxt.setText(userModels.getUserPhone());
            dobtxt.setText(new SimpleDateFormat("d'/'MM'/'yyyy").format(userModels.getUserDOB()));
            webtxt.setText(userModels.getUserWeb());
            skilltxt.setText(userModels.getUserSkill());
            certxt.setText(userModels.getUserCer());
            exptxt.setText(userModels.getUserExperience());
            interettxt.setText(userModels.getUserInterest());
            if(!userModels.getUserAvatar().equals("")){
                Picasso.get().load(userModels.getUserAvatar()).resize(1360,1370).centerCrop().into(avatarImg);
            }
            if(userModels.getUserIntroduce().equals("")){
                webtxt.setVisibility(View.GONE);
            }
            if(userModels.getUserWeb().equals("")){
                exp.setVisibility(View.GONE);
                exptxt.setVisibility(View.GONE);
            }
            if(userModels.getUserSkill().equals("")){
                skill.setVisibility(View.GONE);
                skilltxt.setVisibility(View.GONE);
            }
            if(userModels.getUserCer().equals("")){
                cer.setVisibility(View.GONE);
                certxt.setVisibility(View.GONE);
            }
            if(userModels.getUserInterest().equals("")){
                interet.setVisibility(View.GONE);
                interettxt.setVisibility(View.GONE);
            }
            if(userModels.getUserExperience().equals("")){
                exp.setVisibility(View.GONE);
                exptxt.setVisibility(View.GONE);
            }

        }
    }
}
