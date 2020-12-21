package com.example.proiect.database.service;

import android.content.Context;

import com.example.proiect.asyncTask.AsyncTaskRunner;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.DAO.UtilityDAO;
import com.example.proiect.database.DBManager;
import com.example.proiect.database.models.Invoice;
import com.example.proiect.database.models.Utility;

import java.util.List;
import java.util.concurrent.Callable;

public class UtilityService {

    public final UtilityDAO utilityDAO;
    public final AsyncTaskRunner asyncTaskRunner;

    public UtilityService(Context ctx){
        utilityDAO = DBManager.getInstance(ctx).getUtilityDAO();
        asyncTaskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<Utility>> callback){

        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return utilityDAO.getAll();
            }
        };
        asyncTaskRunner.executeAsync(callable,callback);
    }

    public void getAllFromProvider(Callback<List<Utility>> callback, final String providerName){
        Callable<List<Utility>> callable = new Callable<List<Utility>>() {
            @Override
            public List<Utility> call() throws Exception {
                return utilityDAO.getUtilitiesFromProvider(providerName);
            }
        };
        asyncTaskRunner.executeAsync(callable,callback);
    }


    public void insert(Callback<Utility> callback, final Utility utility){
        Callable<Utility> callable = new Callable<Utility>() {
            @Override
            public Utility call() throws Exception {
                if(utility == null)
                    return null;
                long idUtility = utilityDAO.insert(utility);
                if(idUtility == -1)
                    return null;
                utility.setIdUtility(idUtility);
                return utility;
            }
        };
        asyncTaskRunner.executeAsync(callable,callback);
    }

    public void update(Callback<Utility> callback,Utility utility){
        Callable<Utility> callable = new Callable<Utility>() {
            @Override
            public Utility call() throws Exception {
                if(utility == null)
                    return null;
                int updated = utilityDAO.update(utility);
                if(updated < 1)
                    return null;
                return utility;
            }
        };
        asyncTaskRunner.executeAsync(callable,callback);
    }

    public void delete(Callback<Integer> callback,Utility utility)
    {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if(utility == null)
                    return -1;
                return utilityDAO.delete(utility);
            }
        };
        asyncTaskRunner.executeAsync(callable,callback);
    }
}
