package com.example.bdranrestaurant.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bdranrestaurant.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ViewPagerAdapter extends SliderViewAdapter<ViewPagerAdapter.PagerViewHolder> {
    List<Components> Components;
    OnSliderCLickListener listener;

    public ViewPagerAdapter(List<Components> Components, OnSliderCLickListener listener) {
        this.Components = Components;
        this.listener=listener;

    }


    @Override
    public PagerViewHolder onCreateViewHolder(ViewGroup parent) {

        return new PagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_slide,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {

        Components i = Components.get(position);
        holder.imageView.setImageResource(i.getImage());
        holder.title.setText(i.getTitle());
        holder.body.setText(i.getBody());
        holder.bind(i);

    }


    @Override
    public int getCount() {
        return Components.size();
    }

    public class PagerViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;
        TextView title , body;
        Components components;
        public PagerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.slide_image);
            title=itemView.findViewById(R.id.slide_title);
            body= itemView.findViewById(R.id.slide_body);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSlideCLick(components,imageView,title,body);
                }
            });

        }
        void bind(Components components){
            this.components=components;
        }

    }
}
