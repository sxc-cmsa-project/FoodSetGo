package com.example.foodsetgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.VideoView;

public class SplashActivity extends AppCompatActivity {

    VideoView video1, video2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        video1 = findViewById(R.id.logo_animation);
        String videopath = "android.resource://com.example.foodsetgo/"+R.raw.untitled_design;
        Uri uri = Uri.parse(videopath);
        video1.setVideoURI(uri);
        video1.setZOrderOnTop(true);
        video1.start();

        video2 = findViewById(R.id.text_animation);
        String videopath1 = "android.resource://com.example.foodsetgo/"+R.raw.fsg_logo_animation;
        Uri uri2 = Uri.parse(videopath1);
        video2.setVideoURI(uri2);
        video2.setZOrderOnTop(true);
        video2.start();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },2900);



    }

}
