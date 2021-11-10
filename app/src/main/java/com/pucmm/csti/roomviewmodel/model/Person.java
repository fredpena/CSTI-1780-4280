package com.pucmm.csti.roomviewmodel.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "person")
public class Person {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "number")
    private String number;
    @ColumnInfo(name = "pinCode")
    private int pinCode;
    @ColumnInfo(name = "city")
    private String city;

    @Ignore
    public Person(String name, String email, String number, int pinCode, String city) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.pinCode = pinCode;
        this.city = city;
    }

    public Person(int id, String name, String email, String number, int pinCode, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.number = number;
        this.pinCode = pinCode;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
