package com.example.foodsetgo.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.Interface.ItemClickListener;
import com.example.foodsetgo.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemName, itemPrice, totalPrice, itemCount;
    //public Button addBttn, removeBttn;
    public RelativeLayout foreground, background;

    private ItemClickListener itemClickListener;

    public void set_itemName(TextView itemName){
        this.itemName = itemName;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.cart_item_name);
        itemPrice = itemView.findViewById(R.id.cart_item_price);
        itemCount = itemView.findViewById(R.id.cart_item_count);
        totalPrice = itemView.findViewById(R.id.cart_total_price);
        foreground = itemView.findViewById(R.id.foreground_view);
        background = itemView.findViewById(R.id.backgroud_view);
        /*addBttn =   itemView.findViewById(R.id.cart_addBtn);
        removeBttn = itemView.findViewById(R.id.cart_removeBtn);*/
    }

    @Override
    public void onClick(View v) {

    }
}
