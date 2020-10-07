package com.example.bdranrestaurant.AdminPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bdranrestaurant.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminOrderItemAdapter extends RecyclerView.Adapter<AdminOrderItemAdapter.AdminOrderItemViewHolder> {
    List<AdminOrderItem> adminOrderItemArrayList;
    OnUserClickListener listener;

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference("submitted_orders");
    public AdminOrderItemAdapter(List<AdminOrderItem> adminOrderItemArrayList,OnUserClickListener listener) {
        this.adminOrderItemArrayList = adminOrderItemArrayList;
        this.listener=listener;

    }

    @NonNull
    @Override
    public AdminOrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_item,parent,false);
        AdminOrderItemViewHolder admin_order_itemItemViewHolder =new AdminOrderItemViewHolder(v);
        return admin_order_itemItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderItemViewHolder holder, int position) {
        AdminOrderItem i =adminOrderItemArrayList.get(position);
        holder.admin_order_item_username_tv.setText(i.getUsername());
        holder.admin_order_item_phone_tv.setText(i.getPhonenumber());
        holder.bind(i);
    }
    @Override
    public int getItemCount() {
        return adminOrderItemArrayList.size();
    }

    public class AdminOrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView admin_order_item_username_tv,admin_order_item_phone_tv;
        Button serve_btn;
        AdminOrderItem adminOrderItem;
        public AdminOrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            admin_order_item_username_tv=itemView.findViewById(R.id.admin_order_item_username_tv);
            admin_order_item_phone_tv=itemView.findViewById(R.id.admin_order_item_phone_tv);
            serve_btn=itemView.findViewById(R.id.serve_btn);
            serve_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserClick(adminOrderItem);
                }
            });
        }
        void bind(AdminOrderItem adminOrderItem){
            this.adminOrderItem=adminOrderItem;
        }

    }
    public interface OnUserClickListener {
        void onUserClick(AdminOrderItem adminOrderItem);
    }
}