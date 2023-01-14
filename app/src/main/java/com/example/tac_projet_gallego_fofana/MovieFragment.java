package com.example.tac_projet_gallego_fofana;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tac_projet_gallego_fofana.api.API_Client;
import com.example.tac_projet_gallego_fofana.api.API_Interface;
import com.example.tac_projet_gallego_fofana.api.Genre;
import com.example.tac_projet_gallego_fofana.api.GenreCatalog;
import com.example.tac_projet_gallego_fofana.api.Movie;
import com.example.tac_projet_gallego_fofana.api.MovieCatalog;
import com.example.tac_projet_gallego_fofana.recycler.CustomAdapterMovie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MovieFragment extends Fragment {

    public static final String TAB_NAME = "Movies";

    private final String TAG = "MATDAV";
    protected RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private CustomAdapterMovie customAdapter;
    private final Map<Integer, String> genre_dictionary = new HashMap<>();
    private MainActivityViewModel mainActivityViewModel;
    private List<Movie> movie_list;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // your operation....
                }
            });

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieFragment.
     */
    public static MovieFragment newInstance() {
        return new MovieFragment();
    }

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(view.getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        API_Interface apiService = API_Client.getClient().create(API_Interface.class);
        Call<GenreCatalog> callAllGenres = apiService.getAllGenres();
        callAllGenres.enqueue(new Callback<GenreCatalog>() {
            @Override
            public void onResponse(@NonNull Call<GenreCatalog> call, @NonNull Response<GenreCatalog> response) {
                Log.d(TAG, "on response : genres");
                assert response.body() != null;
                List<Genre> genre_list = response.body().getGenres();
                for (Genre genre : genre_list) {
                    Log.d(TAG, "in loop : genres " + genre.getId() + " : " + genre.getName());
                    genre_dictionary.put(genre.getId(), genre.getName());
                }
                displayListOfGenres(genre_list);
            }

            @Override
            public void onFailure(@NonNull Call<GenreCatalog> call, @NonNull Throwable t) {
                Log.d(TAG, "on failure");
            }
        });

        Call<MovieCatalog> callPopMovies = apiService.getPopularMovies();
        callPopMovies.enqueue(new Callback<MovieCatalog>() {
            @Override
            public void onResponse(@NonNull Call<MovieCatalog> call, @NonNull Response<MovieCatalog> response) {
                Log.d(TAG,"on response");
                assert response.body() != null;
                movie_list = response.body().getResults();
                customAdapter = new CustomAdapterMovie(view.getContext(), movie_list, mainActivityViewModel, genre_dictionary, startForResult, R.layout.item_layout);
                mainActivityViewModel.getAllFavMovies().observe(getViewLifecycleOwner(), favMovieList -> {
                    customAdapter.notifyDataSetChanged();
                });
                recyclerView.setAdapter(customAdapter);
                displayListOfMovies(movie_list);

            }
            @Override
            public void onFailure(@NonNull Call<MovieCatalog> call, @NonNull Throwable t) {
                Log.d(TAG,"on failure");
            }
        });
    }

    protected void displayLinear() {
        if (customAdapter.getItemLayoutType() == R.layout.item_layout) return;
        layoutManager.setSpanCount(1);
        customAdapter.setItemLayoutType(R.layout.item_layout);
        recyclerView.setAdapter(customAdapter);
    }

    protected void displayGrid() {
        if (customAdapter.getItemLayoutType() == R.layout.item_layout_grid) return;
        layoutManager.setSpanCount(2);
        customAdapter.setItemLayoutType(R.layout.item_layout_grid);
        recyclerView.setAdapter(customAdapter);
    }

    private void displayListOfMovies(List<Movie> movies){
        StringBuilder stringBuilder = new StringBuilder();
        for (Movie movie : movies){
            stringBuilder.append("-").append(movie.getTitle()).append("\n");
        }
        Log.d(TAG, "réponse = \n" + stringBuilder);
    }

    private void displayListOfGenres(List<Genre> genres){
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : genres){
            stringBuilder.append("-").append(genre.getId()).append(" : ").append(genre.getName()).append("\n");
        }
        Log.d(TAG, "réponse = \n" + stringBuilder);
    }
}