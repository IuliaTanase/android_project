package com.example.proiect.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.proiect.database.models.Invoice;
import com.example.proiect.database.models.UtilityAndInvoice;

import java.util.List;

@Dao
public interface InvoiceDAO {

    @Insert
    long insert(Invoice invoice);

    @Update
    int update(Invoice invoice);

    @Delete
    int delete(Invoice invoice);

    @Transaction
    @Query("select distinct * from invoices join utilities on invoices.idUtility=utilities.idUtility")
    List<UtilityAndInvoice> getAll();

    @Transaction
    @Query("select distinct * from invoices join utilities on invoices.idUtility=utilities.idUtility where sum < :price order by sum")
    List<UtilityAndInvoice> getInvoicesWithMaxSum(double price);

    @Transaction
    @Query("select distinct * from invoices join utilities on invoices.idUtility=utilities.idUtility where name= :utilityName order by name")
    List<UtilityAndInvoice> getInvoicesFromUtility(String utilityName);

    @Query("select distinct * from invoices join utilities on invoices.idUtility=utilities.idUtility where name= :utilityName and sum < :price")
    List<UtilityAndInvoice> getInvoicesWithSumAndName(String utilityName, double price);
}
