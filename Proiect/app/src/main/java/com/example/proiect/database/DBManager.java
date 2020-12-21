package com.example.proiect.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.proiect.database.DAO.InvoiceDAO;
import com.example.proiect.database.DAO.UtilityDAO;
import com.example.proiect.database.models.Invoice;
import com.example.proiect.database.models.Utility;
import com.example.proiect.utils.DateConverter;

@Database(entities = {Invoice.class, Utility.class},exportSchema = false,version =1)
@TypeConverters(DateConverter.class)
public abstract class DBManager extends RoomDatabase {

    public static final String DB_RENT_MANAGER ="db_rent_manager";
    public static DBManager dbManager;

    public static DBManager getInstance(Context ctx){
        if(dbManager==null){
            synchronized (DBManager.class){
                if(dbManager==null){
                    dbManager = Room.databaseBuilder(ctx,DBManager.class,DB_RENT_MANAGER)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return dbManager;
    }

    public abstract InvoiceDAO getInvoiceDAO();
    public abstract UtilityDAO getUtilityDAO();


}
