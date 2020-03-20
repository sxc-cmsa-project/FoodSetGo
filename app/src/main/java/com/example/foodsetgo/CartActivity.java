package com.example.foodsetgo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsetgo.Commom.Common;
import com.example.foodsetgo.Database.Database;
import com.example.foodsetgo.Helper.RecyclerItemTouchHelper;
import com.example.foodsetgo.Interface.RecyclerItemTouchHelperListener;
import com.example.foodsetgo.Model.CardAdapter;
import com.example.foodsetgo.Model.Order;
import com.example.foodsetgo.Model.Request;
import com.example.foodsetgo.ViewHolder.CartViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView totalPrice, cartItemCount, emptyCartMssg
            , itemTotal, deliveryCharge, totalDiscount
            , totalDiscountText, totalTaxes;
    Button placeOrder, clearCart, applyCoupon;

    RelativeLayout rootLayout;

    List<Order> cart = new ArrayList<>();
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Request");

        recyclerView = findViewById(R.id.recycler_list_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        placeOrder = findViewById(R.id.place_order_bttn);
        clearCart = findViewById(R.id.clear_cart_bttn);
        applyCoupon = findViewById(R.id.apply_coupon_bttn);

        totalPrice = findViewById(R.id.total_price);
        cartItemCount = findViewById(R.id.cart_item_count);
        emptyCartMssg = findViewById(R.id.empty_cart_mssg);
        itemTotal = findViewById(R.id.item_total);
        deliveryCharge = findViewById(R.id.item_delivery);
        totalDiscount = findViewById(R.id.item_discount);
        totalDiscountText = findViewById(R.id.item_discount_text);
        totalTaxes = findViewById(R.id.item_taxes);

        rootLayout =  findViewById(R.id.cart_activity_layout);

        //Swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).cleanCart();
                loadItemList();
                emptyCartMssg.setVisibility(View.VISIBLE);
                totalDiscountText.setTextColor(getResources().getColor(R.color.goodBlack));
                totalDiscount.setTextColor(Color.parseColor("#808080"));
            }
        });
        loadItemList();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your address : ");

        final EditText address = new EditText(CartActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        address.setLayoutParams(lp);
        alertDialog.setView(address);
        alertDialog.setIcon(R.drawable.ic_shopping_cart);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String date_time = currentTime + "  " + currentDate;
                Paper.init(getBaseContext());
                String phone = Paper.book().read(Common.USER_KEY);
                String status = "0";
                Request request = new Request(
                        phone,
                        Common.currentUser.getName(),
                        address.getText().toString(),
                        status,
                        date_time.toString(),
                        totalPrice.getText().toString(),
                        cart);

                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(CartActivity.this, "Thank you. Order Placed.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void loadItemList(){
        emptyCartMssg.setVisibility(View.INVISIBLE);
        cart = new Database(this).getCart();
        adapter = new CardAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        int total = 0;  double discount=0.0;
        for (Order order:cart)
        {
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            discount += (Integer.parseInt(order.getDiscount()))
                    *(Integer.parseInt(order.getPrice()))
                    *(Integer.parseInt(order.getQuantity()))
                    *0.01;
        }

        Locale locale = new Locale("en", "IN");
        final NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        itemTotal.setText(fmt.format(total));
        final double taxes = total * 0.05;
        totalTaxes.setText(fmt.format(taxes));
        double del = 0.00;
        if (!cart.isEmpty()) {
            del = 25.00;
            deliveryCharge.setText(fmt.format(del));
        }
        if ((totalDiscount.getText().toString()).equals(fmt.format(0)))
            totalDiscount.setText(fmt.format(0));
        totalPrice.setText(fmt.format(total+taxes+del));

        if (cart.isEmpty())
        {
            del = 0.00;
            deliveryCharge.setText(fmt.format(del));
            emptyCartMssg.setVisibility(View.VISIBLE);
            totalDiscount.setText(fmt.format(0));
        }
        /*else
        {
            totalDiscount.setText("-"+fmt.format(discount));
            totalDiscount.setTextColor(getResources().getColor(R.color.goodGreen));
            totalDiscountText.setTextColor(getResources().getColor(R.color.goodGreen));
            totalPrice.setText(fmt.format(total +taxes+ del - discount));
        }*/

        final double finalDiscount = discount;
        final int finalTotal = total;
        final double finalDel = del;
        applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cart.isEmpty())
                {
                    totalDiscount.setText("-"+fmt.format(finalDiscount));
                    totalDiscount.setTextColor(getResources().getColor(R.color.goodGreen));
                    totalDiscountText.setTextColor(getResources().getColor(R.color.goodGreen));
                    totalPrice.setText(fmt.format(finalTotal +taxes+ finalDel - finalDiscount));
                }
            }
        });
        applyCoupon.performClick();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder)
        {
            String name = ((CardAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getName();
            final Order deleteItem = ((CardAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).deleteRow(name);
            loadItemList();
            /*int total = 0;  double discount=0.0;
            for (Order order:cart)
            {
                total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
                discount += (Integer.parseInt(order.getDiscount()))
                        *(Integer.parseInt(order.getPrice()))
                        *(Integer.parseInt(order.getQuantity()))
                        *0.01;
            }

            Locale locale = new Locale("en", "IN");
            final NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            itemTotal.setText(fmt.format(total));*/

            //Make snackbar
            Snackbar snackbar = Snackbar.make(rootLayout, name + " removed from cart.", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);
                    loadItemList();
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
