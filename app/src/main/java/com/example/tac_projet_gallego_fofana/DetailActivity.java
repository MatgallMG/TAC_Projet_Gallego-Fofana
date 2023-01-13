package com.example.tac_projet_gallego_fofana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.stream.Collectors;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout_detail);
        Toast.makeText(this, "on create detail", Toast.LENGTH_SHORT).show();

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
        itemDetailMovieTitle.setText(intent.getStringExtra("MOVIE_TITLE"));
        itemDetailMovieTagLine.setText("MOVIE_TAGLINE");
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+ intent.getStringExtra("MOVIE_POSTER"))
                .into(itemDetailMoviePoster);
        itemDetailMovieVote.setText("Note : "+String.valueOf(intent.getDoubleExtra("MOVIE_VOTE", (double) 0)));
        itemDetailMovieOverview.setText("OVERVIEW : "+intent.getStringExtra("MOVIE_OVERVIEW"));
        itemMovieGenres.setText("GENRES : "+intent.getStringExtra("MOVIE_GENRES"));
        itemDetailMovieLanguage.setText(intent.getStringExtra("MOVIE_LANGUAGE"));
        itemDetailMovieReleaseDate.setText(intent.getStringExtra("MOVIE_RELEASE_DATE"));
        itemDetailMovieRuntime.setText("MOVIE_RUNTIME");
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+ intent.getStringExtra("MOVIE_BACKDROP"))
                .into(itemDetailMovieBackdrop);
        itemDetailMovieStatus.setText("MOVIE_STATUS");

    }

    public void navMainLayout(View view) {
        //Goto mainLayout
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
