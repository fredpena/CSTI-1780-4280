package com.pucmm.csti.model;

import java.io.Serializable;
import java.util.Objects;

public class Userr implements Serializable {

    public enum ROL {SELLER, CUSTOMER}

    public int uid;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public ROL rol;
    public String contact;
    public String birthday;
    public String photo;

    public  Userr() {
    }

    public int getUid() {
        return uid;
    }

    public Userr setUid(int uid) {
        this.uid = uid;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Userr setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Userr setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Userr setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Userr setPassword(String password) {
        this.password = password;
        return this;
    }

    public ROL getRol() {
        return rol;
    }

    public Userr setRol(ROL rol) {
        this.rol = rol;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public Userr setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public Userr setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Userr setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Userr)) return false;
        Userr user = (Userr) o;
        return getUid() == user.getUid();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }

    @Override
    public String toString() {
        return "Userr{" +
                "uid=" + uid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rol=" + rol +
                ", contact='" + contact + '\'' +
                ", birthday='" + birthday + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
