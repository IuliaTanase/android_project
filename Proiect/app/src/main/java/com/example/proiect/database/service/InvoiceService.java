package com.example.proiect.database.service;

import android.content.Context;
import android.telecom.Call;

import com.example.proiect.asyncTask.AsyncTaskRunner;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.DAO.InvoiceDAO;
import com.example.proiect.database.DBManager;
import com.example.proiect.database.models.Invoice;
import com.example.proiect.database.models.UtilityAndInvoice;

import java.util.List;
import java.util.concurrent.Callable;

public class InvoiceService {

    private final InvoiceDAO invoiceDAO;
    private final AsyncTaskRunner asyncTaskRunner;

    public InvoiceService(Context ctx){
        invoiceDAO = DBManager.getInstance(ctx).getInvoiceDAO();
        asyncTaskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<UtilityAndInvoice>> callback){

        Callable<List<UtilityAndInvoice>> callable = new Callable<List<UtilityAndInvoice>>() {
            @Override
            public List<UtilityAndInvoice> call() throws Exception {
                return invoiceDAO.getAll();
            }
        };
        asyncTaskRunner.executeAsync(callable,callback);

    }

    public void getInvoicesWithMaxSum(Callback<List<UtilityAndInvoice>> callback,final double price){

        Callable<List<UtilityAndInvoice>> callable = new Callable<List<UtilityAndInvoice>>() {
            @Override
            public List<UtilityAndInvoice> call() throws Exception {
                return invoiceDAO.getInvoicesWithMaxSum(price);
            }
        };

        asyncTaskRunner.executeAsync(callable,callback);
    }

    public void getInvoicesFromUtility(Callback<List<UtilityAndInvoice>> callback,final String utilityName){
        Callable<List<UtilityAndInvoice>> callable = new Callable<List<UtilityAndInvoice>>() {
            @Override
            public List<UtilityAndInvoice> call() throws Exception {
                return invoiceDAO.getInvoicesFromUtility(utilityName);
            }
        };

        asyncTaskRunner.executeAsync(callable,callback);
    }

    public void getInvoicesWithSumAndName(Callback<List<UtilityAndInvoice>>callback, final String utilityName, final double price ){
        Callable<List<UtilityAndInvoice>> callable = new Callable<List<UtilityAndInvoice>>() {
            @Override
            public List<UtilityAndInvoice> call() throws Exception {
                return invoiceDAO.getInvoicesWithSumAndName(utilityName,price);
            }
        };
        asyncTaskRunner.executeAsync(callable,callback);
    }

    public void insert(Callback<Invoice> callback,final Invoice invoice){
        Callable<Invoice> callable = new Callable<Invoice>() {
            @Override
            public Invoice call() throws Exception {
                if(invoice == null)
                    return null;
                long idInvoice = invoiceDAO.insert(invoice);
                if(idInvoice == -1)
                    return null;
                invoice.setIdInvoice(idInvoice);
                return invoice;
            }
        };

        asyncTaskRunner.executeAsync(callable,callback);
    }

    public void update(Callback<Invoice> callback,final Invoice invoice){

        Callable<Invoice> callable = new Callable<Invoice>() {
            @Override
            public Invoice call() throws Exception {
                if(invoice == null)
                    return null;
                int updated = invoiceDAO.update(invoice);
                if(updated < 1)
                    return null;
                return invoice;
            }
        };
        asyncTaskRunner.executeAsync(callable,callback);
    }

    public void delete(Callback<Integer> callback,final Invoice invoice){

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if(invoice == null)
                    return -1;
                return invoiceDAO.delete(invoice);
            }
        };
        asyncTaskRunner.executeAsync(callable,callback);
    }
}
