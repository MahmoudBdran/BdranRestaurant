package com.example.bdranrestaurant.FoodPackage;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bdranrestaurant.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    List<Categories> categories;
    OnCategoryCLickListener listener;
    public CategoriesAdapter(List<Categories> categories,OnCategoryCLickListener listener) {
        this.categories = categories;
        this.listener=listener;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_custom_layout,parent,false);
        CategoriesViewHolder viewHolder = new CategoriesViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Categories c = categories.get(position);
        Picasso.get().load(c.getImage()).into(holder.category_image);
        holder.category_name.setText(c.getName());
        holder.category_desc.setText(c.getDesc());
        holder.category_price.setText(c.getPrice());
        holder.bind(c);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        ImageView category_image;
        TextView category_name, category_desc ,category_price;
        Categories categories;
        FloatingActionButton order_fbtn;
        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            category_image=itemView.findViewById(R.id.category_image);
            category_name=itemView.findViewById(R.id.category_name);
            category_desc=itemView.findViewById(R.id.category_description);
            category_price=itemView.findViewById(R.id.category_price);
            order_fbtn=itemView.findViewById(R.id.order_fbtn);
            order_fbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCategoryClick(categories);
                }
            });
        }
        void bind(Categories categories){
            this.categories=categories;
        }

    }


}
