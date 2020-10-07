package com.example.bdranrestaurant.AdminPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bdranrestaurant.Login.LoginActivity;
import com.example.bdranrestaurant.MainScreen.MainActivity;
import com.example.bdranrestaurant.R;
import com.example.bdranrestaurant.ServePackage.ServeActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
RecyclerView admin_activity_orders_rv;
    List<AdminOrderItem> adminOrderItemArrayList=new ArrayList<>();
private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("sumbitted_order");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        admin_activity_orders_rv=findViewById(R.id.admin_activity_orders_rv);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                         String userid=snapshot.getKey();
                      String  username=snapshot.child("userInfo").child("username").getValue().toString();//userInfo --> username
                        String userPhone= snapshot.child("userInfo").child("phone").getValue().toString();//userInfo -->phone
                        Log.v("erynew",userid);
                        Log.v("erynew",username);
                        Log.v("erynew",userPhone);

                        adminOrderItemArrayList.add(new AdminOrderItem(username, userPhone,userid));

//                        for (DataSnapshot snapshot1 :snapshot.child("orderInfo").getChildren()) {
//                            if (snapshot1.hasChildren()) {
//                               String  orderInfo = snapshot1.getKey();
//                                Log.v("erynew", orderInfo);
//                               String order_number = snapshot1.child("number").getValue().toString();
//                               String order_total_price = snapshot1.child("total_price").getValue().toString();
//                                Log.v("erynew", order_number);
//                                Log.v("erynew", order_total_price);
//                            }
//                        }
                    }
                    AdminOrderItemAdapter adapter=new AdminOrderItemAdapter(adminOrderItemArrayList, new AdminOrderItemAdapter.OnUserClickListener() {
                        @Override
                        public void onUserClick(AdminOrderItem adminOrderItem) {
                            Intent intent =new Intent(AdminActivity.this, ServeActivity.class);
                            intent.putExtra("uid",adminOrderItem.getUid());
                            intent.putExtra("username",adminOrderItem.getUsername());
                            intent.putExtra("userphone",adminOrderItem.getPhonenumber());
                            startActivity(intent);
                        }
                    });
                    adapter.notifyDataSetChanged();
                    admin_activity_orders_rv.setAdapter(adapter);
                    admin_activity_orders_rv.setLayoutManager(new LinearLayoutManager(AdminActivity.this));
                        }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void signout(View view) {
        new MaterialAlertDialogBuilder(AdminActivity.this)
                .setTitle("Log out?")
                .setMessage("Are you sure you log out?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthUI.getInstance().signOut(AdminActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    }
                                });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .show();
    }
}
