package com.example.bdranrestaurant.PendingPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bdranrestaurant.CartPackage.OrderItems;
import com.example.bdranrestaurant.CartPackage.OrderRecyclerAdapter;
import com.example.bdranrestaurant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingActivity extends AppCompatActivity {
Toolbar toolbar;
    double total_price_tv=0;
RecyclerView pending_orders_rv;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference("Pending_orders");
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    String current_user_id=firebaseUser.getUid();
    TextView order_total_price_tv;
    List<OrderItems> orderItems=new ArrayList<>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        toolbar=findViewById(R.id.pending_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pending Orders");
        order_total_price_tv =findViewById(R.id.pending_orders_total_price_tv);
        incomingFirebaseOrders();
        pending_orders_rv=findViewById(R.id.pending_rv);
        pending_orders_rv.setEnabled(false);

    }
    public void incomingFirebaseOrders(){

        databaseReference.child(current_user_id).child("orderInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChildren()) {

                        String order_name = snapshot.getKey();
                        String order_desc = snapshot.child("orderDescription").getValue().toString();
                        String order_price = snapshot.child("price").getValue().toString();
                        String order_image = snapshot.child("order_image").getValue().toString();
                        String total_price = snapshot.child("total_price").getValue().toString();
                        String order_requested_number = snapshot.child("number").getValue().toString();
                        total_price_tv += Double.parseDouble(total_price);
                        orderItems.add(new OrderItems(order_image, order_name, order_desc, order_price, total_price, order_requested_number));

                    }else{


                    }
                }
                if (orderItems != null) {
                    com.example.bdranrestaurant.CartPackage.OrderRecyclerAdapter adapter = new OrderRecyclerAdapter(orderItems);
                    adapter.notifyDataSetChanged();
//                    Log.v("order",current_user_id);
                    order_total_price_tv.append(String.valueOf(total_price_tv));
                    pending_orders_rv.setAdapter(adapter);
                    pending_orders_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    pending_orders_rv.setHasFixedSize(true);
                } else if (orderItems == null){

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
