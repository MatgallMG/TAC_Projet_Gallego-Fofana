package com.example.tac_projet_gallego_fofana.recycler;

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
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tac_projet_gallego_fofana.DetailActivity;
import com.example.tac_projet_gallego_fofana.MainActivityViewModel;
import com.example.tac_projet_gallego_fofana.R;
import com.example.tac_projet_gallego_fofana.api.Movie;

import com.bumptech.glide.Glide;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyviewHolder> {

    private String TAG = "MATDAV";
    private List<Movie> movie_list;
    private Context context;
    private MainActivityViewModel mainActivityViewModel;
    private Map<Integer, String> genre_dictionary;
    private ActivityResultLauncher<Intent> startForResult;

    public CustomAdapter(Context context, List<Movie> movie_list, MainActivityViewModel mainActivityViewModel, Map<Integer, String> genre_dictionary, ActivityResultLauncher<Intent> startForResult) {
        this.context = context;
        this.movie_list = movie_list;
        this.mainActivityViewModel = mainActivityViewModel;
        this.genre_dictionary = genre_dictionary;
        this.startForResult = startForResult;
    }

    public void setMovieList(List<Movie> movie_list) {
        this.movie_list = movie_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyviewHolder holder, int position) {

        Movie currentMovie = movie_list.get(position);

        List<String> genres = new ArrayList<>();

        for (Integer genreId : currentMovie.getGenreIds()) {
            genres.add(genre_dictionary.get(genreId));
        }

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500"+currentMovie.getPosterPath())
                .into(holder.moviePoster);

        holder.movieTitle.setText(currentMovie.getTitle());
        holder.movieNote.setText(currentMovie.getVoteAverage().toString());
        holder.movieDate.setText(currentMovie.getReleaseDate());
        holder.movieGenres.setText(String.join(" | ", genres));

        if (holder.isAlreadyFavorite(currentMovie)) {
            holder.imageButtonFav.setImageResource(R.drawable.star_filled);
        }
    }

    @Override
    public int getItemCount() {
        if (movie_list != null) {
            return movie_list.size();
        }
        return 0;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout movieBox;
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieGenres;
        TextView movieNote;
        TextView movieDate;
        ImageButton imageButtonFav;

        public MyviewHolder(@NonNull View itemView) {
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
                    Toast.makeText(view.getContext(), "item N°"+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    //Goto Layout2
                    /*Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("MOVIE_TITLE", movie_list.get(getAdapterPosition()).getTitle());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("MOVIE_TITLE", movie_list.get(getAdapterPosition()).getTitle());
                    startForResult.launch(intent);
                }
            });

            imageButtonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "item N°"+getAdapterPosition()+" Favorited !", Toast.LENGTH_SHORT).show();

                    Movie currentMovie = movie_list.get(getAdapterPosition());

                    if (isAlreadyFavorite(currentMovie)) {
                        removeFavMovieFromDB(currentMovie);
                        imageButtonFav.setImageResource(R.drawable.star_empty);
                    } else {
                        addFavMovieToDB(currentMovie);
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

        public void removeFavMovieFromDB(Movie m) {
            Toast.makeText(context, "Retrait des favoris : "+m.getTitle(), Toast.LENGTH_SHORT).show();
            mainActivityViewModel.deleteFavMovie(m.getId());
        }
    }
}
