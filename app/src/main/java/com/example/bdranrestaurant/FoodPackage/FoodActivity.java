package com.example.bdranrestaurant.FoodPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bdranrestaurant.CartPackage.CartActivity;
import com.example.bdranrestaurant.Login.LoginActivity;
import com.example.bdranrestaurant.PendingPackage.PendingActivity;
import com.example.bdranrestaurant.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {
    SliderView food_slider;
    private ProgressDialog loadingBurgerProgress;
    BottomNavigationView category_tablayout;
    RecyclerView rv;
    double total_price=0;
    private TextView username_tv;
    boolean dont_add_from_burger_firebase_again_bitch=false;
    List<Categories> burger_list =new ArrayList<>() ;
    List<Categories> pizza_list =new ArrayList<>() ;
    List<Categories> chicken_list =new ArrayList<>() ;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser =mAuth.getCurrentUser();
    String current_user=firebaseUser.getUid();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference =firebaseDatabase.getReference("categories");
    private DatabaseReference user_databaseReference =firebaseDatabase.getReference("users");
    private DatabaseReference database_order_reference =firebaseDatabase.getReference("orders");
    CoordinatorLayout food_root_layout;
    FloatingActionButton settings_fab,cart_fab,exit_fab,food_pending_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        loadingBurgerProgress=new ProgressDialog(this);
        food_slider=findViewById(R.id.above_slider_drinks);
        category_tablayout=findViewById(R.id.categories_tablayout);
        settings_fab=findViewById(R.id.settings_fab);
        cart_fab=findViewById(R.id.food_cart_fab);
        exit_fab=findViewById(R.id.food_exit_fab);
        food_pending_fab=findViewById(R.id.food_pending_fab);
        cart_fab.setVisibility(View.INVISIBLE);
        exit_fab.setVisibility(View.INVISIBLE);
        food_pending_fab.setVisibility(View.INVISIBLE);
        username_tv=findViewById(R.id.username_tv);
        user_databaseReference.child(current_user).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username_tv.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        food_root_layout=findViewById(R.id.food_root_layout);
        settings_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(food_root_layout);
                if ( exit_fab.getVisibility()== View.INVISIBLE){

                    food_pending_fab.setVisibility(View.VISIBLE);
                    cart_fab.setVisibility(View.VISIBLE);
                    exit_fab.setVisibility(View.VISIBLE);
                }else{
                    food_pending_fab.setVisibility(View.INVISIBLE);
                    cart_fab.setVisibility(View.INVISIBLE);
                    exit_fab.setVisibility(View.INVISIBLE);
                }
            }
        });
        cart_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodActivity.this,CartActivity.class));
            }
        });
        exit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    logout();
            }
        });
        food_pending_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodActivity.this, PendingActivity.class));
            }
        });
        rv=findViewById(R.id.rv);

        first_load();
