package com.example.tac_projet_gallego_fofana.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class FavMovie {

    @PrimaryKey() private Integer id;
    @ColumnInfo(name = "adult") private final Boolean adult;
    @ColumnInfo(name = "backdropPath") private final String backdropPath;
    @ColumnInfo(name = "originalLanguage") private final String originalLanguage;
    @ColumnInfo(name = "originalTitle") private final String originalTitle;
    @ColumnInfo(name = "overview") private final String overview;
    @ColumnInfo(name = "popularity") private final Double popularity;
    @ColumnInfo(name = "posterPath") private final String posterPath;
    @ColumnInfo(name = "releaseDate") private final String releaseDate;
    @ColumnInfo(name = "title") private final String title;
    @ColumnInfo(name = "video") private final Boolean video;
    @ColumnInfo(name = "voteAverage") private final Double voteAverage;
    @ColumnInfo(name = "voteCount") private final Integer voteCount;

    @Ignore private List<Integer> genreIds;

    public FavMovie(Integer id, Boolean adult, String backdropPath, String originalLanguage, String originalTitle, String overview, Double popularity, String posterPath, String releaseDate, String title, Boolean video, Double voteAverage, Integer voteCount) {
        this.id = id;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
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
