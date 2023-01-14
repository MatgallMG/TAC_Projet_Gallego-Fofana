package com.example.tac_projet_gallego_fofana.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tac_projet_gallego_fofana.DetailActivity;
import com.example.tac_projet_gallego_fofana.MainActivityViewModel;
import com.example.tac_projet_gallego_fofana.R;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CustomAdapterFavoris extends ListAdapter<FavMovie, CustomAdapterFavoris.MyViewHolder> {


    private final Map<Integer, List<Integer>> movieGenres;
    private final Context context;
    private final MainActivityViewModel mainActivityViewModel;
    private final Map<Integer, String> genre_dictionary;
    private final ActivityResultLauncher<Intent> startForResult;
    public int item_layout_type;

    public CustomAdapterFavoris(Context context, MainActivityViewModel mainActivityViewModel, Map<Integer, String> genre_dictionary, ActivityResultLauncher<Intent> startForResult, int item_layout_type, Map<Integer, List<Integer>> movieGenres) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.mainActivityViewModel = mainActivityViewModel;
        this.genre_dictionary = genre_dictionary;
        this.startForResult = startForResult;
        this.item_layout_type = item_layout_type;
        this.movieGenres = movieGenres;
    }

    private static final DiffUtil.ItemCallback<FavMovie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<FavMovie>() {
                @Override
                public boolean areItemsTheSame(@NonNull FavMovie oldFavMovies, @NonNull FavMovie newFavMovies) {
                    return oldFavMovies.equals(newFavMovies);
                }

                @Override
                public boolean areContentsTheSame(@NonNull FavMovie oldFavMovies, @NonNull FavMovie newFavMovies) {
                    return oldFavMovies.getId().equals(newFavMovies.getId());
                }
            };


    public void setItemLayoutType(int item_layout_type) {
        this.item_layout_type = item_layout_type;
    }

    public int getItemLayoutType() {
        return this.item_layout_type;
    }

    public void setGenres(FavMovie favMovie) {
        favMovie.setGenreIds(movieGenres.get(favMovie.getId()));
    }


    @NonNull
    @Override
    public CustomAdapterFavoris.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_layout_type, parent, false);
        return new MyViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomAdapterFavoris.MyViewHolder holder, int position) {

        FavMovie favMovie = getItem(position);
        setGenres(favMovie);
        List<String> genres = new ArrayList<>();
        for (Integer genreId : favMovie.getGenreIds()){
            genres.add(genre_dictionary.get(genreId));
        }
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + favMovie.getPosterPath())
                .into(holder.moviePoster);

        holder.movieTitle.setText(curtail(favMovie.getTitle()));
        holder.movieNote.setText(favMovie.getVoteAverage().toString());
        holder.movieDate.setText(favMovie.getReleaseDate());
        holder.movieGenres.setText(curtail(genres));
        holder.imageButtonFav.setImageResource(R.drawable.star_filled);
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
        StringBuilder res = new StringBuilder();
        int i = 0;
        while (i != genres.size()) {
            res.append(genres.get(i));
            i++;
            if (res.length() >= 40) return res+"...";
            res.append(" | ");
        }
        return res.toString();
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

            movieBox.setOnClickListener(view -> {
                Intent intent = new Intent(context, DetailActivity.class);
                FavMovie currentMovie = getItem(getAdapterPosition());
                intent.putExtra("MOVIE_ID", currentMovie.getId());
                intent.putExtra("MOVIE_POSTER_PATH", currentMovie.getPosterPath());
                intent.putExtra("MOVIE_BACKDROP_PATH", currentMovie.getBackdropPath());
                intent.putExtra("IS_FAVORITED", mainActivityViewModel.isAlreadyFavorite(currentMovie.getId()) ? R.drawable.star_filled : R.drawable.star_empty);
                startForResult.launch(intent);
            });

            imageButtonFav.setOnClickListener(view -> {
                //Toast.makeText(view.getContext(), "item N°"+getAdapterPosition()+" Favorited !", Toast.LENGTH_SHORT).show();
                FavMovie currentFavMovie = getItem(getAdapterPosition());
                removeFavMovieFromDB(currentFavMovie);
            });
        }

        public void removeFavMovieFromDB(FavMovie m) {
            //Toast.makeText(context, "Retrait des favoris : "+m.getTitle(), Toast.LENGTH_SHORT).show();
            mainActivityViewModel.deleteFavMovie(m.getId());
        }
    }
}
