package com.example.tac_projet_gallego_fofana.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenreCatalog {

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
