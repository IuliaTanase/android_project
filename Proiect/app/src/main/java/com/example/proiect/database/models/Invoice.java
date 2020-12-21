package com.example.proiect.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Update;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "invoices", foreignKeys = @ForeignKey(entity = Utility.class,parentColumns = "idUtility",childColumns = "idUtility"))
public class Invoice implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idInvoice")
    private long idInvoice;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "sum")
    private double sum;

    @ColumnInfo(name = "apartmentName")
    private String apartmentName;

    @ColumnInfo(name = "idUtility")
    private long idUtility;

    public Invoice(long idInvoice, Date date, double sum, String apartmentName, long idUtility) {
        this.idInvoice = idInvoice;
        this.date = date;
        this.sum = sum;
        this.apartmentName = apartmentName;
        this.idUtility = idUtility;
    }

    @Ignore
    public Invoice(Date date, double sum, String apartmentName, long idUtility) {
        this.date = date;
        this.sum = sum;
        this.apartmentName = apartmentName;
        this.idUtility = idUtility;
    }

    @Ignore
    public Invoice(Date date, double sum, String apartmentName) {
        this.date = date;
        this.sum = sum;
        this.apartmentName = apartmentName;
    }

    public long getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(long idInvoice) {
        this.idInvoice = idInvoice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public long getIdUtility() {
        return idUtility;
    }

    public void setIdUtility(long idUtility) {
        this.idUtility = idUtility;
    }
}
