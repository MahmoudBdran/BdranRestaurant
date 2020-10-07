package com.example.bdranrestaurant.ServePackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bdranrestaurant.AdminPackage.AdminActivity;
import com.example.bdranrestaurant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServeActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference =firebaseDatabase.getReference("sumbitted_order");
    DatabaseReference deleteFromSumbittedOrders =firebaseDatabase.getReference("sumbitted_order");
    DatabaseReference deleteFromPendingRef=firebaseDatabase.getReference("Pending_orders");
    DatabaseReference finishedOrdersRef=firebaseDatabase.getReference("finished_orders");
    String orderName="";
    String orderNumber="";
    String order_total_price="";
    List<ServeModel> serveModelList=new ArrayList<>();
    RecyclerView serve_rv;
    TextView serve_username,serve_phone;
    Button serve_btn;
    TextView serve_order_total_price_tv;
    String requested_user_id="";
    String ordered_times="";
    double total_price=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serve);
        Intent incomgintent=getIntent();
        Log.v("uid",incomgintent.getStringExtra("uid"));
        requested_user_id=incomgintent.getStringExtra("uid");
        serve_rv=findViewById(R.id.serve_rv);
        serve_btn=findViewById(R.id.accept_order);
        serve_order_total_price_tv=findViewById(R.id.serve_order_total_price_tv);
        serve_username=findViewById(R.id.serve_username);
        serve_phone=findViewById(R.id.serve_phone);
        serve_username.setText(incomgintent.getStringExtra("username"));
        serve_phone.setText(incomgintent.getStringExtra("userphone"));
        serve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptOrder();
            }
        });

        databaseReference.child(incomgintent.getStringExtra("uid")).child("orderInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    if (snapshot.hasChildren()){

                         orderName =snapshot.getKey();
                        orderNumber=snapshot.child("number").getValue().toString();
                         order_total_price=snapshot.child("total_price").getValue().toString();
                         total_price+=Double.parseDouble(order_total_price);
                        serveModelList.add(new ServeModel(orderName,orderNumber,order_total_price));
                    }
                }
                serve_order_total_price_tv.append(String.valueOf(total_price));
                ServeRecyclerAdapter adapter =new ServeRecyclerAdapter(serveModelList);
                serve_rv.setLayoutManager(new LinearLayoutManager(ServeActivity.this));
                serve_rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    void acceptOrder(){

        deleteFromSumbittedOrders.child(requested_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    deleteFromPendingRef.child(requested_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ServeActivity.this, "Accepted Orders", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ServeActivity.this, AdminActivity.class));
                        }else{
                            Toast.makeText(ServeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                }
            }
        });
    }
}
