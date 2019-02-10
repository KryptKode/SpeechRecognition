package com.text.speech.db.dao;

import com.text.speech.db.models.Setup;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface SetupDao extends BasicDAO<Setup> {
    @Query("SELECT * FROM Setup LIMIT 1")
    LiveData<Setup> getSetUp();

    @Override
    @Query("SELECT * FROM Setup")
    LiveData<List<Setup>> getAll();

    @Override
    @Query("SELECT * FROM Setup WHERE id=:id")
    LiveData<Setup> getItemById(int id);
}
