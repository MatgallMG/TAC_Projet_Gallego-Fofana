package com.example.tac_projet_gallego_fofana;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tac_projet_gallego_fofana.data.My_Repository;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends AndroidViewModel {

    private final String LOG = "MATDAV";
    public final MutableLiveData<List<FavMovie>> allFavMovies;
    private My_Repository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        allFavMovies = new MutableLiveData<>();

        createRepository(application);
        initAllFavMovies();
    }

    private void createRepository(Application application) {
        repository = new My_Repository(application);
    }

    private void initAllFavMovies() {
        Observable<List<FavMovie>> favMovies = repository.getAllFavMovies();
        Observer<List<FavMovie>> observer = favMovies.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<FavMovie>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOG, "subscribe ON");
                    }

                    @Override
                    public void onNext(List<FavMovie> favMovieList) {
                        Log.d(LOG, "next");
                        allFavMovies.setValue(favMovieList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG, "ERREUR : " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG, "complete");
                    }
                });
    }

    public Boolean isAlreadyFavorite(int movieId) {
        return Objects.requireNonNull(allFavMovies.getValue()).stream().map(FavMovie::getId).collect(Collectors.toList()).contains(movieId);
    }

    public LiveData<List<FavMovie>> getAllFavMovies() { return allFavMovies; }

    public void addFavMovie(FavMovie favMovie) {
        repository.insert(favMovie);
        allFavMovies.setValue(repository.getAllFavMovies().blockingFirst());
    }

    public void deleteFavMovie(Integer id) {
        repository.deleteById(id);
        allFavMovies.setValue(repository.getAllFavMovies().blockingFirst());
    }
}
