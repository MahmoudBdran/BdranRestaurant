package com.example.bdranrestaurant.DrinksPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bdranrestaurant.FoodPackage.Food_components;
import com.example.bdranrestaurant.FoodPackage.OnFoodSliderClickListener;
import com.example.bdranrestaurant.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;


public class DrinksSliderAdapter extends SliderViewAdapter<DrinksSliderAdapter.PagerViewHolder> {
    List<DrinksComponents> drinks_list;
    OnDrinksSliderCLickListener listener;

    public DrinksSliderAdapter(List<DrinksComponents> drinks_list, OnDrinksSliderCLickListener listener) {
        this.drinks_list = drinks_list;
        this.listener=listener;

    }


    @Override
    public PagerViewHolder onCreateViewHolder(ViewGroup parent) {

        return new PagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cutom_food_slide,parent,false));
    }

    @Override
    public void onBindViewHolder(PagerViewHolder viewHolder, int position) {
            DrinksComponents d = drinks_list.get(position);
            viewHolder.imageView.setImageResource(d.getImage());
            viewHolder.bind(d);
    }


    @Override
    public int getCount() {
        return drinks_list.size();
    }

    public class PagerViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;
        TextView title , body;
        DrinksComponents components;
        public PagerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.slide_image);
            title=itemView.findViewById(R.id.slide_title);
            body= itemView.findViewById(R.id.slide_body);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDrinksSlideCLick(components,imageView);
                }
            });

        }
        void bind(DrinksComponents components){
            this.components=components;
        }

    }
}
