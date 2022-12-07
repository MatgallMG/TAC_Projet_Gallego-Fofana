package com.example.tac_projet_gallego_fofana.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;
import com.example.tac_projet_gallego_fofana.data.room.dao.FavMovieDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FavMovie.class}, version = 1)
public abstract class My_Database extends RoomDatabase {
    public abstract FavMovieDao favMovieDao();

    private static volatile My_Database INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static My_Database getDataBase(final Context context) {
        if (INSTANCE == null) {
            synchronized (My_Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    My_Database.class, "my_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}