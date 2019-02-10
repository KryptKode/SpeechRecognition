package com.text.speech.db.dao;



import java.util.Collection;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;


@Dao
public interface BasicDAO<Entity> {

    LiveData<List<Entity>> getAll();
    LiveData<Entity> getItemById(int id);






    /* Inserts */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(final Entity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(final Entity... entities);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(final Collection<Entity> entities);

    /* Deletes */
    @Delete
    int delete(final Entity entity);

    @Delete
    int delete(final Collection<Entity> entities);

    /*@Delete
    int deleteAll();*/

    /* Updates */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(final Entity entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(final Collection<Entity> entities);
}
