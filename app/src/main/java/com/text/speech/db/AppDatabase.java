package com.text.speech.db;



import com.text.speech.db.converters.Converters;
import com.text.speech.db.dao.SetupDao;
import com.text.speech.db.models.Setup;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Created by Cyberman on 4/2/2018.
 */
@TypeConverters({Converters.class})
@Database(entities = Setup.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "speech.db";

    public abstract SetupDao setupDao();

}
