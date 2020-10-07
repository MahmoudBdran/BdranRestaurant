package com.example.bdranrestaurant.FoodPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bdranrestaurant.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class FoodSLiderAdapter extends SliderViewAdapter<FoodSLiderAdapter.PagerViewHolder> {
    List<Food_components> Components;
    OnFoodSliderClickListener listener;

    public FoodSLiderAdapter(List<Food_components> Components, OnFoodSliderClickListener listener) {
        this.Components = Components;
        this.listener=listener;

    }


    @Override
    public PagerViewHolder onCreateViewHolder(ViewGroup parent) {

        return new PagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cutom_food_slide,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {

        Food_components i = Components.get(position);
        holder.imageView.setImageResource(i.getImage());
        holder.bind(i);

    }


    @Override
    public int getCount() {
        return Components.size();
    }

    public class PagerViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;
        TextView title , body;
        Food_components components;
        public PagerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.slide_image);
            title=itemView.findViewById(R.id.slide_title);
            body= itemView.findViewById(R.id.slide_body);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFoodSlideCLick(components,imageView);
                }
            });

        }
        void bind(Food_components components){
            this.components=components;
        }

    }
}
