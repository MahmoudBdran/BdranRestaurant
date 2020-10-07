package com.example.bdranrestaurant.CartPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bdranrestaurant.MainScreen.MainActivity;
import com.example.bdranrestaurant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
//    private AdView mAdView;
    RecyclerView recyclerView;
    double total_price_tv=0;
    Toolbar toolbar;
    int i=0;
    String myname="";
    String myphone="";
    TextView order_total_price_tv;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference("orders");
    private DatabaseReference sendorder=firebaseDatabase.getReference("sumbitted_order");
    private DatabaseReference sendorder2=firebaseDatabase.getReference("sumbitted_order");
    private DatabaseReference sendorder3=firebaseDatabase.getReference("Pending_orders");
    private DatabaseReference sendorder4=firebaseDatabase.getReference("Pending_orders");
    private DatabaseReference deleteorderfromordersRef=firebaseDatabase.getReference("orders");

    private DatabaseReference mydata=firebaseDatabase.getReference("users");
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    String current_user_id=firebaseUser.getUid();
    List<OrderItems>orderItems=new ArrayList<>() ;
    ImageView emptycart;
    TextView no_items_tv;
    Button order_btn;

    String ordername="",order_requested_number="",order_totalprice="",order_image="",orderDescription="",price="";
    List<OrderItems> list=new ArrayList<>();
    List<OrderItems> list2=new ArrayList<>();
    List<List<String>> orderList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView =findViewById(R.id.order_rv);
        emptycart=findViewById(R.id.empty_cart_iv);
        no_items_tv=findViewById(R.id.no_items_tv);
        toolbar=findViewById(R.id.toolbar);
        order_btn=findViewById(R.id.order_btn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setTitle("");
        order_btn.setVisibility(View.INVISIBLE);
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPendingOrder();
                sendOrder();
            }
        });
        order_total_price_tv=findViewById(R.id.order_total_price_tv);

        incomingFirebaseOrders();
//        mAdView = findViewById(R.id.adView);
//        MobileAds.initialize(this, "ca-app-pub-9853915689887157~4018129964");
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }
    public void incomingFirebaseOrders(){
        emptycart.setVisibility(ImageView.INVISIBLE);
        no_items_tv.setVisibility(TextView.INVISIBLE);
        databaseReference.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("price").getValue()!=null&&snapshot.child("total_price").getValue()!=null&&snapshot.child("number").getValue()!=null) {
                        order_btn.setVisibility(View.VISIBLE);
                        String order_name = snapshot.getKey();
                        String order_desc = snapshot.child("description").getValue().toString();
                           String order_price = snapshot.child("price").getValue().toString();
                           String order_image = snapshot.child("image").getValue().toString();
                           String total_price = snapshot.child("total_price").getValue().toString();
                           String order_requested_number = snapshot.child("number").getValue().toString();
                           total_price_tv += Double.parseDouble(total_price);
                           orderItems.add(new OrderItems(order_image, order_name, order_desc, order_price, total_price, order_requested_number));

                    }else{
                        order_btn.setVisibility(View.INVISIBLE);
                        emptycart.setVisibility(View.VISIBLE);
                        no_items_tv.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);

                    }
                }
                if (orderItems != null) {
                    OrderRecyclerAdapter adapter = new OrderRecyclerAdapter(orderItems);
                    adapter.notifyDataSetChanged();
//                    Log.v("order",current_user_id);
                    order_total_price_tv.append(String.valueOf(total_price_tv));
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setHasFixedSize(true);
                } else if (orderItems == null){
                    order_btn.setVisibility(View.INVISIBLE);
                    emptycart.setVisibility(View.VISIBLE);
                    no_items_tv.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendOrder(){
        list=orderItems;

        if (list!=null){
            for ( i =0 ; i<list.size();i++){
                Log.v("hbl",list.get(i).getName());
                Log.v("hbl",list.get(i).getOrder_requested_number());
                Log.v("hbl",list.get(i).getTotal_price());
                ordername=list.get(i).getName();
                order_requested_number=list.get(i).getOrder_requested_number();
                order_totalprice=list.get(i).getTotal_price();
                order_image=list.get(i).getImage();
                orderDescription=list.get(i).getDesc();
                price=list.get(i).getPrice();
                Log.v("imagge",order_image);
                sendorder.child(current_user_id).child("orderInfo").child(ordername).child("number")
                        .setValue(order_requested_number).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }
                    }
                });
