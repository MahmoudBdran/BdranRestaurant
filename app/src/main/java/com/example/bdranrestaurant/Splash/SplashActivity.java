package com.example.bdranrestaurant.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.example.bdranrestaurant.MainScreen.MainActivity;
import com.example.bdranrestaurant.R;

public class SplashActivity extends AppCompatActivity {
MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent= new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },8000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
        player=null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (player==null){
            player=MediaPlayer.create(SplashActivity.this,R.raw.kolmanga);
            player.start();
        }
    }
}
