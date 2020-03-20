package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.icu.util.ULocale;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.foodsetgo.Interface.ItemClickListener;
import com.example.foodsetgo.Model.Restaurant;
import com.example.foodsetgo.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference restaurant = database.getReference("Restaurant");
    RecyclerView recyclerMenu, recyclerPopular;

    FirebaseRecyclerAdapter<Restaurant, MenuViewHolder> adapter, searchAdapter;
    MaterialSearchBar searchView;
    List<String> suggestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FloatingActionButton fab = findViewById(R.id.fab);
        BottomAppBar bar = findViewById(R.id.bar);

        recyclerMenu = findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        loadMenu();

        recyclerPopular = findViewById(R.id.recycler_popualar);
        recyclerPopular.setHasFixedSize(true);
        recyclerPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        loadPopular();

        searchView = findViewById(R.id.searchView);
        loadSuggest();
        searchView.setLastSuggestions(suggestList);
        searchView.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for (String search:suggestList)
                {
                    if (search.toLowerCase().contains(searchView.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchView.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchView.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerMenu.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationDrawerFragment bottomNavigationFragment = new NavigationDrawerFragment();
                bottomNavigationFragment.show(getSupportFragmentManager(), "TAG");
            }
        });

        bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(HomeActivity.this, "Account Clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
    }

    private void loadSuggest() {
        restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Restaurant res = postSnapshot.getValue(Restaurant.class);
                    suggestList.add(res.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        FirebaseRecyclerOptions<Restaurant> options =
                new FirebaseRecyclerOptions.Builder<Restaurant>()
                        .setQuery(restaurant.orderByChild("name").equalTo(text.toString()), Restaurant.class)
                        .build();
        searchAdapter = new FirebaseRecyclerAdapter<Restaurant, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder holder, int position, @NonNull final Restaurant model) {

                holder.resName.setText(model.getName());
                Picasso.get().load(model.getImg()).into(holder.resLogo);
                holder.resPrice.setText(model.getPrice());
                holder.resType.setText(model.getType());
                holder.resRating.setText(model.getRating().toString());

                final Restaurant clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        String key = getRef(position).getKey();
                        Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
                        intent.putExtra("arg", key);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_cardview, parent, false);
                return new MenuViewHolder(view);
            }
        };
        searchAdapter.startListening();
        recyclerMenu.setAdapter(searchAdapter);
    }

    private void loadMenu() {
        FirebaseRecyclerOptions<Restaurant> options =
                new FirebaseRecyclerOptions.Builder<Restaurant>()
                        .setQuery(restaurant, Restaurant.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Restaurant, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder holder, int position, @NonNull final Restaurant model) {

                holder.resName.setText(model.getName());
                Picasso.get().load(model.getImg()).into(holder.resLogo);
                holder.resPrice.setText(model.getPrice());
                holder.resType.setText(model.getType());
                holder.resRating.setText(model.getRating().toString());

                final Restaurant clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        String key = getRef(position).getKey();
                        Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
                        intent.putExtra("arg", key);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_cardview, parent, false);
                return new MenuViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerMenu.setAdapter(adapter);
    }

    private void loadPopular() {
        FirebaseRecyclerOptions<Restaurant> options =
                new FirebaseRecyclerOptions.Builder<Restaurant>()
                        .setQuery(restaurant.limitToLast(6), Restaurant.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Restaurant, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder holder, int position, @NonNull final Restaurant model) {

                holder.resName.setText(model.getName());
                Picasso.get().load(model.getImg()).into(holder.resLogo);

                final Restaurant clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        String key = getRef(position).getKey();
                        Intent intent = new Intent(HomeActivity.this, RestaurantActivity.class);
                        intent.putExtra("arg", key);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.popular_restaurant_cardview, parent, false);
                return new MenuViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerPopular.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setupBottombar() {
        BottomAppBar bar = findViewById(R.id.bar);

        AppCompatActivity activity = (AppCompatActivity) getApplicationContext();
        if (activity != null) {
            activity.setSupportActionBar(bar);
        }

    }

}
