package com.pucmm.csti.model;

import androidx.room.*;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "carousel")
public class Carousel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int uid;
    @ColumnInfo(name = "product")
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
