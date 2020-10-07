package com.example.bdranrestaurant.DrinksPackage;

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
import com.example.bdranrestaurant.FoodPackage.Categories;
import com.example.bdranrestaurant.FoodPackage.CategoriesAdapter;
import com.example.bdranrestaurant.FoodPackage.FoodSLiderAdapter;
import com.example.bdranrestaurant.FoodPackage.Food_components;
import com.example.bdranrestaurant.FoodPackage.OnCategoryCLickListener;
import com.example.bdranrestaurant.FoodPackage.OnFoodSliderClickListener;
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

public class DrinksActivity extends AppCompatActivity {
    SliderView drinks_slider;
    private ProgressDialog loadingBurgerProgress;
    BottomNavigationView drinks_tablayout;
    RecyclerView rv;
    double total_price=0;
    private TextView drinks_username_tv;
    boolean dont_add_from_hot_firebase_again_bitch =false;
    List<DrinksCategories> hot_list =new ArrayList<>() ;
    List<DrinksCategories> juice_list =new ArrayList<>() ;
    List<DrinksCategories> soda_list =new ArrayList<>() ;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser =mAuth.getCurrentUser();
    String current_user=firebaseUser.getUid();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference =firebaseDatabase.getReference("categories");

    private DatabaseReference user_database_reference =firebaseDatabase.getReference("users");
    private DatabaseReference database_order_reference =firebaseDatabase.getReference("orders");
    CoordinatorLayout drinks_root_layout;
    FloatingActionButton drinks_settings_fab, drinks_cart_fab, drinks_exit_fab,drinks_pending_fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);
        loadingBurgerProgress=new ProgressDialog(this);
        drinks_slider =findViewById(R.id.above_slider_drinks);
        drinks_tablayout =findViewById(R.id.categories_tablayout);
        drinks_settings_fab =findViewById(R.id.drinks_settings_fab);
        drinks_username_tv=findViewById(R.id.drinks_username_tv);
        drinks_cart_fab =findViewById(R.id.drinks_cart_fab);
        drinks_exit_fab =findViewById(R.id.drinks_exit_fab);
        drinks_pending_fab=findViewById(R.id.drinks_pending_fab);
        drinks_pending_fab.setVisibility(View.INVISIBLE);
        drinks_cart_fab.setVisibility(View.INVISIBLE);
        drinks_exit_fab.setVisibility(View.INVISIBLE);
        drinks_root_layout=findViewById(R.id.drinks_root_layout);
        user_database_reference.child(current_user).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drinks_username_tv.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        drinks_settings_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(drinks_root_layout);
                if ( drinks_exit_fab.getVisibility()== View.INVISIBLE){
                    drinks_pending_fab.setVisibility(View.VISIBLE);
                    drinks_cart_fab.setVisibility(View.VISIBLE);
                    drinks_exit_fab.setVisibility(View.VISIBLE);
                }else{
                    drinks_pending_fab.setVisibility(View.INVISIBLE);
                    drinks_cart_fab.setVisibility(View.INVISIBLE);
                    drinks_exit_fab.setVisibility(View.INVISIBLE);
                }
            }
        });
        drinks_cart_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrinksActivity.this, CartActivity.class));
            }
        });
        drinks_exit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        drinks_pending_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrinksActivity.this, PendingActivity.class));
            }
        });
        rv=findViewById(R.id.drinks_rv);

        first_load();