//                sendorder.child(current_user_id).child("orderInfo").child(ordername).child("image")
//                        .setValue(order_image).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//
//                        }
//                    }
//                });
//                sendorder.child(current_user_id).child("orderInfo").child(ordername).child("description")
//                        .setValue(orderDescription).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//
//                        }
//                    }
//                });
//                sendorder.child(current_user_id).child("orderInfo").child(ordername).child("price")
//                        .setValue(price).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//
//                        }
//                    }
//                });
                sendorder.child(current_user_id).child("orderInfo").child(ordername).child("total_price")
                        .setValue(order_totalprice).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            userInfoSend();

//                            Toast.makeText(CartActivity.this, "Orders been submitted successfully", Toast.LENGTH_SHORT).show();

                        }else{
                        }
                    }
                });
                if (i==(list.size()-1)){
                    startActivity(new Intent(CartActivity.this,MainActivity.class));
                    Toast.makeText(CartActivity.this, "Orders been submitted successfully", Toast.LENGTH_SHORT).show();

                }
            }

        }

    }
    void userInfoSend(){

        mydata.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myname= dataSnapshot.child("name").getValue().toString();
                Log.v("mydata",myname);
                myphone= dataSnapshot.child("phone").getValue().toString();
                Log.v("mydata",myphone);
                sendorder2.child(current_user_id).child("userInfo").child("username").setValue(myname).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.v("yeeah",myname);
                            sendorder.child(current_user_id).child("userInfo").child("phone").setValue(myphone).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                    }else{

                                    }
                                }
                            });
                            deleteorderfromordersRef.child(current_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                        }else{
                            Toast.makeText(CartActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendPendingOrder(){
        list2=orderItems;

        if (list2!=null){
            for ( i =0 ; i<list2.size();i++){
                Log.v("hebl",list2.get(i).getImage());
                Log.v("hebl",list2.get(i).getDesc());
                Log.v("hebl",list2.get(i).getPrice());

                ordername=list2.get(i).getName();
                order_requested_number=list2.get(i).getOrder_requested_number();
                order_totalprice=list2.get(i).getTotal_price();
                order_image=list2.get(i).getImage();
                orderDescription=list2.get(i).getDesc();
                price=list2.get(i).getPrice();
                sendorder3.child(current_user_id).child("orderInfo").child(ordername).child("number")
                        .setValue(order_requested_number).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }
                    }
                });
                sendorder3.child(current_user_id).child("orderInfo").child(ordername).child("order_image")
                        .setValue(order_image).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }
                    }
                });
                sendorder3.child(current_user_id).child("orderInfo").child(ordername).child("orderDescription")
                        .setValue(orderDescription).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }
                    }
                });
                sendorder3.child(current_user_id).child("orderInfo").child(ordername).child("price")
                        .setValue(price).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }
                    }
                });
                sendorder3.child(current_user_id).child("orderInfo").child(ordername).child("total_price")
                        .setValue(order_totalprice).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
////                            PendinguserInfoSend();
//                            startActivity(new Intent(CartActivity.this,MainActivity.class));
//                            if (i==(list.size()-1)){
//
//                                Toast.makeText(CartActivity.this, "Orders been submitted successfully", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }else{
//                        }
                    }
                });
            }
        }

    }
    void PendinguserInfoSend(){

        mydata.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myname= dataSnapshot.child("name").getValue().toString();
                Log.v("mydata",myname);
                myphone= dataSnapshot.child("phone").getValue().toString();
                Log.v("mydata",myphone);
                sendorder4.child(current_user_id).child("userInfo").child("username").setValue(myname).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.v("yeeah",myname);
                            sendorder3.child(current_user_id).child("userInfo").child("phone").setValue(myphone).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                    }else{

                                    }
                                }
                            });

                        }else{
                            Toast.makeText(CartActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

