package com.text.speech.db.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Setup {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private boolean setup;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSetup() {
        return setup;
    }

    public void setSetup(boolean setup) {
        this.setup = setup;
    }
}
