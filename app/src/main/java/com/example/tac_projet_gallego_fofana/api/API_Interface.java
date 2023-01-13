package com.example.tac_projet_gallego_fofana.api;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_Interface {
    @GET("movie/{movieId}?api_key=a41875d908caf1dc6ee321953d5209ac&language=en-US")
    Call<MovieDetails> getDetails(@Path("movieId") int movieId);

    @GET("movie/popular?api_key=a41875d908caf1dc6ee321953d5209ac&language=en-US&page=1")
    Call<MovieCatalog> getPopularMovies();

    @GET("genre/movie/list?api_key=a41875d908caf1dc6ee321953d5209ac&language=en-US")
    Call<GenreCatalog> getAllGenres();

    //@GET("movie/popular?api_key=a41875d908caf1dc6ee321953d5209ac&language=en-US&page=1/result")
}
