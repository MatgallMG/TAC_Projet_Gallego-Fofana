package com.example.tac_projet_gallego_fofana.data;

import android.app.Application;

import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;
import com.example.tac_projet_gallego_fofana.data.room.My_Database;
import com.example.tac_projet_gallego_fofana.data.room.dao.FavMovieDao;

import java.util.List;

import io.reactivex.Observable;

public class My_Repository {

    private final FavMovieDao favMovieDao;

    public My_Repository(Application application) {
        My_Database db = My_Database.getDataBase(application);

        favMovieDao = db.favMovieDao();
    }

    public void insert(FavMovie favMovie) {
        My_Database.databaseWriteExecutor.execute(() -> favMovieDao.insert(favMovie));
    }

    public void deleteById(Integer id) {
        My_Database.databaseWriteExecutor.execute(() -> favMovieDao.deleteById(id));
    }

    public Observable<List<FavMovie>> getAllFavMovies() {
        return favMovieDao.getAllFavMovies();
    }

}