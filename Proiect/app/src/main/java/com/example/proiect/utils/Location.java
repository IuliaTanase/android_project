package com.example.proiect.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    private int id;
    private String city;
    private String address;
    private Apartment apartment;

    public Location() {
        this.id = 0;
        this.city = "";
        this.address = "";
        this.apartment = new Apartment();
    }

    public Location(int id, String city, String address, Apartment apartment) {
        this.id = id;
        this.city = city;
        this.address = address;
        this.apartment = apartment;
    }

    private Location (Parcel source) {
        id = source.readInt();
        city = source.readString();
        address = source.readString();
        String ap = source.readString();
        apartment = new Apartment().stringToApartment(ap);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeString(new Apartment().apartmentToString(apartment));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) { return new Location(in); }

        @Override
        public Location[] newArray(int size) { return new Location[size]; }
    };

    @Override
    public int describeContents() { return 0; }


}
