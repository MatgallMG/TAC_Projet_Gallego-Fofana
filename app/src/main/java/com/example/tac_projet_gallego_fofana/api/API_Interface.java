package com.example.tac_projet_gallego_fofana.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API_Interface {
    @GET("movie/popular?api_key=a41875d908caf1dc6ee321953d5209ac&language=en-US&page=1")
    Call<MovieCatalog> getPopularMovies();

    @GET("genre/movie/list?api_key=a41875d908caf1dc6ee321953d5209ac&language=en-US")
    Call<GenreCatalog> getAllGenres();

    //@GET("movie/popular?api_key=a41875d908caf1dc6ee321953d5209ac&language=en-US&page=1/result")
}
