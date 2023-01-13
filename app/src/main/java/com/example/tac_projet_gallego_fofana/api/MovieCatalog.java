package com.example.tac_projet_gallego_fofana.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieCatalog {
    @SerializedName("results")
    @Expose
    private final List<Movie> results = null;

    public List<Movie> getResults() {
        return results;
    }


}
