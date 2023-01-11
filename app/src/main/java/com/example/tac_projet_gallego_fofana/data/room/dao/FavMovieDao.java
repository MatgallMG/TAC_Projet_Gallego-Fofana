package com.example.tac_projet_gallego_fofana.data.room.dao;

import io.reactivex.Observable;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;

@Dao
public interface FavMovieDao {

    @Insert
    long insert(FavMovie favMovie);

    /*@Delete
    void delete(FavMovie favMovie);*/

    @Query("DELETE FROM favmovie WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * from favmovie ORDER BY title ASC")
    public abstract Observable<List<FavMovie>> getAllFavMovies();
}