//        incomingFirebaseBurger();
        incomingFirebaseChicken();
        incomingFirebasePizza();
        List<Food_components> components = new ArrayList<>();
        components.add(new Food_components(R.drawable.burger));
        components.add(new Food_components(R.drawable.pizza));
        components.add(new Food_components(R.drawable.chicken));
        food_slider.setSliderAdapter(new FoodSLiderAdapter(components, new OnFoodSliderClickListener() {
            @Override
            public void onFoodSlideCLick(Food_components components, ImageView imageView) {

            }
        }));

        category_tablayout.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.burger:
                        if (dont_add_from_burger_firebase_again_bitch==false){
                            incomingFirebaseBurger();
                            dont_add_from_burger_firebase_again_bitch=true;


                        }else{
                            recyclerViewContents(burger_list);

                        }

                        break;
                    case R.id.pizza:
                        recyclerViewContents(pizza_list);




                        break;
                    case R.id.chicken:
                        recyclerViewContents(chicken_list);


                    break;

                }


                return true;
            }
        });

    }
    public void recyclerViewContents(List<Categories> categories ){


        CategoriesAdapter adapter = new CategoriesAdapter(categories, new OnCategoryCLickListener() {
            @Override
            public void onCategoryClick(Categories categories) {
                onCategoryClickDialoge(categories);
            }
        });
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
    }
    void incomingFirebaseBurger(){
        databaseReference.child("food").child("burger").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String burger_name =snapshot.getKey();

                    String burger_desc=snapshot.child("description").getValue().toString();
                    String burger_price=snapshot.child("price").getValue().toString();
                    String burger_image=snapshot.child("image").getValue().toString();

                    burger_list.add(new Categories(burger_image,burger_name,burger_desc,burger_price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        CategoriesAdapter adapter = new CategoriesAdapter(burger_list, new OnCategoryCLickListener() {
            @Override
            public void onCategoryClick(Categories categories) {
                onCategoryClickDialoge(categories);
            }
        });
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
    }
    void incomingFirebasePizza(){

        databaseReference.child("food").child("pizza").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String burger_name =snapshot.getKey();
                    String burger_desc=snapshot.child("description").getValue().toString();
                    String burger_price=snapshot.child("price").getValue().toString();
                    String burger_image=snapshot.child("image").getValue().toString();
                    pizza_list.add(new Categories(burger_image,burger_name,burger_desc,burger_price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        CategoriesAdapter adapter = new CategoriesAdapter(pizza_list, new OnCategoryCLickListener() {
            @Override
            public void onCategoryClick(Categories categories) {
                onCategoryClickDialoge(categories);
            }
        });
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
    }
    void incomingFirebaseChicken(){
        databaseReference.child("food").child("chicken").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String burger_name =snapshot.getKey();

                    String burger_desc=snapshot.child("description").getValue().toString();
                    String burger_price=snapshot.child("price").getValue().toString();
                    String burger_image=snapshot.child("image").getValue().toString();

                    chicken_list.add(new Categories(burger_image,burger_name,burger_desc,burger_price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        CategoriesAdapter adapter = new CategoriesAdapter(chicken_list, new OnCategoryCLickListener() {
            @Override
            public void onCategoryClick(Categories categories) {
                onCategoryClickDialoge(categories);
            }
        });
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
    }


    @Override
    protected void onStart() {
        super.onStart();





    }
     void onCategoryClickDialoge(final Categories categories){
        View view = getLayoutInflater().inflate(R.layout.selected_category_in_dialoge,null);
        ImageView selected_image=view.findViewById(R.id.selected_category_image);
        final TextView selected_name=view.findViewById(R.id.selected_category_name);
        TextView selected_desc=view.findViewById(R.id.selected_category_description);
        TextView selected_price=view.findViewById(R.id.selected_category_price);
         final EditText selected_category_number=view.findViewById(R.id.category_number);
        Picasso.get().load(categories.getImage()).into(selected_image);
        selected_name.setText(categories.getName());
        selected_desc.setText(categories.getDesc());
        selected_price.setText(categories.getPrice());


         Log.v("ayhbl",categories.getPrice());

        new MaterialAlertDialogBuilder(FoodActivity.this)
                .setTitle("Order")
                .setMessage("Sure to add "+categories.getName()+" to shopping cart?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                          total_price=Double.parseDouble(categories.price) * Double.parseDouble(selected_category_number.getText().toString());
                        database_order_reference.child(current_user).child(categories.getName()).child("description")
                                .setValue(categories.getDesc()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.v("ayhbl",categories.getDesc());
                                    database_order_reference.child(current_user).child(categories.getName()).child("image")
                                            .setValue(categories.getImage()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Log.v("ayhbl",categories.getImage());
                                                database_order_reference.child(current_user).child(categories.getName()).child("price")
                                                        .setValue(categories.getPrice()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            database_order_reference.child(current_user).child(categories.getName()).child("number")
                                                                    .setValue(selected_category_number.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){

                                                                        database_order_reference.child(current_user).child(categories.getName()).child("total_price")
                                                                                .setValue(total_price).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()){
                                                                                    Toast.makeText(FoodActivity.this, "added to cart successfully", Toast.LENGTH_SHORT).show();
                                                                                }else{
                                                                                    Toast.makeText(FoodActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    Log.v("error",task.getException().getMessage());}
                                                                            }
                                                                        });
                                                                    }else{
                                                                        Toast.makeText(FoodActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        Log.v("error",task.getException().getMessage()); }
                                                                }
                                                            });
                                                        }else{
                                                            Toast.makeText(FoodActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            Log.v("error",task.getException().getMessage());}
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(FoodActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.v("error",task.getException().getMessage());}
                                        }
                                    });
                                }else{
                                    Toast.makeText(FoodActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.v("error",task.getException().getMessage());
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(FoodActivity.this, "cancelled.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setView(view)

                .show();
    }
    private void first_load(){

        databaseReference.child("food").child("burger").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String burger_name =snapshot.getKey();

                        String burger_desc=snapshot.child("description").getValue().toString();
                        String burger_price=snapshot.child("price").getValue().toString();
                        String burger_image=snapshot.child("image").getValue().toString();

                        burger_list.add(new Categories(burger_image,burger_name,burger_desc,burger_price));
                        Log.v("Log"," iam here");
                    }
//                loadingBurgerProgress.dismiss();
                    dont_add_from_burger_firebase_again_bitch=true;

                }
                CategoriesAdapter adapter = new CategoriesAdapter(burger_list, new OnCategoryCLickListener() {
                    @Override
                    public void onCategoryClick(Categories categories) {
                        onCategoryClickDialoge(categories);
                    }
                });
                adapter.notifyDataSetChanged();

                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rv.setHasFixedSize(true);
                Log.v("Log"," recycler here");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    void logout(){
        new MaterialAlertDialogBuilder(FoodActivity.this)
                .setTitle("Log out?")
                .setMessage("Are you sure you log out?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthUI.getInstance().signOut(FoodActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            startActivity(new Intent(FoodActivity.this,LoginActivity.class));
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
