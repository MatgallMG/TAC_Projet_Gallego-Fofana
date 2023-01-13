package com.example.tac_projet_gallego_fofana;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tac_projet_gallego_fofana.api.Movie;
import com.example.tac_projet_gallego_fofana.data.My_Repository;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;

import java.util.List;
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

        allFavMovies = new MutableLiveData<List<FavMovie>>();

        createRepository(application);
        getAllFavMovies();
    }

    public void createRepository(Application application) {
        repository = new My_Repository(application);
    }

    public void getAllFavMovies() {
        Observable<List<FavMovie>> persons = repository.getAllFavMovies();
        Observer<List<FavMovie>> observer = persons.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<FavMovie>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOG, "subscribe ON");
                    }

                    @Override
                    public void onNext(List<FavMovie> personList) {
                        Log.d(LOG, "next");
                        allFavMovies.setValue(personList);
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

    public Boolean isAlreadyFavorite(Movie m) {
        return allFavMovies.getValue().stream().map(f -> f.getId()).collect(Collectors.toList()).contains(m.getId());
    }

    public LiveData<List<FavMovie>> getFavMovie() { return allFavMovies; }

    public void addFavMovie(FavMovie favMovie) {
        repository.insert(favMovie);
    }

    public void deleteFavMovie(Integer id) {
        repository.deleteById(id);
    }
}
