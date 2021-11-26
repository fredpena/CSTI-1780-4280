package com.pucmm.csti.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "category")
public class Category implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int uid;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "active")
    private boolean active;
    @ColumnInfo(name = "photo")
    private String photo;

    public Category() {
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Category{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", photo='" + photo + '\'' +
                '}';
    }
}
