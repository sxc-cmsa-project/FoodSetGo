package com.example.foodsetgo;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends BottomSheetDialogFragment {


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        NavigationView navigationView = view.findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case  R.id.about :
                        Toast.makeText(getActivity(), "About Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case  R.id.payment :
                        Toast.makeText(getActivity(), "Payment Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case  R.id.orders :
                        Toast.makeText(getActivity(), "Orders Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case  R.id.favourites :
                        Toast.makeText(getActivity(), "Favourites Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case  R.id.logout :
                        Paper.book().destroy();

                        Intent i = new Intent(getContext(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        break;
                }

                return false;
            }
        });



        return  view;
    }

}
