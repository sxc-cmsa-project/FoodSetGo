package com.example.foodsetgo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.material.bottomappbar.BottomAppBar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setupBottombar() {
        BottomAppBar bar = (BottomAppBar) findViewById(R.id.bar);
        AppCompatActivity activity = (AppCompatActivity) getApplicationContext();
        if (activity != null) {
            activity.setSupportActionBar(bar);
        }
    }
}
