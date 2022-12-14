package com.example.tac_projet_gallego_fofana;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tac_projet_gallego_fofana.api.API_Client;
import com.example.tac_projet_gallego_fofana.api.API_Interface;
import com.example.tac_projet_gallego_fofana.api.Genre;
import com.example.tac_projet_gallego_fofana.api.GenreCatalog;
import com.example.tac_projet_gallego_fofana.api.Movie;
import com.example.tac_projet_gallego_fofana.api.MovieCatalog;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;
import com.example.tac_projet_gallego_fofana.recycler.CustomAdapterFavoris;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavorisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavorisFragment extends Fragment {

    public static final String TAB_NAME = "Favoris";

    private String TAG = "MATDAVFF";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CustomAdapterFavoris customAdapter;
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieFragment.
     */
    public static FavorisFragment newInstance() {
        FavorisFragment fragment = new FavorisFragment();
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        */
        return fragment;
    }

    public FavorisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favoris, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);


        API_Interface apiService = API_Client.getClient().create(API_Interface.class);
        Call<GenreCatalog> callAllGenres = apiService.getAllGenres();
        callAllGenres.enqueue(new Callback<GenreCatalog>() {
            @Override
            public void onResponse(Call<GenreCatalog> call, Response<GenreCatalog> response) {
                Log.d(TAG, "on response : genres");
                List<Genre> genre_list = response.body().getGenres();
                for (Genre genre : genre_list) {
                    Log.d(TAG, "in loop : genres " + genre.getId() + " : " + genre.getName());
                    genre_dictionary.put(genre.getId(), genre.getName());
                }
                displayListOfGenres(genre_list);
            }

            @Override
            public void onFailure(Call<GenreCatalog> call, Throwable t) {
                Log.d(TAG, "on failure");
            }
        });

        Call<MovieCatalog> callPopMovies = apiService.getPopularMovies();
        callPopMovies.enqueue(new Callback<MovieCatalog>() {
            @Override
            public void onResponse(Call<MovieCatalog> call, Response<MovieCatalog> response) {
                //Log.d(TAG,"on response");
                List<Movie> movie_list = response.body().getResults();
                List<FavMovie> listeFavoris = mainActivityViewModel.getFavMovie().getValue();
                //Log.d(TAG,"observe");
                mainActivityViewModel.getAllFavMovies();
                getListGenre(movie_list,listeFavoris);
                customAdapter = new CustomAdapterFavoris(view.getContext(), listeFavoris, mainActivityViewModel, genre_dictionary,startForResult);
                recyclerView.setAdapter(customAdapter);
                displayListOfFavoris(listeFavoris);

            }
            @Override
            public void onFailure(Call<MovieCatalog> call, Throwable t) {
                Log.d(TAG,"on failure");
            }
        });

    }

    private void displayListOfGenres(List<Genre> genres){
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : genres){
            stringBuilder.append("-"+genre.getId()+" : "+genre.getName()+"\n");
        }
        Log.d(TAG, "r??ponse = \n" + stringBuilder);
    }

    private void displayListOfFavoris(List<FavMovie> favoris){
        StringBuilder stringBuilder = new StringBuilder();
        for (FavMovie fav : favoris){
            stringBuilder.append("-"+fav.getTitle()+"\n");
        }
        Log.d(TAG, "r??ponse = \n" + stringBuilder);
    }

    private void getListGenre(List<Movie> movieList, List<FavMovie> favMovieList){
        for (FavMovie favM: favMovieList) {
            for (int i = 0; i < movieList.size(); i++) {
                if (Objects.equals(movieList.get(i).getId(), favM.getId())){
                    favM.setGenreIds(movieList.get(i).getGenreIds());
                    break;
                }
            }
        }
    }
}