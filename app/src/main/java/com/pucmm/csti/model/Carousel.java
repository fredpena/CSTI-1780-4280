package com.pucmm.csti.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "carousel")
public class Carousel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int uid;
    @ColumnInfo(name = "Product")
    private int product;
    @ColumnInfo(name = "lineNum")
    private int lineNum;
    @ColumnInfo(name = "photo")
    private String photo;

    @Ignore
    public Carousel(int product, int lineNum, String photo) {
        this.product = product;
        this.lineNum = lineNum;
        this.photo = photo;
    }

    public Carousel(int uid, int product, int lineNum, String photo) {
        this.uid = uid;
        this.product = product;
        this.lineNum = lineNum;
        this.photo = photo;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
