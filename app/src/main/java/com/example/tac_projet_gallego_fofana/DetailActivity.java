package com.example.tac_projet_gallego_fofana;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tac_projet_gallego_fofana.api.API_Client;
import com.example.tac_projet_gallego_fofana.api.API_Interface;
import com.example.tac_projet_gallego_fofana.api.Movie;
import com.example.tac_projet_gallego_fofana.api.MovieCatalog;
import com.example.tac_projet_gallego_fofana.api.MovieDetails;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;
import com.example.tac_projet_gallego_fofana.recycler.CustomAdapterMovie;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout_detail);
        Toast.makeText(this, "on create detail", Toast.LENGTH_SHORT).show();

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
        Movie currentMovie = (Movie) intent.getSerializableExtra("MOVIE");

        API_Interface apiService = API_Client.getClient().create(API_Interface.class);
        Call<MovieDetails> callMovieDetails = apiService.getDetails(currentMovie.getId());
        callMovieDetails.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                Log.d("MATDAV","on response : callMovieDetails");
                itemDetailMovieTagLine.setText(response.body().getTagline());
                itemDetailMovieRuntime.setText(String.valueOf(getTimeString(response.body().getRuntime())));
                itemDetailMovieStatus.setText(response.body().getStatus());
            }
            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.d("MATDAV","on failure");
            }

            public String getTimeString(int min) {
                return (min/60)+"h"+(min%60);
            }
        });

        // SET ALL THE MOVIE INFOS INSIDE THE DETAIL CARD
        favButton.setImageResource(intent.getIntExtra("IS_FAVORITED", R.drawable.star_empty));
        itemDetailMovieTitle.setText(currentMovie.getTitle());
        itemDetailMovieTagLine.setText("TAG_LINE");
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + currentMovie.getPosterPath())
                .into(itemDetailMoviePoster);
        itemDetailMovieVote.setText("Note : " + String.valueOf(currentMovie.getVoteAverage()));
        itemDetailMovieOverview.setText("OVERVIEW : " + currentMovie.getOverview());
        itemMovieGenres.setText("GENRES : " + intent.getStringExtra("MOVIE_GENRES"));
        itemDetailMovieLanguage.setText(currentMovie.getOriginalLanguage());
        itemDetailMovieReleaseDate.setText(currentMovie.getReleaseDate());
        itemDetailMovieRuntime.setText("MOVIE_RUNTIME");
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + currentMovie.getBackdropPath())
                .into(itemDetailMovieBackdrop);
        itemDetailMovieStatus.setText("MOVIE_STATUS");

        // INFORM THE USER THAT THEY CAN'T INTERACT WITH THE STAR
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation shake = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);
                favButton.startAnimation(shake);
            }
        });
    }


    public void navMainLayout(View view) {
        //Goto mainLayout
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
