package com.example.foodsetgo.Model;

public class Order {

    private String ItemID, Name, Price, Quantity, Discount;

    public Order(){
    }

    public Order(String itemID, String name,  String quantity, String price, String discount) {
        ItemID = itemID;
        Name = name;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
