package com.example.bdranrestaurant.DrinksPackage;

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

public class DrinksCategoriesAdapter extends RecyclerView.Adapter<DrinksCategoriesAdapter.DrinksCategoriesViewHolder> {
    List<DrinksCategories> drinksCategoriesList;
    OnDrinksCategoryClickListener listener;

    public DrinksCategoriesAdapter(List<DrinksCategories> drinksCategoriesList, OnDrinksCategoryClickListener listener) {
        this.drinksCategoriesList = drinksCategoriesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DrinksCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_drinks_item,parent,false);
        DrinksCategoriesViewHolder drinksCategoriesViewHolder = new DrinksCategoriesViewHolder(v);
        return drinksCategoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DrinksCategoriesViewHolder holder, int position) {
        DrinksCategories d = drinksCategoriesList.get(position);
        Picasso.get().load(d.getImage()).into(holder.category_image);
        holder.category_name.setText(d.getName());
        holder.category_desc.setText(d.getDesc());
        holder.category_price.setText(d.getPrice());
        holder.bind(d);
    }

    @Override
    public int getItemCount() {
        return drinksCategoriesList.size();
    }

    public class DrinksCategoriesViewHolder extends RecyclerView.ViewHolder {
        ImageView category_image;
        TextView category_name, category_desc ,category_price;
        DrinksCategories categories;
        FloatingActionButton order_fbtn;
        public DrinksCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            category_image=itemView.findViewById(R.id.category_image);
            category_name=itemView.findViewById(R.id.category_name);
            category_desc=itemView.findViewById(R.id.category_description);
            category_price=itemView.findViewById(R.id.category_price);
            order_fbtn=itemView.findViewById(R.id.order_fbtn);
            order_fbtn.setBackgroundResource(R.color.green_layout_color);
            order_fbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCategoryClick(categories);
                }
            });
        }
        void bind(DrinksCategories categories){
            this.categories=categories;
        }

    }
}
