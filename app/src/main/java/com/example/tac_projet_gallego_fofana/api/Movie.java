package com.example.tac_projet_gallego_fofana.api;

import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {

    public Movie(FavMovie fm) {
        this.id = fm.getId();
        this.adult = fm.getAdult();
        this.backdropPath = fm.getBackdropPath();
        this.genreIds = fm.getGenreIds();
        this.originalLanguage = fm.getOriginalLanguage();
        this.originalTitle = fm.getOriginalTitle();
        this.overview = fm.getOverview();
        this.popularity = fm.getPopularity();
        this.posterPath = fm.getPosterPath();
        this.releaseDate = fm.getReleaseDate();
        this.title = fm.getTitle();
        this.video = fm.getVideo();
        this.voteAverage = fm.getVoteAverage();
        this.voteCount = fm.getVoteCount();
    }

    @SerializedName("adult")
    @Expose
    private final Boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    private final String backdropPath;
    @SerializedName("genre_ids")
    @Expose
    private final List<Integer> genreIds;
    @SerializedName("id")
    @Expose
    private final Integer id;
    @SerializedName("original_language")
    @Expose
    private final String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private final String originalTitle;
    @SerializedName("overview")
    @Expose
    private final String overview;
    @SerializedName("popularity")
    @Expose
    private final Double popularity;
    @SerializedName("poster_path")
    @Expose
    private final String posterPath;
    @SerializedName("release_date")
    @Expose
    private final String releaseDate;
    @SerializedName("title")
    @Expose
    private final String title;
    @SerializedName("video")
    @Expose
    private final Boolean video;
    @SerializedName("vote_average")
    @Expose
    private final Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private final Integer voteCount;

    public Boolean getAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public Integer getId() {
        return id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getVideo() {
        return video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

}