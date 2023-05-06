package com.b1906478.gojob.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.b1906478.gojob.R;
import com.b1906478.gojob.model.Career;

import java.util.ArrayList;
import java.util.List;

public class careerAdapter extends  RecyclerView.Adapter<careerAdapter.careerViewHolder>{
    private List<Career> careers;
    private careerListener careerlistener;

    public careerAdapter(List<Career> careers, careerListener careerlistener) {
        this.careers = careers;
        this.careerlistener = careerlistener;
    }

    @NonNull
    @Override
    public careerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new careerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_career_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull careerViewHolder holder, int position) {
        holder.bindCareer(careers.get(position));
    }

    @Override
    public int getItemCount() {
        return careers.size();
    }

    public List<Career> getSelectedCareer(){
        List<Career> selectedCareer = new ArrayList<>();
        for (Career career : careers){
            if (career.isSelected){
                selectedCareer.add(career);
            }
        }
        return selectedCareer;
    }

    class careerViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layoutCareer;
        TextView txtCareer;
        ImageView careerIc;
        View viewbackground;

        public careerViewHolder(@NonNull View itemView) {
            super(itemView);
            viewbackground = itemView.findViewById(R.id.viewBackground);
            layoutCareer = itemView.findViewById(R.id.layoutCareer);
            txtCareer = itemView.findViewById(R.id.txtCareer);
        }

        void bindCareer(final Career career){
            txtCareer.setText(career.nameCareer);
            if(career.isSelected){
                viewbackground.setBackgroundResource(R.drawable.career_background_selected);
                txtCareer.setTextColor(txtCareer.getResources().getColor(R.color.white));
            }else {
                viewbackground.setBackgroundResource(R.drawable.career_background);
                txtCareer.setTextColor(txtCareer.getResources().getColor(R.color.darkmodedfont));
            }
            layoutCareer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(career.isSelected)
                    {
                        viewbackground.setBackgroundResource(R.drawable.career_background);
                        txtCareer.setTextColor(view.getResources().getColor(R.color.darkmodedfont));
                        career.isSelected = false;
                        if (getSelectedCareer().size() == 0){
                            careerlistener.onCareerAction(false);
                        }
                    }else {
                        txtCareer.setTextColor(view.getResources().getColor(R.color.white));
                        viewbackground.setBackgroundResource(R.drawable.career_background_selected);
                        career.isSelected = true;
                        careerlistener.onCareerAction(true);
                    }
                }
            });
        }
    }
}
