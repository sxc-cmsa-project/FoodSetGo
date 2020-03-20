package com.example.foodsetgo.Model;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.foodsetgo.CartActivity;
import com.example.foodsetgo.Database.Database;
import com.example.foodsetgo.Interface.ItemClickListener;
import com.example.foodsetgo.R;
import com.example.foodsetgo.RestaurantActivity;
import com.example.foodsetgo.ViewHolder.CartViewHolder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CardAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CardAdapter(List<Order> listData, Context context){
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_cardview, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        holder.itemName.setText(listData.get(position).getName());
        holder.itemPrice.setText("₹"+listData.get(position).getPrice());
        Locale locale = new Locale("en", "IN");
        final NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        final int price = (Integer.parseInt(listData.get(position).getPrice())*(Integer.parseInt(listData.get(position).getQuantity())));
        final int pri = Integer.parseInt(listData.get(position).getPrice());
        holder.totalPrice.setText(fmt.format(price));
        holder.itemCount.setText(listData.get(position).getQuantity());

                    /*holder.addBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(holder.itemCount.getText().toString());
                String c = ""+(count+1);

                String id  = listData.get(position).getItemID();
                new Database(context.getApplicationContext()).deleteRow(listData.get(position).getName());
                new Database(context.getApplicationContext()).addToCart(new Order(
                        id,
                        listData.get(position).getName(),
                        c,
                        listData.get(position).getPrice(),
                        listData.get(position).getDiscount()
                ));
                String s = listData.get(position).getName();

                holder.itemCount.setText(c);
                int p = pri*(Integer.valueOf(c));
                holder.totalPrice.setText(fmt.format(p));
                holder.itemCount.setVisibility(View.VISIBLE);
                holder.itemCount.setText(c);

            }
        });
        holder.removeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(holder.itemCount.getText().toString());

                if(count>0)
                {
                    count--;
                    String c = ""+count;
                    holder.itemCount.setText(c);
                    int p = pri*(Integer.valueOf(c));
                    holder.totalPrice.setText(fmt.format(p));
                    if(count==0)
                        holder.itemCount.setVisibility(View.INVISIBLE);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int position){
        return listData.get(position);
    }

    public void removeItem(int position){
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item, int position){
        listData.add(position, item);
        notifyItemInserted(position);
    }
}
