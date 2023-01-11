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
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tac_projet_gallego_fofana.DetailActivity;
import com.example.tac_projet_gallego_fofana.MainActivityViewModel;
import com.example.tac_projet_gallego_fofana.R;

import com.bumptech.glide.Glide;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CustomAdapterFavoris extends RecyclerView.Adapter<CustomAdapterFavoris.MyViewHolder> {

    private String TAG = "MATDAV";
    private List<FavMovie> favMovie_list;
    private Context context;
    private MainActivityViewModel mainActivityViewModel;
    private Map<Integer, String> genre_dictionary;
    private ActivityResultLauncher<Intent> startForResult;

    public CustomAdapterFavoris(Context context, List<FavMovie> favMovie_list, MainActivityViewModel mainActivityViewModel, Map<Integer, String> genre_dictionary, ActivityResultLauncher<Intent> startForResult) {
        this.context = context;
        this.favMovie_list = favMovie_list;
        this.mainActivityViewModel = mainActivityViewModel;
        this.genre_dictionary = genre_dictionary;
        this.startForResult = startForResult;
    }

    public void setMovieList(List<FavMovie> favMovie_list) {
        this.favMovie_list = favMovie_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomAdapterFavoris.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterFavoris.MyViewHolder holder, int position) {

        FavMovie currentFavMovie = favMovie_list.get(position);

        List<String> genres = new ArrayList<>();

        for (Integer genreId : currentFavMovie.getGenreIds()) {
            genres.add(genre_dictionary.get(genreId));
        }

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500"+ currentFavMovie.getPosterPath())
                .into(holder.moviePoster);

        holder.movieTitle.setText(currentFavMovie.getTitle());
        holder.movieNote.setText(currentFavMovie.getVoteAverage().toString());
        holder.movieDate.setText(currentFavMovie.getReleaseDate());
        holder.movieGenres.setText(String.join(" | ", genres));
        holder.imageButtonFav.setImageResource(R.drawable.star_filled);

    }

    @Override
    public int getItemCount() {
        if (favMovie_list != null) {
            return favMovie_list.size();
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
                    Toast.makeText(view.getContext(), "item N°"+getAdapterPosition(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("MOVIE_TITLE", favMovie_list.get(getAdapterPosition()).getTitle());
                    startForResult.launch(intent);
                }
            });

            imageButtonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "item N°"+getAdapterPosition()+" Favorited !", Toast.LENGTH_SHORT).show();

                    FavMovie currentFavMovie = favMovie_list.get(getAdapterPosition());

                    removeFavMovieFromDB(currentFavMovie);
                    imageButtonFav.setImageResource(R.drawable.star_empty);

                }
            });
        }

        public void removeFavMovieFromDB(FavMovie m) {
            Toast.makeText(context, "Retrait des favoris : "+m.getTitle(), Toast.LENGTH_SHORT).show();
            mainActivityViewModel.deleteFavMovie(m.getId());
        }
    }
}
