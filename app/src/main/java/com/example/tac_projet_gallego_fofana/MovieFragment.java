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
import com.example.tac_projet_gallego_fofana.recycler.CustomAdapter;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment newInstance() {
        MovieFragment Mfragment = new MovieFragment();
        return Mfragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                Log.d(TAG,"on response");
                List<Movie> movie_list = response.body().getResults();
                customAdapter = new CustomAdapter(view.getContext(), movie_list, mainActivityViewModel, genre_dictionary, startForResult);
                recyclerView.setAdapter(customAdapter);
                displayListOfMovies(movie_list);

            }
            @Override
            public void onFailure(Call<MovieCatalog> call, Throwable t) {
                Log.d(TAG,"on failure");
            }
        });
    }

    private void displayListOfMovies(List<Movie> movies){
        StringBuilder stringBuilder = new StringBuilder();
        for (Movie movie : movies){
            stringBuilder.append("-"+movie.getTitle()+"\n");
        }
        Log.d(TAG, "réponse = \n" + stringBuilder);
    }

    private void displayListOfGenres(List<Genre> genres){
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : genres){
            stringBuilder.append("-"+genre.getId()+" : "+genre.getName()+"\n");
        }
        Log.d(TAG, "réponse = \n" + stringBuilder);
    }
}