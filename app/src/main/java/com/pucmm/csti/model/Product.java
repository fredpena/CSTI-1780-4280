package com.pucmm.csti.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product")
public class Product {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "itemCode")
    private String itemCode;
    @ColumnInfo(name = "itemName")
    private String itemName;
    @ColumnInfo(name = "category")
    private int category;
    @ColumnInfo(name = "price")
    private double price;
    @ColumnInfo(name = "active")
    private boolean active;

    public Product() {
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Product{" +
                "itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", active=" + active +
                '}';
    }
}
