package com.example.proiect.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Apartment implements Parcelable {
    private int id;
    private String title;
    private int nrOfRooms;
    private double rentPerMonth;
    private String address;
    private String description;
    private boolean availability;
    private Date freeDate;
    private Tenant tenant;

    public Apartment(int id, String title, int nrOfRooms, double rentPerMonth, String address, String description, boolean availability, Date freeDate, Tenant tenant) {
        this.id = id;
        this.title = title;
        this.nrOfRooms = nrOfRooms;
        this.rentPerMonth = rentPerMonth;
        this.address = address;
        this.description = description;
        this.availability = availability;
        this.freeDate = freeDate;
        if(tenant == null) {
            this.tenant = new Tenant();
        } else {
            this.tenant = tenant;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNrOfRooms() {
        return nrOfRooms;
    }

    public void setNrOfRooms(int nrOfRooms) {
        this.nrOfRooms = nrOfRooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRentPerMonth() {
        return rentPerMonth;
    }

    public void setRentPerMonth(double rentPerMonth) {
        this.rentPerMonth = rentPerMonth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public Date getFreeDate() {
        return freeDate;
    }

    public void setFreeDate(Date freeDate) {
        this.freeDate = freeDate;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @NonNull
    @Override
    public String toString() {
        return "Apartment: " + this.title +
                "Rent per month: " + this.rentPerMonth +
                "Number of rooms: " + this.nrOfRooms +
                "Address: " + this.address +
                "Description: " + (description != null ? this.description : "-") +
                "Rented: " + this.availability +
                "Free date: " + new DateConverter().dateToString(freeDate) +
                "Tenant name: " + ((this.tenant.getFullName() != null) ? this.tenant.getFullName() : "-");
    }



    private Apartment(Parcel source) {
        id = source.readInt();
        title = source.readString();
        rentPerMonth = source.readDouble();
        nrOfRooms = source.readInt();
        address = source.readString();
        description = source.readString();
        availability = source.readBoolean();
        freeDate = new DateConverter().stringToDate(source.readString());
        String string = source.readString();
        tenant = new Tenant().stringToTenant(string);
    }

    public static final Creator<Apartment> CREATOR = new Creator<Apartment>() {
        @Override
        public Apartment createFromParcel(Parcel source) {
            return new Apartment(source);
        }

        @Override
        public Apartment[] newArray(int size) {
            return new Apartment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(rentPerMonth);
        dest.writeInt(nrOfRooms);
        dest.writeString(address);
        dest.writeString(description);
        dest.writeBoolean(availability);
        dest.writeString(new DateConverter().dateToString(freeDate));
        dest.writeString(new Tenant().tenantToString(tenant));
    }
}