//        incomingFirebaseHot();
        incomingFirebasejuice();
        incomingFirebaseSoda();
        List<Food_components> components = new ArrayList<>();
        components.add(new Food_components(R.drawable.drinks_slider_image1));
        components.add(new Food_components(R.drawable.drinks_slider_image2));
        components.add(new Food_components(R.drawable.drinks_slider_image3));
        drinks_slider.setSliderAdapter(new FoodSLiderAdapter(components, new OnFoodSliderClickListener() {
            @Override
            public void onFoodSlideCLick(Food_components components, ImageView imageView) {


            }
        }));

        drinks_tablayout.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.hot:
                        if (dont_add_from_hot_firebase_again_bitch ==false){
                            incomingFirebaseHot();
                            dont_add_from_hot_firebase_again_bitch =true;


                        }else{
                            recyclerViewContents(hot_list);

                        }

                        break;
                    case R.id.juice:
                        recyclerViewContents(juice_list);




                        break;
                    case R.id.soda:
                        recyclerViewContents(soda_list);


                        break;

                }


                return true;
            }
        });

    }
    //on create end
    public void recyclerViewContents(List<DrinksCategories> categories ){


        DrinksCategoriesAdapter adapter = new DrinksCategoriesAdapter(hot_list, new OnDrinksCategoryClickListener() {
            @Override
            public void onCategoryClick(DrinksCategories categories) {
                onCategoryClickDialoge(categories);
            }
        });

        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
    }


    void incomingFirebaseHot(){
        databaseReference.child("drinks").child("hot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String hot_name =snapshot.getKey();
                    String hot_desc=snapshot.child("description").getValue().toString();
                    String hot_price=snapshot.child("price").getValue().toString();
                    String hot_image=snapshot.child("image").getValue().toString();
                    hot_list.add(new DrinksCategories(hot_image,hot_name,hot_desc,hot_price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DrinksCategoriesAdapter adapter = new DrinksCategoriesAdapter(hot_list, new OnDrinksCategoryClickListener() {
            @Override
            public void onCategoryClick(DrinksCategories categories) {
                onCategoryClickDialoge(categories);
            }
        });


        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
    }
    void incomingFirebaseSoda(){

        databaseReference.child("drinks").child("soda").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String burger_name =snapshot.getKey();
                    String burger_desc=snapshot.child("description").getValue().toString();
                    String burger_price=snapshot.child("price").getValue().toString();
                    String burger_image=snapshot.child("image").getValue().toString();
                    juice_list.add(new DrinksCategories(burger_image,burger_name,burger_desc,burger_price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DrinksCategoriesAdapter adapter = new DrinksCategoriesAdapter(hot_list, new OnDrinksCategoryClickListener() {
            @Override
            public void onCategoryClick(DrinksCategories categories) {
                onCategoryClickDialoge(categories);
            }
        });

        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
    }
    void incomingFirebasejuice(){
        databaseReference.child("drinks").child("juice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String burger_name =snapshot.getKey();

                    String burger_desc=snapshot.child("description").getValue().toString();
                    String burger_price=snapshot.child("price").getValue().toString();
                    String burger_image=snapshot.child("image").getValue().toString();

                    soda_list.add(new DrinksCategories(burger_image,burger_name,burger_desc,burger_price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DrinksCategoriesAdapter adapter = new DrinksCategoriesAdapter(hot_list, new OnDrinksCategoryClickListener() {
            @Override
            public void onCategoryClick(DrinksCategories categories) {
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
    void onCategoryClickDialoge(final DrinksCategories categories){
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

        new MaterialAlertDialogBuilder(DrinksActivity.this)
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
                                                                                    Toast.makeText(DrinksActivity.this, "added to cart successfully", Toast.LENGTH_SHORT).show();
                                                                                }else{
                                                                                    Toast.makeText(DrinksActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    Log.v("error",task.getException().getMessage());}
                                                                            }
                                                                        });
                                                                    }else{
                                                                        Toast.makeText(DrinksActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        Log.v("error",task.getException().getMessage()); }
                                                                }
                                                            });
                                                        }else{
                                                            Toast.makeText(DrinksActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            Log.v("error",task.getException().getMessage());}
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(DrinksActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.v("error",task.getException().getMessage());}
                                        }
                                    });
                                }else{
                                    Toast.makeText(DrinksActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.v("error",task.getException().getMessage());
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DrinksActivity.this, "cancelled.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setView(view)

                .show();
    }
    private void first_load(){

        databaseReference.child("drinks").child("hot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String burger_name =snapshot.getKey();

                        String hot_desc=snapshot.child("description").getValue().toString();
                        String hot_price=snapshot.child("price").getValue().toString();
                        String hot_image=snapshot.child("image").getValue().toString();

                        hot_list.add(new DrinksCategories(hot_image,burger_name,hot_desc,hot_price));
                        Log.v("Log"," iam here");
                    }
//                loadingBurgerProgress.dismiss();
                    dont_add_from_hot_firebase_again_bitch =true;

                }
                DrinksCategoriesAdapter adapter = new DrinksCategoriesAdapter(hot_list, new OnDrinksCategoryClickListener() {
                    @Override
                    public void onCategoryClick(DrinksCategories categories) {
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
        new MaterialAlertDialogBuilder(DrinksActivity.this)
                .setTitle("Log out?")
                .setMessage("Are you sure you log out?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthUI.getInstance().signOut(DrinksActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            startActivity(new Intent(DrinksActivity.this, LoginActivity.class));
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
