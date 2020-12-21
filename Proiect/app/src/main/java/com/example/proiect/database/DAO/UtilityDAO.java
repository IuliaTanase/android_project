package com.example.proiect.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiect.database.models.Utility;

import java.util.List;

@Dao
public interface UtilityDAO {

    @Insert
    long insert(Utility utility);

    @Update
    int update(Utility utility);

    @Delete
    int delete(Utility utility);

    @Query("select * from utilities order by name")
    List<Utility> getAll();

    @Query("select * from utilities where provider = :providerName order by provider")
    List<Utility> getUtilitiesFromProvider(String providerName);


}
