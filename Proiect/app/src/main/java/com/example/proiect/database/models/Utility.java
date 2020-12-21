package com.example.proiect.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "utilities")
public class Utility implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idUtility")
    private long idUtility;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "provider")
    private String provider;

    public Utility(long idUtility, String name, String provider) {
        this.idUtility = idUtility;
        this.name = name;
        this.provider = provider;
    }

    @Ignore
    public Utility(String name, String provider) {
        this.name = name;
        this.provider = provider;
    }

    public long getIdUtility() {
        return idUtility;
    }

    public void setIdUtility(long idUtility) {
        this.idUtility = idUtility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
