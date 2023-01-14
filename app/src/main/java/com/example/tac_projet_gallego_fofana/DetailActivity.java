package com.example.tac_projet_gallego_fofana;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tac_projet_gallego_fofana.api.API_Client;
import com.example.tac_projet_gallego_fofana.api.API_Interface;
import com.example.tac_projet_gallego_fofana.api.Genre;
import com.example.tac_projet_gallego_fofana.api.MovieDetails;

import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout_detail);
        //Toast.makeText(this, "on create detail", Toast.LENGTH_SHORT).show();

        ImageButton favButton = findViewById(R.id.favButton);
        TextView itemDetailMovieTitle = findViewById(R.id.itemDetailMovieTitle);
        TextView itemDetailMovieTagLine = findViewById(R.id.itemDetailMovieTagLine);
        ImageView itemDetailMoviePoster = findViewById(R.id.itemDetailMoviePoster);
        TextView itemDetailMovieVote = findViewById(R.id.itemDetailMovieVote);
        TextView itemDetailMovieOverview = findViewById(R.id.itemDetailMovieOverview);
        TextView itemMovieGenres = findViewById(R.id.itemMovieGenres);
        TextView itemDetailMovieLanguage = findViewById(R.id.itemDetailMovieLanguage);
        TextView itemDetailMovieReleaseDate = findViewById(R.id.itemDetailMovieReleaseDate);
        TextView itemDetailMovieRuntime = findViewById(R.id.itemDetailMovieRuntime);
        ImageView itemDetailMovieBackdrop = findViewById(R.id.itemDetailMovieBackdrop);
        TextView itemDetailMovieStatus = findViewById(R.id.itemDetailMovieStatus);

        Intent intent = getIntent();
        int currentMovieId = intent.getIntExtra("MOVIE_ID", 808);

        // GET THE MOVIE DETAIL
        API_Interface apiService = API_Client.getClient().create(API_Interface.class);
        Call<MovieDetails> callMovieDetails = apiService.getDetails(currentMovieId);
        callMovieDetails.enqueue(new Callback<MovieDetails>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                // UPDATE THE DETAIL CARD
                assert response.body() != null;
                itemDetailMovieTitle.setText(response.body().getTitle());
                itemDetailMovieTagLine.setText(response.body().getTagline());
                itemDetailMovieVote.setText("Note : " + response.body().getVoteAverage());
                itemDetailMovieOverview.setText("OVERVIEW : " + response.body().getOverview());
                itemMovieGenres.setText("GENRES : " + response.body().getGenres().stream().map(genre -> ((Genre) genre).getName()).collect(Collectors.joining(" | ")));
                itemDetailMovieLanguage.setText(response.body().getOriginalLanguage());
                itemDetailMovieReleaseDate.setText(response.body().getReleaseDate());
                itemDetailMovieRuntime.setText(getTimeString(response.body().getRuntime()));
                itemDetailMovieStatus.setText(response.body().getStatus());
            }
            @Override
            public void onFailure(@NonNull Call<MovieDetails> call, @NonNull Throwable t) {
                Log.d("MATDAV","on failure : callMovieDetails");
            }

            public String getTimeString(int min) {
                return (min/60)+"h"+(min%60);
            }
        });

        // SET THE IMAGES
        favButton.setImageResource(intent.getIntExtra("IS_FAVORITED", R.drawable.star_empty));
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + intent.getStringExtra("MOVIE_POSTER_PATH"))
                .into(itemDetailMoviePoster);
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + intent.getStringExtra("MOVIE_BACKDROP_PATH"))
                .into(itemDetailMovieBackdrop);
        itemDetailMovieStatus.setText("MOVIE_STATUS");

        // INFORM THE USER THAT THEY CAN'T INTERACT WITH THE STAR
        favButton.setOnClickListener(view -> {
            Animation shake = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);
            favButton.startAnimation(shake);
        });
    }


    public void navMainLayout(View view) {
        //Goto mainLayout
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
