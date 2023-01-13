package com.example.tac_projet_gallego_fofana.recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tac_projet_gallego_fofana.DetailActivity;
import com.example.tac_projet_gallego_fofana.MainActivityViewModel;
import com.example.tac_projet_gallego_fofana.R;

import com.bumptech.glide.Glide;
import com.example.tac_projet_gallego_fofana.api.Movie;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CustomAdapterMovie extends RecyclerView.Adapter<CustomAdapterMovie.MyViewHolder> {

    private String TAG = "MATDAV";
    private List<Movie> movie_list;
    private Context context;
    private MainActivityViewModel mainActivityViewModel;
    private Map<Integer, String> genre_dictionary;
    private ActivityResultLauncher<Intent> startForResult;
    private int item_layout_type;

    public CustomAdapterMovie(Context context, List<Movie> movie_list, MainActivityViewModel mainActivityViewModel, Map<Integer, String> genre_dictionary, ActivityResultLauncher<Intent> startForResult, int item_layout_type) {
        this.context = context;
        this.movie_list = movie_list;
        this.mainActivityViewModel = mainActivityViewModel;
        this.genre_dictionary = genre_dictionary;
        this.startForResult = startForResult;
        this.item_layout_type = item_layout_type;
    }

    public void setItemLayoutType(int item_layout_type) {
        this.item_layout_type = item_layout_type;
    }

    public int getItemLayoutType() {
        return this.item_layout_type;
    }

    public void setMovieList(List<Movie> movie_list) {
        this.movie_list = movie_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_layout_type, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Movie currentMovie = movie_list.get(position);

        List<String> genres = new ArrayList<>();

        for (Integer genreId : currentMovie.getGenreIds()) {
            genres.add(genre_dictionary.get(genreId));
        }

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500"+ currentMovie.getPosterPath())
                .into(holder.moviePoster);

        holder.movieTitle.setText(curtail(currentMovie.getTitle()));
        holder.movieNote.setText(currentMovie.getVoteAverage().toString());
        holder.movieDate.setText(currentMovie.getReleaseDate());
        holder.movieGenres.setText(curtail(genres));

        if (holder.isAlreadyFavorite(currentMovie)) {
            holder.imageButtonFav.setImageResource(R.drawable.star_filled);
        } else {
            holder.imageButtonFav.setImageResource(R.drawable.star_empty);
        }
    }

    private String curtail(String title) {
        if (item_layout_type == R.layout.item_layout) {
            // LINEAR
            if (title.length() < 35) return title;
            return title.substring(0, 35).trim() + "...";
        } else {
            // GRID
            if (title.length() < 25) return title;
            return title.substring(0, 25).trim() + "...";
        }
    }

    private String curtail(List<String> genres) {
        // GRID : genres are set to visibility:invisible in xml layout anyways
        if (item_layout_type == R.layout.item_layout_grid) return "";
        // LINEAR
        String res = "";
        int i = 0;
        while (i != genres.size()) {
            res += genres.get(i);
            i++;
            if (res.length() >= 40) return res+"...";
            res += " | ";
        }
        return res;
    }

    @Override
    public int getItemCount() {
        if (movie_list != null) {
            return movie_list.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout movieBox;
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieGenres;
        TextView movieNote;
        TextView movieDate;
        ImageButton imageButtonFav;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            movieBox = (ConstraintLayout) itemView.findViewById(R.id.boxItem);
            moviePoster = (ImageView) itemView.findViewById(R.id.itemMovieImage);
            movieTitle = (TextView) itemView.findViewById(R.id.itemMovieTitle);
            movieGenres = (TextView) itemView.findViewById(R.id.itemMovieGenres);
            movieNote = (TextView) itemView.findViewById(R.id.itemMovieNote);
            movieDate = (TextView) itemView.findViewById(R.id.itemMovieReleaseDate);
            imageButtonFav = (ImageButton) itemView.findViewById(R.id.favButton);

            movieBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "item N°"+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    Movie currentMovie = movie_list.get(getAdapterPosition());

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("MOVIE_TITLE", currentMovie.getTitle());
                    intent.putExtra("MOVIE_POSTER", currentMovie.getPosterPath());
                    //TagLine
                    intent.putExtra("MOVIE_VOTE", currentMovie.getVoteAverage());
                    intent.putExtra("MOVIE_OVERVIEW", currentMovie.getOverview());
                    //Onlined genres
                    intent.putExtra("MOVIE_GENRES", String.join(" | ", currentMovie.getGenreIds().stream().map((genreId) -> genre_dictionary.get(genreId)).collect(Collectors.toList())));
                    intent.putExtra("MOVIE_LANGUAGE", movie_list.get(getAdapterPosition()).getOriginalLanguage());
                    intent.putExtra("MOVIE_RELEASE_DATE", movie_list.get(getAdapterPosition()).getReleaseDate());
                    //Runtime
                    intent.putExtra("MOVIE_BACKDROP", movie_list.get(getAdapterPosition()).getBackdropPath());
                    //Status
                    startForResult.launch(intent);
                }
            });

            imageButtonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "item N°"+getAdapterPosition()+" Favorited !", Toast.LENGTH_SHORT).show();

                    Movie currentFavMovie = movie_list.get(getAdapterPosition());

                    if (isAlreadyFavorite(currentFavMovie)) {
                        removeFavMovieFromDB(currentFavMovie,getAdapterPosition());
                        imageButtonFav.setImageResource(R.drawable.star_empty);
                    } else {
                        addFavMovieToDB(currentFavMovie);
                        imageButtonFav.setImageResource(R.drawable.star_filled);
                    }
                }
            });
        }

        public Boolean isAlreadyFavorite(Movie m) {
            return mainActivityViewModel.allFavMovies.getValue().stream().map(f -> f.getId()).collect(Collectors.toList()).contains(m.getId());
        }

        public void addFavMovieToDB(Movie m) {
            FavMovie newFav = new FavMovie(
                    m.getId(),
                    m.getAdult(),
                    m.getBackdropPath(),
                    m.getOriginalLanguage(),
                    m.getOriginalTitle(),
                    m.getOverview(),
                    m.getPopularity(),
                    m.getPosterPath(),
                    m.getReleaseDate(),
                    m.getTitle(),
                    m.getVideo(),
                    m.getVoteAverage(),
                    m.getVoteCount());
            Toast.makeText(context, "Ajout aux favoris : "+newFav.getTitle(), Toast.LENGTH_SHORT).show();
            mainActivityViewModel.addFavMovie(newFav);
        }

        public void removeFavMovieFromDB(Movie m,int pos) {
            Toast.makeText(context, "Retrait des favoris : "+m.getTitle(), Toast.LENGTH_SHORT).show();
            mainActivityViewModel.deleteFavMovie(m.getId());
        }
    }
}
