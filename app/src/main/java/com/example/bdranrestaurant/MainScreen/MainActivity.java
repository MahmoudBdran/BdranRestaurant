package com.example.bdranrestaurant.MainScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bdranrestaurant.AdminPackage.AdminActivity;
import com.example.bdranrestaurant.CartPackage.CartActivity;
import com.example.bdranrestaurant.DessertsPackage.dessertsActivity;
import com.example.bdranrestaurant.DrinksPackage.DrinksActivity;
import com.example.bdranrestaurant.FoodPackage.Categories;
import com.example.bdranrestaurant.FoodPackage.FoodActivity;
import com.example.bdranrestaurant.Login.LoginActivity;
import com.example.bdranrestaurant.PendingPackage.PendingActivity;
import com.example.bdranrestaurant.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SliderView viewPager2;
    Animation fadein,fadeout,transition;
    ImageView img;
    TextView title_tv,body_tv;
    Bundle b = new Bundle();
    private FirebaseAuth mAuth;
    private FloatingActionButton fab,exit_fab,cart_fab,pending_fab;
    private Animation fab_from_low_to_up,fab_from_up_to_low;
    RelativeLayout main_Activity_root_layout;
    List<Categories> categories= new ArrayList<>();
    private FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private DatabaseReference burgerReference = firebaseDatabase.getReference("categories").child("food").child("burger");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab=findViewById(R.id.fab);
        exit_fab=findViewById(R.id.exit_fab);
        cart_fab=findViewById(R.id.cart_fab);
        pending_fab=findViewById(R.id.pending_fab);
        main_Activity_root_layout=findViewById(R.id.main_Activity_root_layout);
        exit_fab.setVisibility(View.INVISIBLE);
        cart_fab.setVisibility(View.INVISIBLE);
        pending_fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(main_Activity_root_layout);
                if ( exit_fab.getVisibility()== View.INVISIBLE){
//                    fab_from_up_to_low=AnimationUtils.loadAnimation(MainActivity.this,R.anim.fab_from_up_to_low);
//                    fab_from_low_to_up=AnimationUtils.loadAnimation(MainActivity.this,R.anim.fab_from_low_to_up);
//
//                    exit_fab.setAnimation(fab_from_low_to_up);
                    cart_fab.setVisibility(View.VISIBLE);
                    exit_fab.setVisibility(View.VISIBLE);
                    pending_fab.setVisibility(View.VISIBLE);
                }else{

//                    exit_fab.setAnimation(fab_from_up_to_low);
                    cart_fab.setVisibility(View.INVISIBLE);
                    exit_fab.setVisibility(View.INVISIBLE);
                    pending_fab.setVisibility(View.INVISIBLE);
                }
            }
        });

    exit_fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logOut();
        }
    });
    cart_fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        }
    });
    pending_fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, PendingActivity.class));
        }
    });
        viewPager2=findViewById(R.id.viewpager2);
        mAuth=FirebaseAuth.getInstance();
        List<Components> images =new ArrayList<>();
        images.add(new Components(R.drawable.i1,"FOOD","You can request any order any time with a best delivery in EGYPT. there are more gifts"));
        images.add(new Components(R.drawable.i2,"DRINKS","Drinks here are all kinds you will thinks about just order. "));
        images.add(new Components(R.drawable.i3,"DESSERTS","Yummy ,delicious sweets are here. start the journey right now."));
        viewPager2.setSliderAdapter(new ViewPagerAdapter(images, new OnSliderCLickListener() {
                    @Override
                    public void onSlideCLick(Components components, final ImageView imageView, final TextView title, final TextView body) {
                        exit_fab.setVisibility(View.INVISIBLE);
                        cart_fab.setVisibility(View.INVISIBLE);
                        pending_fab.setVisibility(View.INVISIBLE);
                        if (components.getTitle().equals("FOOD")){
                            fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                            fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                            transition=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.low_to_up);
                            imageView.startAnimation(fadeout);
                            title.startAnimation(fadeout);
                            body.startAnimation(fadeout);
                            img=imageView;
                            title_tv=title;
                            body_tv=body;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent =new Intent(MainActivity.this,FoodActivity.class);
                                    startActivity(intent);

                                }
                            },100);



                        }else if (components.getTitle().equals("DRINKS")) {
                            fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                            fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                            imageView.startAnimation(fadeout);
                            title.startAnimation(fadeout);
                            body.startAnimation(fadeout);

                            img=imageView;
                            title_tv=title;
                            body_tv=body;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    startActivity(new Intent(MainActivity.this, DrinksActivity.class));

                                }
                            },100);
                        }else if (components.getTitle().equals("DESSERTS")) {
                            fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                            fadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                            imageView.startAnimation(fadeout);
                            title.startAnimation(fadeout);
                            body.startAnimation(fadeout);

                            img=imageView;
                            title_tv=title;
                            body_tv=body;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    startActivity(new Intent(MainActivity.this, dessertsActivity.class));

                                }
                            },100);
                        }

                    }
                }));
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser ==null){
            getToStart();
        }else{
            if (firebaseUser.getEmail().equals("admin@admin.com")){
                getToAdminActivity();
            }
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
//        img.setVisibility(View.VISIBLE);
//        title_tv.setVisibility(View.VISIBLE);
//        body_tv.setVisibility(View.VISIBLE);
    }


    private void getToAdminActivity(){
        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }
    private void getToStart() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    void logOut(){
        new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Log out?")
                .setMessage("Are you sure you log out?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthUI.getInstance().signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
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
