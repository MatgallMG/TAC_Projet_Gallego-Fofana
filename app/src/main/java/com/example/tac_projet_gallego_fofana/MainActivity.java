package com.example.tac_projet_gallego_fofana;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tac_projet_gallego_fofana.api.API_Client;
import com.example.tac_projet_gallego_fofana.api.API_Interface;
import com.example.tac_projet_gallego_fofana.api.Genre;
import com.example.tac_projet_gallego_fofana.api.GenreCatalog;
import com.example.tac_projet_gallego_fofana.api.Movie;
import com.example.tac_projet_gallego_fofana.api.MovieCatalog;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;
import com.example.tac_projet_gallego_fofana.recycler.CustomAdapter;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MATDAV";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CustomAdapter customAdapter;
    private Map<Integer, String> genre_dictionary = new HashMap<>();
    private MainActivityViewModel mainActivityViewModel;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // your operation....
                }
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "on create main", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);


        API_Interface apiService = API_Client.getClient().create(API_Interface.class);
        Call<GenreCatalog> callAllGenres = apiService.getAllGenres();
        callAllGenres.enqueue(new Callback<GenreCatalog>() {
            @Override
            public void onResponse(Call<GenreCatalog> call, Response<GenreCatalog> response) {
                Log.d(TAG,"on response : genres");
                List<Genre> genre_list = response.body().getGenres();
                for (Genre genre : genre_list) {
                    Log.d(TAG,"in loop : genres "+genre.getId()+" : "+genre.getName());
                    genre_dictionary.put(genre.getId(), genre.getName());
                }
                displayListOfGenres(genre_list);
            }
            @Override
            public void onFailure(Call<GenreCatalog> call, Throwable t) {
                Log.d(TAG,"on failure");
            }
        });

        Call<MovieCatalog> callPopMovies = apiService.getPopularMovies();
        callPopMovies.enqueue(new Callback<MovieCatalog>() {
            @Override
            public void onResponse(Call<MovieCatalog> call, Response<MovieCatalog> response) {
                Log.d(TAG,"on response");
                List<Movie> movie_list = response.body().getResults();
                customAdapter = new CustomAdapter(getApplicationContext(), movie_list, mainActivityViewModel, genre_dictionary, startForResult);
                recyclerView.setAdapter(customAdapter);
                displayListOfMovies(movie_list);

            }
            @Override
            public void onFailure(Call<MovieCatalog> call, Throwable t) {
                Log.d(TAG,"on failure");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "on resume main", Toast.LENGTH_SHORT).show();
    }

    private void displayListOfMovies(List<Movie> movies){
        StringBuilder stringBuilder = new StringBuilder();
        for (Movie movie : movies){
            stringBuilder.append("-"+movie.getTitle()+"\n");
        }
        Log.d(TAG, "réponse = \n" + stringBuilder.toString());
    }

    private void displayListOfGenres(List<Genre> genres){
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : genres){
            stringBuilder.append("-"+genre.getId()+" : "+genre.getName()+"\n");
        }
        Log.d(TAG, "réponse = \n" + stringBuilder.toString());
    }

}