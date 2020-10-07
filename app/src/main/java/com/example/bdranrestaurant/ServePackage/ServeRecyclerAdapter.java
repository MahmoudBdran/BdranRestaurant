package com.example.bdranrestaurant.ServePackage;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bdranrestaurant.R;

import java.util.List;

public class ServeRecyclerAdapter extends RecyclerView.Adapter<ServeRecyclerAdapter.ServeVH> {
    List<ServeModel> serveModelList;

    public ServeRecyclerAdapter(List<ServeModel> serveModelList) {
        this.serveModelList = serveModelList;
    }

    @NonNull
    @Override
    public ServeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServeVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.serve_activity_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServeVH holder, int position) {
        ServeModel s = serveModelList.get(position);
        holder.serve_orderName.setText(s.getOrderName());
        if (Integer.parseInt(s.getOrderNumber())>1){
            holder.serve_orderNumber.setText(s.getOrderNumber()+" items orderd");
        }else {
            holder.serve_orderNumber.setText(s.getOrderNumber()+"item orderd");
        }
        holder.serve_orderTotalPrice.setText(s.getOrderTotalPrice()+"$");

    }
    @Override
    public int getItemCount() {
        return serveModelList.size();
    }

    public class ServeVH extends RecyclerView.ViewHolder {
        TextView serve_orderName,serve_orderNumber,serve_orderTotalPrice;

        public ServeVH(@NonNull View itemView) {
            super(itemView);
            serve_orderName=itemView.findViewById(R.id.serve_order_name);
            serve_orderNumber=itemView.findViewById(R.id.serve_order_number);
            serve_orderTotalPrice=itemView.findViewById(R.id.serve_order_total_price);
        }
    }
}
