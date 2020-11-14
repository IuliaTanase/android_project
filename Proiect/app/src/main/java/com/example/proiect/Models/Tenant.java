package com.example.proiect.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Tenant implements Parcelable {
    private int id;
    private String fullName;
    private String phone;

    public Tenant() {
        this.id = 0;
        this.fullName = "-";
        this.phone = "-";
    }

    public Tenant(String fullName) {
        this.id = 0;
        this.fullName = fullName;
        this.phone = "-";
    }

    public Tenant(int id, String fullName, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Tenant(Parcel source) {
        id = source.readInt();
        fullName = source.readString();
        phone = source.readString();
    }

    public Tenant stringToTenant(String tenantString) {
        String[] splitted = tenantString.split(",");
        return new Tenant(Integer.parseInt(splitted[0]), splitted[1], splitted[2]);
    }

    public String tenantToString(Tenant tenant) {
        return tenant.id + "," + tenant.fullName + "," + tenant.phone;
    }

    public static final Creator<Tenant> CREATOR = new Creator<Tenant>() {
        @Override
        public Tenant createFromParcel(Parcel source) {
            return new Tenant(source);
        }

        @Override
        public Tenant[] newArray(int size) {
            return new Tenant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fullName);
        dest.writeString(phone);
    }
}
