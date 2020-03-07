package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsetgo.Interface.ItemClickListener;
import com.example.foodsetgo.Model.Item;
import com.example.foodsetgo.Model.Restaurant;
import com.example.foodsetgo.ViewHolder.ItemViewHolder;
import com.example.foodsetgo.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RestaurantActivity extends AppCompatActivity {

    //Firebase
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference resTable = database.getReference("Restaurant");
    final DatabaseReference itemTable = database.getReference("Items");

    RecyclerView recyclerItemList;

    String passedArg;
    public TextView resName, resPrice, resType, resRating;
    public ImageView resLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        resName = findViewById(R.id.res_name);
        resPrice = findViewById(R.id.res_price);
        resType = findViewById(R.id.res_type);
        resLogo = findViewById(R.id.res_logo);
        resRating = findViewById(R.id.res_rating);

        recyclerItemList = findViewById(R.id.recycler_item_list);
        recyclerItemList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerItemList.setLayoutManager(layoutManager);

        if(getIntent()!=null)
            passedArg = getIntent().getStringExtra("arg");
        if(!passedArg.isEmpty() && passedArg!=null)
            loadItemList(passedArg);

        resTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Restaurant restaurant = dataSnapshot.child(passedArg).getValue(Restaurant.class);
                resName.setText(restaurant.getName());
                resPrice.setText(restaurant.getPrice());
                resType.setText(restaurant.getType());
                Picasso.get().load(restaurant.getImg()).into(resLogo);
                resRating.setText(restaurant.getRating().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadItemList(String passedArg) {
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(itemTable.orderByChild("resid").equalTo(passedArg), Item.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder1, int position, @NonNull final Item model) {

                holder1.itemName.setText(model.getName());
                holder1.itemPrice.setText("₹"+model.getPrice());
                holder1.itemType.setText(model.getCourse());
                holder1.itemRating.setText(model.getRating());
                if(model.getType().equals("nveg"))
                    holder1.itemLogo.setImageResource(R.drawable.icon_non_veg);

                final Item clickItem = model;
                holder1.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(RestaurantActivity.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

                holder1.itemAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RestaurantActivity.this, "Added "+clickItem.getName(), Toast.LENGTH_SHORT).show();
                        int count = Integer.valueOf(holder1.itemCount.getText().toString());
                        String c = ""+(count+1);
                        holder1.itemCount.setVisibility(View.VISIBLE);
                        holder1.itemCount.setText(c);
                    }
                });

                holder1.itemRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = Integer.valueOf(holder1.itemCount.getText().toString());
                        if(count>0)
                        {
                            Toast.makeText(RestaurantActivity.this, "Removed "+clickItem.getName(), Toast.LENGTH_SHORT).show();
                            String c = ""+(count-1);
                            if(c.equals("0"))
                                holder1.itemCount.setVisibility(View.INVISIBLE);
                            holder1.itemCount.setText(c);
                        }
                    }
                });

            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_cardview, parent, false);
                return new ItemViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerItemList.setAdapter(adapter);
    }
}

