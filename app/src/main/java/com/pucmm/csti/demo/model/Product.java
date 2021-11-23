package com.pucmm.csti.demo.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "product")
public class Product {

    @PrimaryKey()
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

}
