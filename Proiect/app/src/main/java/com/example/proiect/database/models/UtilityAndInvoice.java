package com.example.proiect.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UtilityAndInvoice {

    @Embedded
    public Utility utility;

    @Relation(
            parentColumn = "idUtility",
            entityColumn = "idUtility"
    )
    public List<Invoice> invoices;
}
