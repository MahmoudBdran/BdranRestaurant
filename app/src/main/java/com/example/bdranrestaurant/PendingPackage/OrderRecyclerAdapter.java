package com.example.bdranrestaurant.PendingPackage;

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

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder> {
    List<OrderItems> orderItems;

    public OrderRecyclerAdapter(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_custom_layout,null,false);
        OrderViewHolder viewHolder = new OrderViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItems o = orderItems.get(position);
        holder.order_name.setText(o.getName());
        holder.order_desc.setText(o.getDesc());
        Picasso.get().load(o.getImage()).into(holder.order_image);
        holder.order_price.setText(o.getPrice());
        holder.order_total_price.setText(o.getTotal_price());
        holder.order_requested_number.setText(o.getOrder_requested_number());

    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView order_image;
        TextView order_name, order_desc ,order_price,order_total_price,order_requested_number;
        OrderItems orderItem;
        FloatingActionButton order_delete_fbtn;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            order_image=itemView.findViewById(R.id.order_image);
            order_name=itemView.findViewById(R.id.order_name);
            order_desc=itemView.findViewById(R.id.order_description);
            order_price=itemView.findViewById(R.id.order_price);
            order_total_price=itemView.findViewById(R.id.custom_order_total_price_tv);
            order_requested_number=itemView.findViewById(R.id.order_requested_number);

        }
    }
}
