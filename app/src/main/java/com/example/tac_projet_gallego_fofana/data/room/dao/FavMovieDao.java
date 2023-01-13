package com.example.tac_projet_gallego_fofana.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface FavMovieDao {

    @Insert
    void insert(FavMovie favMovie);

    /*@Delete
    void delete(FavMovie favMovie);*/

    @Query("DELETE FROM favmovie WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * from favmovie ORDER BY title ASC")
    Observable<List<FavMovie>> getAllFavMovies();
}
