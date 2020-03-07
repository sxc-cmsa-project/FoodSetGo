package com.example.foodsetgo.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.Interface.ItemClickListener;
import com.example.foodsetgo.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemName, itemPrice, itemType, itemRating, itemCount;
    public ImageView itemLogo;
    public Button itemAdd, itemRemove;

    private ItemClickListener itemClickListener;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        itemName = itemView.findViewById(R.id.item_name);
        itemPrice = itemView.findViewById(R.id.item_price);
        itemType = itemView.findViewById(R.id.item_type);
        itemLogo = itemView.findViewById(R.id.item_logo);
        itemRating = itemView.findViewById(R.id.item_rating);
        itemAdd = itemView.findViewById(R.id.addBtn);
        itemRemove = itemView.findViewById(R.id.removeBtn);
        itemCount = itemView.findViewById(R.id.item_count);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }



}
